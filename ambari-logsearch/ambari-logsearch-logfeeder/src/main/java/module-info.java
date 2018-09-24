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
module ambari.logsearch.logfeeder {
  exports org.apache.ambari.logfeeder.common;
  requires java.xml.ws.annotation;

  requires ambari.logsearch.config.api;
  requires ambari.logsearch.config.local;
  requires ambari.logsearch.config.zookeeper;
  requires ambari.logsearch.config.solr;
  requires ambari.logsearch.logfeeder.plugin.api;
  requires ambari.logsearch.logfeeder.container.registry;
  requires ambari.logsearch.log4j2.appender;

  requires log4j;
  requires org.apache.logging.log4j;
  requires org.apache.logging.log4j.core;
  requires org.apache.logging.log4j.jcl;
  requires org.apache.logging.log4j.jul;
  requires org.apache.logging.log4j.slf4j.impl;

  requires javax.inject;
  requires commons.cli;
  requires commons.lang;
  requires commons.io;
  requires gson;
  requires spring.boot.autoconfigure;
  requires spring.boot;
  requires spring.context;
  requires com.google.common;
  requires solr.solrj;
  requires ambari.logsearch.config.json;
  requires org.apache.commons.lang3;
  requires spring.core;
  requires commons.collections;
  requires httpclient;
  requires spring.beans;
  requires hadoop.common;
  requires grok;
  requires ambari.logsearch.appender;
  requires curator.recipes;
  requires curator.framework;
  requires ambari.metrics.common;
  requires commons.csv;
  requires kafka.clients;
  requires org.apache.commons.compress;
  requires ant;
  requires jackson.mapper.asl;
  requires jackson.core.asl;
  requires minio;

}