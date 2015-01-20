/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.server.checks;

import org.apache.ambari.server.AmbariException;
import org.apache.ambari.server.ServiceNotFoundException;
import org.apache.ambari.server.controller.PrereqCheckRequest;
import org.apache.ambari.server.state.Cluster;
import org.apache.ambari.server.state.Config;
import org.apache.ambari.server.state.DesiredConfig;
import org.apache.ambari.server.state.stack.PrerequisiteCheck;
import org.apache.ambari.server.state.stack.PrereqCheckStatus;
import org.apache.ambari.server.state.stack.PrereqCheckType;

import java.util.Map;

/**
 * Checks that YARN has work-preserving restart enabled.
 */
public class ServicesYarnWorkPreservingCheck extends AbstractCheckDescriptor {

  /**
   * Constructor.
   */
  public ServicesYarnWorkPreservingCheck() {
    super("SERVICES_YARN_WP", PrereqCheckType.SERVICE, "YARN work preserving restart should be enabled");
  }

  @Override
  public boolean isApplicable(PrereqCheckRequest request) throws AmbariException {
    final Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
    try {
      cluster.getService("YARN");
    } catch (ServiceNotFoundException ex) {
      return false;
    }
    return true;
  }

  @Override
  public void perform(PrerequisiteCheck prerequisiteCheck, PrereqCheckRequest request) throws AmbariException {
    final String clusterName = request.getClusterName();
    final Cluster cluster = clustersProvider.get().getCluster(clusterName);
    final String configType = "yarn-site";
    final Map<String, DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
    final DesiredConfig desiredConfig = desiredConfigs.get(configType);
    final Config config = cluster.getConfig(configType, desiredConfig.getTag());
    if (!config.getProperties().containsKey("yarn.resourcemanager.work-preserving-recovery.enabled") ||
      !Boolean.getBoolean(config.getProperties().get("yarn.resourcemanager.work-preserving-recovery.enabled"))) {
      prerequisiteCheck.getFailedOn().add("YARN");
      prerequisiteCheck.setStatus(PrereqCheckStatus.FAIL);
      prerequisiteCheck.setFailReason("YARN doesn't have work preserving restart, yarn.resourcemanager.work-preserving-recovery.enabled property is missing");
    }
  }
}
