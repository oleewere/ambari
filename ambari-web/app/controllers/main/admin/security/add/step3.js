/**
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

var App = require('app');
var stringUtils = require('utils/string_utils');

App.MainAdminSecurityAddStep3Controller = Em.Controller.extend({
  name: 'mainAdminSecurityAddStep3Controller',
  hostComponents: [],
  doDownloadCsv: function(){
    var blob = new Blob([stringUtils.arrayToCSV(this.get('hostComponents'))], {type: "text/csv;charset=utf-8"});
    saveAs(blob, "host-principal-keytab-list.csv");
  },
  loadStep: function(){
    var configs = this.get('content.serviceConfigProperties');
    var hosts = App.Host.find();
    var result = [];
    var componentsToDisplay = ['NAMENODE', 'SECONDARY_NAMENODE', 'DATANODE', 'JOBTRACKER', 'ZOOKEEPER_SERVER', 'HIVE_SERVER', 'TASKTRACKER',
      'OOZIE_SERVER', 'NAGIOS_SERVER', 'HBASE_MASTER', 'HBASE_REGIONSERVER'];
    var securityUsers = [];
    if (!securityUsers || securityUsers.length < 1) { // Page could be refreshed in middle
      if (App.testMode) {
        securityUsers.pushObject({id: 'puppet var', name: 'hdfs_user', value: 'hdfs'});
        securityUsers.pushObject({id: 'puppet var', name: 'mapred_user', value: 'mapred'});
        securityUsers.pushObject({id: 'puppet var', name: 'hbase_user', value: 'hbase'});
        securityUsers.pushObject({id: 'puppet var', name: 'hive_user', value: 'hive'});
        securityUsers.pushObject({id: 'puppet var', name: 'smokeuser', value: 'ambari-qa'});
        securityUsers.pushObject({id: 'puppet var', name: 'zk_user', value: 'zookeeper'});
        securityUsers.pushObject({id: 'puppet var', name: 'oozie_user', value: 'oozie'});
        securityUsers.pushObject({id: 'puppet var', name: 'nagios_user', value: 'nagios'});
        securityUsers.pushObject({id: 'puppet var', name: 'user_group', value: 'hadoop'});
      } else {
        App.router.get('mainAdminSecurityController').setSecurityStatus();
        securityUsers = App.router.get('mainAdminSecurityController').get('serviceUsers');
      }
    }
    var isHbaseInstalled = App.Service.find().findProperty('serviceName', 'HBASE');
    var generalConfigs = configs.filterProperty('serviceName', 'GENERAL');
    var realm = generalConfigs.findProperty('name', 'kerberos_domain').value;
    var smokeUserId = securityUsers.findProperty('name', 'smokeuser').value;
    var hdfsUserId = securityUsers.findProperty('name', 'hdfs_user').value;
    var hbaseUserId = securityUsers.findProperty('name', 'hbase_user').value;
    var mapredUserId = securityUsers.findProperty('name', 'mapred_user').value;
    var hiveUserId = securityUsers.findProperty('name', 'hive_user').value;
    var zkUserId = securityUsers.findProperty('name', 'zk_user').value;
    var oozieUserId = securityUsers.findProperty('name', 'oozie_user').value;
    var nagiosUserId = securityUsers.findProperty('name', 'nagios_user').value;
    var hadoopGroupId = securityUsers.findProperty('name', 'user_group').value;

    var smokeUser = smokeUserId + '@' + realm;
    var hdfsUser = hdfsUserId + '@' + realm;
    var hbaseUser = hbaseUserId + '@' + realm;
    var smokeUserKeytabPath = generalConfigs.findProperty('name', 'smokeuser_keytab').value;
    var hdfsUserKeytabPath = generalConfigs.findProperty('name', 'keytab_path').value + "/hdfs.headless.keytab";
    var hbaseUserKeytabPath = generalConfigs.findProperty('name', 'keytab_path').value + "/hbase.headless.keytab";
    var httpPrincipal = generalConfigs.findProperty('name', 'hadoop_http_principal_name');
    var httpKeytabPath = generalConfigs.findProperty('name', 'hadoop_http_keytab').value;
    var componentToOwnerMap = {
      'NAMENODE': hdfsUserId,
      'SECONDARY_NAMENODE': hdfsUserId,
      'DATANODE': hdfsUserId,
      'TASKTRACKER': mapredUserId,
      'JOBTRACKER': mapredUserId,
      'ZOOKEEPER_SERVER': zkUserId,
      'HIVE_SERVER': hiveUserId,
      'OOZIE_SERVER': oozieUserId,
      'NAGIOS_SERVER': nagiosUserId,
      'HBASE_MASTER': hbaseUserId,
      'HBASE_REGIONSERVER': hbaseUserId
    };

    var addedPrincipalsHost = {}; //Keys = host_principal, Value = 'true'

    hosts.forEach(function(host){
      result.push({
        host: host.get('hostName'),
        component: Em.I18n.t('admin.addSecurity.user.smokeUser'),
        principal: smokeUser,
        keytab: smokeUserKeytabPath,
        owner: smokeUserId,
        group: hadoopGroupId,
        acl: '440'
      });
      result.push({
        host: host.get('hostName'),
        component: Em.I18n.t('admin.addSecurity.user.hdfsUser'),
        principal: hdfsUser,
        keytab: hdfsUserKeytabPath,
        owner: hdfsUserId,
        group: hadoopGroupId,
        acl: '440'
      });
      if (isHbaseInstalled) {
        result.push({
          host: host.get('hostName'),
          component: Em.I18n.t('admin.addSecurity.user.hbaseUser'),
          principal: hbaseUser,
          keytab: hbaseUserKeytabPath,
          owner: hbaseUserId,
          group: hadoopGroupId,
          acl: '440'
        });
      }
      if(host.get('hostComponents').someProperty('componentName', 'NAMENODE') ||
        host.get('hostComponents').someProperty('componentName', 'SECONDARY_NAMENODE') ||
        host.get('hostComponents').someProperty('componentName', 'WEBHCAT_SERVER') ||
        host.get('hostComponents').someProperty('componentName', 'OOZIE_SERVER')){
        result.push({
          host: host.get('hostName'),
          component: Em.I18n.t('admin.addSecurity.user.httpUser'),
          principal: httpPrincipal.value.replace('_HOST', host.get('hostName')) + httpPrincipal.unit,
          keytab: httpKeytabPath,
          owner: 'root',
          group: hadoopGroupId,
          acl: '440'
        });
      }
      host.get('hostComponents').forEach(function(hostComponent){
        if(componentsToDisplay.contains(hostComponent.get('componentName'))){
          var serviceConfigs = configs.filterProperty('serviceName', hostComponent.get('service.serviceName'));
          var principal, keytab;
          serviceConfigs.forEach(function(config){
            if (config.component && config.component === hostComponent.get('componentName')) {
              if (config.name.endsWith('_principal_name')) {
                principal = config.value.replace('_HOST', host.get('hostName')) + config.unit;
              } else if (config.name.endsWith('_keytab') || config.name.endsWith('_keytab_path')) {
                keytab = config.value;
              }
            } else if (config.components && config.components.contains(hostComponent.get('componentName'))) {
              if (config.name.endsWith('_principal_name')) {
                principal = config.value.replace('_HOST', host.get('hostName')) + config.unit;
              } else if (config.name.endsWith('_keytab') || config.name.endsWith('_keytab_path')) {
                keytab = config.value;
              }
            }
          });


          var key = host.get('hostName') + "--" + principal;
          if (!addedPrincipalsHost[key]) {
            var owner = componentToOwnerMap[hostComponent.get('componentName')];
            if(!owner){
              owner = '';
            }
            result.push({
              host: host.get('hostName'),
              component: hostComponent.get('displayName'),
              principal: principal,
              keytab: keytab,
              owner: owner,
              group: hadoopGroupId,
              acl: '400'
            });
            addedPrincipalsHost[key] = true;
          }
        }
      });
    });
    this.set('hostComponents', result);
  }
});
