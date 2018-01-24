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

import org.apache.ambari.logsearch.common.LogSearchConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class UIMappingConfig {

  @Value("#{propertiesSplitter.parseMap('${logsearch.web.service_logs.group.labels:" + LogSearchConstants.SERVICE_GROUP_LABELS_DEFAULTS + "}')}")
  private Map<String, String> serviceGroupLabels;

  @Value("#{propertiesSplitter.parseMap('${logsearch.web.service_logs.component.labels:" + LogSearchConstants.SERVICE_COMPONENT_LABELS_DEFAULTS + "}')}")
  private Map<String, String> serviceComponentLabels;

  @Value("#{propertiesSplitter.parseMap('${logsearch.web.service_logs.field.labels:" + LogSearchConstants.SERVICE_FIELD_LABELS_DEFAULTS + "}')}")
  private Map<String, String> serviceFieldLabels;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.service_logs.field.excludes:" + LogSearchConstants.SERVICE_FIELD_EXCLUDES_DEFAULTS + "}')}")
  private List<String> serviceFieldExcludeList;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.service_logs.field.visible:" + LogSearchConstants.SERVICE_FIELD_VISIBLE_DEFAULTS + "}')}")
  private List<String> serviceFieldVisibleList;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.service_logs.field.filterable.excludes:" + LogSearchConstants.SERVICE_FIELD_FILTERABLE_EXLUDE_DEFAULTS + "}')}")
  private List<String> serviceFieldFilterableExcludesList;

  @Value("#{propertiesSplitter.parseMap('${logsearch.web.audit_logs.component.labels:" + LogSearchConstants.AUDIT_COMPONENT_LABELS_DEFAULTS + "}')}")
  private Map<String, String> auditComponentLabels;

  @Value("#{propertiesSplitter.parseMapInMap('${logsearch.web.audit_logs.field.labels:" + LogSearchConstants.AUDIT_FIELD_LABELS_DEFAULTS + "}')}")
  private Map<String, Map<String, String>> auditFieldLabels;

  @Value("#{propertiesSplitter.parseMap('${logsearch.web.audit_logs.field.common.labels:" + LogSearchConstants.AUDIT_FIELD_COMMON_LABELS_DEFAULTS + "}')}")
  private Map<String, String> auditFieldCommonLabels;

  @Value("#{propertiesSplitter.parseListInMap('${logsearch.web.audit_logs.field.visible:" + LogSearchConstants.AUDIT_FIELD_VISIBLE_DEFAULTS + "}')}")
  private Map<String, List<String>> auditFieldVisibleleMap;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.audit_logs.field.common.visible:" + LogSearchConstants.AUDIT_FIELD_VISIBLE_COMMON_DEFAULTS + "}')}")
  private List<String> auditFieldCommonVisibleList;

  @Value("#{propertiesSplitter.parseListInMap('${logsearch.web.audit_logs.field.excludes:" + LogSearchConstants.AUDIT_FIELD_EXCLUDES_DEFAULTS + "}')}")
  private Map<String, List<String>> auditFieldExcludeMap;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.audit_logs.field.common.excludes:" + LogSearchConstants.AUDIT_FIELD_EXCLUDES_COMMON_DEFAULTS + "}')}")
  private List<String> auditFieldCommonExcludeList;

  @Value("#{propertiesSplitter.parseListInMap('${logsearch.web.audit_logs.field.filterable.excludes:" + LogSearchConstants.AUDIT_FIELD_FILTERABLE_EXCLUDES_DEFAULTS + "}')}")
  private Map<String, List<String>> auditFieldFilterableExcludeMap;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.audit_logs.field.common.filterable.common.excludes:" + LogSearchConstants.AUDIT_FIELD_FILTERABLE_EXCLUDES_COMMON_DEFAULTS + "}')}")
  private List<String> auditFieldCommonFilterableExcludeList;

  @Value("${logsearch.web.labels.fallback.enabled:true}")
  private boolean labelFallbackEnabled;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.labels.service_logs.field.fallback.prefixes:" + LogSearchConstants.SERVICE_FIELD_FALLBACK_PREFIX_DEFAULTS +"}')}")
  private List<String> serviceFieldFallbackPrefixes;

  @Value("#{propertiesSplitter.parseList('${logsearch.web.labels.service_logs.field.fallback.prefixes:" + LogSearchConstants.AUDIT_FIELD_FALLBACK_PREFIX_DEFAULTS + "}')}")
  private List<String> auditFieldFallbackPrefixes;

  private final Map<String, Map<String, String>> mergedAuditFieldLabelMap = new HashMap<>();

  private final Map<String, List<String>> mergedAuditFieldVisibleMap = new HashMap<>();

  private final Map<String, List<String>> mergedAuditFieldExcludeMap = new HashMap<>();

  private final Map<String, List<String>> mergedAuditFieldFilterableExcludesMap = new HashMap<>();

  public Map<String, String> getServiceGroupLabels() {
    return serviceGroupLabels;
  }

  public void setServiceGroupLabels(Map<String, String> serviceGroupLabels) {
    this.serviceGroupLabels = serviceGroupLabels;
  }

  public Map<String, String> getServiceComponentLabels() {
    return serviceComponentLabels;
  }

  public void setServiceComponentLabels(Map<String, String> serviceComponentLabels) {
    this.serviceComponentLabels = serviceComponentLabels;
  }

  public Map<String, String> getAuditComponentLabels() {
    return auditComponentLabels;
  }

  public void setAuditComponentLabels(Map<String, String> auditComponentLabels) {
    this.auditComponentLabels = auditComponentLabels;
  }

  public Map<String, String> getServiceFieldLabels() {
    return serviceFieldLabels;
  }

  public void setServiceFieldLabels(Map<String, String> serviceFieldLabels) {
    this.serviceFieldLabels = serviceFieldLabels;
  }

  public Map<String, Map<String, String>> getAuditFieldLabels() {
    return auditFieldLabels;
  }

  public void setAuditFieldLabels(Map<String, Map<String, String>> auditFieldLabels) {
    this.auditFieldLabels = auditFieldLabels;
  }

  public List<String> getServiceFieldExcludeList() {
    return serviceFieldExcludeList;
  }

  public void setServiceFieldExcludeList(List<String> serviceFieldExcludeList) {
    this.serviceFieldExcludeList = serviceFieldExcludeList;
  }

  public List<String> getServiceFieldVisibleList() {
    return serviceFieldVisibleList;
  }

  public void setServiceFieldVisibleList(List<String> serviceFieldVisibleList) {
    this.serviceFieldVisibleList = serviceFieldVisibleList;
  }

  public Map<String, List<String>> getAuditFieldVisibleleMap() {
    return auditFieldVisibleleMap;
  }

  public void setAuditFieldVisibleleMap(Map<String, List<String>> auditFieldVisibleleMap) {
    this.auditFieldVisibleleMap = auditFieldVisibleleMap;
  }

  public List<String> getAuditFieldCommonVisibleList() {
    return auditFieldCommonVisibleList;
  }

  public void setAuditFieldCommonVisibleList(List<String> auditFieldCommonVisibleList) {
    this.auditFieldCommonVisibleList = auditFieldCommonVisibleList;
  }

  public Map<String, List<String>> getAuditFieldExcludeMap() {
    return auditFieldExcludeMap;
  }

  public void setAuditFieldExcludeMap(Map<String, List<String>> auditFieldExcludeMap) {
    this.auditFieldExcludeMap = auditFieldExcludeMap;
  }

  public List<String> getAuditFieldCommonExcludeList() {
    return auditFieldCommonExcludeList;
  }

  public void setAuditFieldCommonExcludeList(List<String> auditFieldCommonExcludeList) {
    this.auditFieldCommonExcludeList = auditFieldCommonExcludeList;
  }

  public Map<String, String> getAuditFieldCommonLabels() {
    return auditFieldCommonLabels;
  }

  public void setAuditFieldCommonLabels(Map<String, String> auditFieldCommonLabels) {
    this.auditFieldCommonLabels = auditFieldCommonLabels;
  }

  public boolean isLabelFallbackEnabled() {
    return labelFallbackEnabled;
  }

  public void setLabelFallbackEnabled(boolean labelFallbackEnabled) {
    this.labelFallbackEnabled = labelFallbackEnabled;
  }

  public List<String> getServiceFieldFallbackPrefixes() {
    return serviceFieldFallbackPrefixes;
  }

  public void setServiceFieldFallbackPrefixes(List<String> serviceFieldFallbackPrefixes) {
    this.serviceFieldFallbackPrefixes = serviceFieldFallbackPrefixes;
  }

  public List<String> getAuditFieldFallbackPrefixes() {
    return auditFieldFallbackPrefixes;
  }

  public void setAuditFieldFallbackPrefixes(List<String> auditFieldFallbackPrefixes) {
    this.auditFieldFallbackPrefixes = auditFieldFallbackPrefixes;
  }

  public List<String> getServiceFieldFilterableExcludesList() {
    return serviceFieldFilterableExcludesList;
  }

  public void setServiceFieldFilterableExcludesList(List<String> serviceFieldFilterableExcludesList) {
    this.serviceFieldFilterableExcludesList = serviceFieldFilterableExcludesList;
  }

  public Map<String, List<String>> getMergedAuditFieldVisibleMap() {
    return mergedAuditFieldVisibleMap;
  }

  public Map<String, List<String>> getMergedAuditFieldExcludeMap() {
    return mergedAuditFieldExcludeMap;
  }

  public Map<String, Map<String, String>> getMergedAuditFieldLabelMap() {
    return mergedAuditFieldLabelMap;
  }

  public Map<String, List<String>> getMergedAuditFieldFilterableExcludesMap() {
    return mergedAuditFieldFilterableExcludesMap;
  }

  @PostConstruct
  public void init() {
    mergeCommonAndSpecMapValues(auditFieldLabels, auditFieldCommonLabels, mergedAuditFieldLabelMap);
    mergeCommonAndSpecListValues(auditFieldVisibleleMap, auditFieldCommonVisibleList, mergedAuditFieldVisibleMap);
    mergeCommonAndSpecListValues(auditFieldExcludeMap, auditFieldCommonExcludeList, mergedAuditFieldExcludeMap);
    mergeCommonAndSpecListValues(auditFieldFilterableExcludeMap, auditFieldCommonFilterableExcludeList, mergedAuditFieldFilterableExcludesMap);
  }

  private void mergeCommonAndSpecListValues(Map<String, List<String>> specMap, List<String> commonList,
                                            Map<String, List<String>> mergedMap) {
    Set<String> componentFilterableKeys = specMap.keySet();
    for (String component : componentFilterableKeys) {
      List<String> specAuditDataList = specMap.get(component);
      List<String> mergedDataList = new ArrayList<>();
      if (specAuditDataList != null) {
        mergedDataList.addAll(specAuditDataList);
        for (String commonData : commonList) {
          if (!specAuditDataList.contains(commonData)) {
            mergedDataList.add(commonData);
          }
        }
        mergedMap.put(component, mergedDataList);
      }
    }
  }

  private void mergeCommonAndSpecMapValues(Map<String, Map<String, String>> specMap, Map<String, String> commonMap,
                                           Map<String, Map<String, String>> mergedMap) {
    Set<String> componentFilterableKeys = specMap.keySet();
    for (String component : componentFilterableKeys) {
      Map<String, String> specAuditDataMap = specMap.get(component);
      Map<String, String> mergedAuditDataMap = new HashMap<>();
      if (specAuditDataMap != null) {
        mergedAuditDataMap.putAll(specAuditDataMap);
        for (Map.Entry<String, String> entry : commonMap.entrySet()) {
          if (!specAuditDataMap.containsKey(entry.getKey())) {
            mergedAuditDataMap.put(entry.getKey(), entry.getValue());
          }
        }
        mergedMap.put(component, mergedAuditDataMap);
      }
    }
  }

}
