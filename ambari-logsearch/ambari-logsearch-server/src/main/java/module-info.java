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
module ambari.logsearch.server {

  requires java.xml.ws.annotation;
  requires java.xml.bind;

  requires ambari.logsearch.config.api;
  requires ambari.logsearch.config.zookeeper;
  requires ambari.logsearch.config.solr;
  requires ambari.logsearch.logfeeder;

  requires slf4j.api;
  requires log4j;
  requires spring.security.web;
  requires spring.security.core;
  requires spring.security.ldap;
  requires spring.security.config;
  requires spring.ldap.core;
  requires spring.context;
  requires commons.lang;
  requires com.google.common;
  requires java.ws.rs;
  requires javax.servlet.api;
  requires jjwt;
  requires httpclient;
  requires gson;
  requires zookeeper;
  requires swagger.jaxrs;
  requires swagger.models;
  requires swagger.core;
  requires jersey.client;
  requires org.apache.commons.lang3;
  requires commons.collections;
  requires spring.beans;
  requires reflections;
  requires spring.context.support;
  requires freemarker;
  requires spring.core;
  requires spring.web;
  requires jersey.server;
  requires jersey.media.json.jackson;
  requires jersey.container.servlet.core;
  requires spring.boot.autoconfigure;
  requires spring.boot;
  requires jetty.util;
  requires jetty.server;
  requires spring.session;
  requires solr.solrj;
  requires spring.webmvc;
  requires curator.framework;
  requires spring.data.solr;
  requires commons.io;
  requires hadoop.common;
  requires bcprov.jdk15on;
  requires bcpkix.jdk15on;
  requires spring.data.commons;
  requires httpcore;
  requires jettison;
  requires spring.boot.actuator;
  requires com.fasterxml.jackson.databind;
  requires java.validation;
  requires swagger.annotations;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires jsr305;
  requires org.hibernate.validator;
  requires ant;
  requires solr.core;
  requires lucene.analyzers.common;
  requires hadoop.auth;

}