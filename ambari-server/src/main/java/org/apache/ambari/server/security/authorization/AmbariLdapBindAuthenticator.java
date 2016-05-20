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
package org.apache.ambari.server.security.authorization;


import org.apache.ambari.server.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.BindAuthenticator;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.Attributes;


/**
 * An authenticator which binds as a user and checks if user should get ambari
 * admin authorities according to LDAP group membership
 */
public class AmbariLdapBindAuthenticator extends BindAuthenticator {

  private static final Logger LOG = LoggerFactory.getLogger(AmbariLdapBindAuthenticator.class);

  private Configuration configuration;

  private static final String AMBARI_ADMIN_LDAP_ATTRIBUTE_KEY = "ambari_admin";

  public AmbariLdapBindAuthenticator(BaseLdapPathContextSource contextSource,
                                     Configuration configuration) {
    super(contextSource);
    this.configuration = configuration;
  }

  @Override
  public DirContextOperations authenticate(Authentication authentication) {

    DirContextOperations user = super.authenticate(authentication);

    return setAmbariAdminAttr(user);
  }

  /**
   *  Checks weather user is a member of ambari administrators group in LDAP. If
   *  yes, sets user's ambari_admin attribute to true
   * @param user
   * @return
   */
  private DirContextOperations setAmbariAdminAttr(DirContextOperations user) {
    LdapServerProperties ldapServerProperties =
        configuration.getLdapServerProperties();

    LOG.info("Check that user is a member of ambari administrators group in LDAP...");
    String baseDn = ldapServerProperties.getBaseDN().toLowerCase();
    LOG.info("groupBase: {}", ldapServerProperties.getGroupBase().toLowerCase());
    String groupBase = ldapServerProperties.getGroupBase().toLowerCase();
    LOG.info("groupObjectClass: {}", ldapServerProperties.getGroupObjectClass());
    String groupObjectClass = ldapServerProperties.getGroupObjectClass();
    LOG.info("groupMembershipAttr: {}", ldapServerProperties.getGroupMembershipAttr());
    String groupMembershipAttr = ldapServerProperties.getGroupMembershipAttr();
    LOG.info("adminGroupMappingRules: {}", ldapServerProperties.getAdminGroupMappingRules());
    String adminGroupMappingRules =
        ldapServerProperties.getAdminGroupMappingRules();
    LOG.info("groupNamingAttr: {}", ldapServerProperties.getGroupNamingAttr());
    final String groupNamingAttribute =
        ldapServerProperties.getGroupNamingAttr();
    LOG.info("groupSearchFilter: {}", ldapServerProperties.getGroupSearchFilter());
    String groupSearchFilter = ldapServerProperties.getGroupSearchFilter();

    //If groupBase is set incorrectly or isn't set - search in BaseDn
    int indexOfBaseDn = groupBase.indexOf(baseDn);
    groupBase = indexOfBaseDn <= 0 ? "" : groupBase.substring(0,indexOfBaseDn - 1);

    StringBuilder filterBuilder = new StringBuilder();

    filterBuilder.append("(&(");
    filterBuilder.append(groupMembershipAttr);
    filterBuilder.append("=");
    filterBuilder.append(user.getNameInNamespace());//DN

    if ((groupSearchFilter == null) || groupSearchFilter.equals("") ) {
      //If groupSearchFilter is not specified, build it from other authorization
      // group properties
      filterBuilder.append(")(objectclass=");
      filterBuilder.append(groupObjectClass);
      filterBuilder.append(")(|");
      String[] adminGroupMappingRegexs = adminGroupMappingRules.split(",");
      for (String adminGroupMappingRegex : adminGroupMappingRegexs) {
        filterBuilder.append("(");
        filterBuilder.append(groupNamingAttribute);
        filterBuilder.append("=");
        filterBuilder.append(adminGroupMappingRegex);
        filterBuilder.append(")");
      }
      filterBuilder.append(")");
    } else {
      filterBuilder.append(")");
      filterBuilder.append(groupSearchFilter);
    }
    filterBuilder.append(")");

    AttributesMapper attributesMapper = new AttributesMapper() {
      public Object mapFromAttributes(Attributes attrs)
          throws NamingException {
        return attrs.get(groupNamingAttribute).get();
      }
    };

    LdapTemplate ldapTemplate = new LdapTemplate((getContextSource()));
    ldapTemplate.setIgnorePartialResultException(true);
    ldapTemplate.setIgnoreNameNotFoundException(true);
    LOG.info("ambariAdmingGroups - [groupBase] : {}, [filter] {}", groupBase, filterBuilder.toString(),attributesMapper);
    List<String> ambariAdminGroups = ldapTemplate.search(
        groupBase,filterBuilder.toString(),attributesMapper);

    //user has admin role granted, if user is a member of at least 1 group,
    // which matches the rules in configuration
    if (ambariAdminGroups.size() > 0) {
      user.setAttributeValue(AMBARI_ADMIN_LDAP_ATTRIBUTE_KEY, true);
      LOG.info("Admin role granted for user '{}' (dn)", user.getDn());
    } else {
      LOG.info("User '{}' (dn) is not an admin", user.getDn());
    }

    return user;
  }

}
