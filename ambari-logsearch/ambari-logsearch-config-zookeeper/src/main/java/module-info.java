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
module ambari.logsearch.config.zookeeper {
  exports org.apache.ambari.logsearch.config.zookeeper;
  requires ambari.logsearch.config.api;
  requires ambari.logsearch.config.json;
  requires zookeeper;
  requires curator.framework;
  requires curator.recipes;
  requires curator.client;
  requires slf4j.api;
  requires gson;
  requires guava;
  requires commons.collections;
  requires commons.lang3;
}