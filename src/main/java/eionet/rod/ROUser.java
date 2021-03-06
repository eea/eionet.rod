/**
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "EINRC-4 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Andre Karpistsenko (TietoEnator)
 */

package eionet.rod;

import java.sql.*;
import java.util.Vector;

import eionet.directory.DirectoryService;
import eionet.rod.services.RODServices;
import eionet.rod.util.sql.ConnectionUtil;

import eionet.acl.AccessControlListIF;
import eionet.acl.AccessController;
import eionet.acl.AuthMechanism;
import eionet.acl.SignOnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <P>
 * WebROD specific implementation of the Uses database to authenticate users.
 * </P>
 *
 * @author Rando Valt
 * @version 1.0
 */

public class ROUser {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ROUser.class);

    protected boolean authented = false;
    protected String user = null;
    protected String password = null;
    protected String fullName = null;
    protected String[] _roles = null;

    public ROUser() {
    }

    /**
     * @param userName
     * @param userPws
     * @return boolean
     *
     */
    public boolean authenticate(String userName, String userPws) {
        invalidate();

        // LOG
        LOGGER.info("Authenticating user '" + userName + "'");

        try {
            // DirectoryService.sessionLogin(userName, userPws);
            AuthMechanism.sessionLogin(userName, userPws);
            // fullName = DirectoryService.getFullName(userName);
            fullName = AuthMechanism.getFullName(userName);

            // LOG
            LOGGER.info("Authenticated!");
            //
            authented = true;
            user = userName;
            password = userPws;

        } catch (Exception e) {

            LOGGER.error("User '" + userName + "' not authenticated", e);
        }
        return authented;
    }

    /**
     * @return boolean
     *
     */
    public boolean isAuthentic() {
        return authented;
    }

    /**
     * @param role
     * @return boolean
     *
     */
    public boolean isUserInRole(String role) {
        boolean b = false;

        if (_roles == null)
            getUserRoles();

        for (int i = 0; i < _roles.length; i++)
            if (_roles[i].equals(role))
                b = true;

        return b;
    }

    /**
     * FullName.
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return String
     *
     */
    public String getUserName() {
        return user;
    }

    /**
     * @return Connection
     *
     */
    public Connection getConnection() {
        try {
            return ConnectionUtil.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string array of roles the user is linked to. Note that the
     * method returns newly constructed array, leaving internal
     * role list unrevealed.
     *
     * @return String[]
     */
    public String[] getUserRoles() {
        // String[] roles;
        if (_roles == null) {
            try {
                Vector<String> v = DirectoryService.getRoles(user);
                String[] roles = new String[v.size()];
                for (int i = 0; i < v.size(); i++) {
                    roles[i] = (String) v.elementAt(i);
                }
                _roles = roles;
            } catch (Exception e) {
                // return empty String, no need for roles
                _roles = new String[] {};
            }
        }
        // return new String[]{};
        return _roles;
    }

    /**
     *
     */
    public void invalidate() {
        authented = false;
        user = null;
        password = null;
    }

    /**
     *
     */
    public String toString() {
        return (user == null ? "" : user);
    }

    /**
     *
     * @param userName
     * @param aclPath
     * @param prm
     * @return boolean
     */
    public static boolean hasPermission(String userName, String aclPath, String prm) {

        if (RODUtil.isNullOrEmpty(userName) || RODUtil.isNullOrEmpty(aclPath) || RODUtil.isNullOrEmpty(prm))
            return false;

        boolean result = false;
        try {
            AccessControlListIF acl = AccessController.getAcl(aclPath);
            if (acl != null) {
                result = acl.checkPermission(userName, prm);
                LOGGER.info("User " + userName + " " + (result ? "has" : "does not have") + " permission " + prm + " in acl \""
                        + aclPath + "\"");
            } else
                LOGGER.info("acl \"" + aclPath + "\" not found!");
        } catch (SignOnException soe) {
            LOGGER.error(soe.toString(), soe);
        }

        return result;
    }

}
