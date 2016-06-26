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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.BindAuthenticator;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;


/**
 * An authenticator which binds as a user and checks if user should get ambari
 * admin authorities according to LDAP group membership
 */
public class AmbariLdapBindAuthenticator extends BindAuthenticator {

  private static final Logger LOG = LoggerFactory.getLogger(AmbariLdapBindAuthenticator.class);

  private Configuration configuration;

  private static final String AMBARI_ADMIN_LDAP_ATTRIBUTE_KEY = "ambari_admin";

  private static final String MEMBER_DN_FORMAT_ATTRIBUTE = "member";

  public AmbariLdapBindAuthenticator(BaseLdapPathContextSource contextSource,
                                     Configuration configuration) {
    super(contextSource);
    this.configuration = configuration;
  }

  @Override
  public DirContextOperations authenticate(Authentication authentication) {

    DirContextOperations user = super.authenticate(authentication);
    LdapServerProperties ldapServerProperties =
      configuration.getLdapServerProperties();
    if (StringUtils.isNotEmpty(ldapServerProperties.getAdminGroupMappingRules())) {
      user = setAmbariAdminAttr(user, ldapServerProperties);
    }
    return user;
  }

  /**
   *  Checks weather user is a member of ambari administrators group in LDAP. If
   *  yes, sets user's ambari_admin attribute to true
   * @param user
   * @return
   */
  private DirContextOperations setAmbariAdminAttr(DirContextOperations user, LdapServerProperties ldapServerProperties) {
    String groupMembershipAttr = ldapServerProperties.getGroupMembershipAttr();
    String baseDn = ldapServerProperties.getBaseDN().toLowerCase();
    String groupBase = ldapServerProperties.getGroupBase().toLowerCase();
    String groupObjectClass = ldapServerProperties.getGroupObjectClass();
    String adminGroupMappingRules =
        ldapServerProperties.getAdminGroupMappingRules();
    final String groupNamingAttribute =
        ldapServerProperties.getGroupNamingAttr();
    String groupSearchFilter = ldapServerProperties.getGroupSearchFilter();

    //If groupBase is set incorrectly or isn't set - search in BaseDn
    int indexOfBaseDn = groupBase.indexOf(baseDn);
    groupBase = indexOfBaseDn <= 0 ? "" : groupBase.substring(0,indexOfBaseDn - 1);

    String memberValue = "";
    if (MEMBER_DN_FORMAT_ATTRIBUTE.equals(groupMembershipAttr)) {
      memberValue = user.getNameInNamespace();
    } else {
      memberValue = user.getStringAttribute(ldapServerProperties.getUsernameAttribute());
    }
    String setAmbariAdminAttrQuery;
    if ((groupSearchFilter == null) || groupSearchFilter.equals("") ) {
      String adminGroupMappingRegex = createAdminGroupMappingRegex(adminGroupMappingRules, groupNamingAttribute);
      setAmbariAdminAttrQuery = String.format("(&(%s=%s)(objectclass=%s)(|%s))",
        groupMembershipAttr,
        memberValue,
        groupObjectClass,
        adminGroupMappingRegex);
    } else {
      setAmbariAdminAttrQuery = String.format("(&(%s=%s)%s)",
        groupMembershipAttr,
        memberValue,
        groupSearchFilter);
    }
    LOG.debug("LDAP login set admin attr query: {}", setAmbariAdminAttrQuery);

    AttributesMapper attributesMapper = new AttributesMapper() {
      public Object mapFromAttributes(Attributes attrs)
          throws NamingException {
        return attrs.get(groupNamingAttribute).get();
      }
    };

    LdapTemplate ldapTemplate = new LdapTemplate((getContextSource()));
    ldapTemplate.setIgnorePartialResultException(true);
    ldapTemplate.setIgnoreNameNotFoundException(true);

    List<String> ambariAdminGroups = ldapTemplate.search(
        groupBase, setAmbariAdminAttrQuery, attributesMapper);

    //user has admin role granted, if user is a member of at least 1 group,
    // which matches the rules in configuration
    if (ambariAdminGroups.size() > 0) {
      user.setAttributeValue(AMBARI_ADMIN_LDAP_ATTRIBUTE_KEY, true);
    }

    return user;
  }

  private String createAdminGroupMappingRegex(String adminGroupMappingRules, String groupNamingAttribute) {
    String[] adminGroupMappingRegexs = adminGroupMappingRules.split(",");
    StringBuilder builder = new StringBuilder("");
    for (String adminGroupMappingRegex : adminGroupMappingRegexs) {
      builder.append(String.format("(%s=%s)", groupNamingAttribute, adminGroupMappingRegex));
    }
    return builder.toString();
  }

}
