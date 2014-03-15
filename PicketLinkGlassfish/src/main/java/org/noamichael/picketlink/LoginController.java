/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noamichael.picketlink;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

/**
 * We control the authentication process from this action bean, so that in the
 * event of a failed authentication we can add an appropriate FacesMessage to
 * the response.
 *
 * @author Shane Bryzak
 *
 */
@Stateless
@Named
public class LoginController {

    @Inject
    private Identity identity;

    @Inject
    private FacesContext facesContext;
    @Inject
    private UserService service;
    private User user;
    private String password;

    public void login() {
        AuthenticationResult result = identity.login();
        if (AuthenticationResult.FAILED.equals(result)) {
            facesContext.addMessage(
                    null,
                    new FacesMessage("Authentication was unsuccessful.  Please check your username and password "
                            + "before trying again."));
        }
    }

    public String createUser() {
        user = new User();
        service.addUser(getUser());
        service.updateCredential(user, new Password(password));
        user = null;
        password = "";
        addMsg("User successfully created.");
        return null;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public void addMsg(String msg) {
        FacesMessage fMsg = new FacesMessage(msg);
        fMsg.setSeverity(FacesMessage.SEVERITY_INFO);
        facesContext.addMessage(null, fMsg);
    }

}
