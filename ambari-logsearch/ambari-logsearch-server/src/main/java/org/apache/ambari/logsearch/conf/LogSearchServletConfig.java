/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ambari.logsearch.conf;

import org.apache.ambari.logsearch.configurer.SslConfigurer;
import org.apache.ambari.logsearch.web.listener.LogSearchSessionListener;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.servlet.http.HttpSessionListener;

import java.time.Duration;

import static org.apache.ambari.logsearch.common.LogSearchConstants.LOGSEARCH_APPLICATION_NAME;
import static org.apache.ambari.logsearch.common.LogSearchConstants.LOGSEARCH_SESSION_ID;

@Configuration
public class LogSearchServletConfig {

  private static final Integer SESSION_TIMEOUT = 60 * 30;

  @Inject
  private ServerProperties serverProperties;

  @Inject
  private LogSearchHttpConfig logSearchHttpConfig;

  @Inject
  private SslConfigurer sslConfigurer;

  @Bean
  public HttpSessionListener httpSessionListener() {
    return new LogSearchSessionListener();
  }

  @Bean
  public ServletRegistrationBean jerseyServlet() {
    ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/api/v1/*");
    registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, LogSearchJerseyResourceConfig.class.getName());
    return registration;
  }


  @Bean
  public ConfigurableServletWebServerFactory webServerFactory() {
    JettyServletWebServerFactory webServerFactory = new JettyServletWebServerFactory();
    serverProperties.getServlet().getSession().setTimeout(Duration.ofSeconds(SESSION_TIMEOUT));
    serverProperties.getServlet().getSession().getCookie().setName(LOGSEARCH_SESSION_ID);
    if ("https".equals(logSearchHttpConfig.getProtocol())) {
      sslConfigurer.ensureStorePasswords();
      sslConfigurer.loadKeystore();
      webServerFactory.addServerCustomizers((JettyServerCustomizer) server -> {
        SslContextFactory sslContextFactory = sslConfigurer.getSslContextFactory();
        ServerConnector sslConnector = new ServerConnector(server, sslContextFactory);
        sslConnector.setPort(logSearchHttpConfig.getHttpsPort());
        server.setConnectors(new Connector[]{sslConnector});
      });
    } else {
      webServerFactory.setPort(logSearchHttpConfig.getHttpPort());
    }

    return webServerFactory;
  }

}
