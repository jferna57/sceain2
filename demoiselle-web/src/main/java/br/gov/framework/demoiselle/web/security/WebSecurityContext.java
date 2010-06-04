/*
 * Demoiselle Framework
 * Copyright (c) 2009 Serpro and other contributors as indicated
 * by the @author tag. See the copyright.txt in the distribution for a
 * full listing of contributors.
 *
 * Demoiselle Framework is an open source Java EE library designed to accelerate
 * the development of transactional database Web applications.
 *
 * Demoiselle Framework is released under the terms of the LGPL license 3
 * http://www.gnu.org/licenses/lgpl.html  LGPL License 3
 *
 * This file is part of Demoiselle Framework.
 *
 * Demoiselle Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License 3 as published by
 * the Free Software Foundation.
 *
 * Demoiselle Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Demoiselle Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.gov.framework.demoiselle.web.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.security.ISecurityContext;

/**
 * Implementation of a Web Security Context.
 *
 * @author CETEC/CTJEE
 * @see ISecurityContext
 */
public final class WebSecurityContext implements ISecurityContext {

    /**
     * Log for WebSecurityContext class.
     */
    private static Logger log = Logger.getLogger(WebSecurityContext.class);

    /**
     * Unique instance of WebSecurity Context (singleton pattern).
     */
    private static final WebSecurityContext INSTANCE = new WebSecurityContext();

    /**
     * ThreadLocal for encapsulate the HttpServletRequest.
     */
    private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();

    /**
     * Private constructor. Add this implementation of ISecurityContext into the ContextLocator.
     */
    private WebSecurityContext() {
        ContextLocator.getInstance().setSecurityContext(this);
        log.debug("Security context created");
    }

    /**
     * Set the HttpServletRequest into the trheadlocal request.
     * 
     * @param request 
     */
    public void setRequest(final HttpServletRequest request) {
        log.debug("Adding HTTP request into security context");
        this.request.set(request);
    }

    /**
     * Returns the user principal from the request. 
     * 
     * @return Principal
     * @throws WebSecurityException If there's no request.
     */
    public Principal getUserPrincipal() {
        if (request.get() == null) {
            throw new WebSecurityException("Security context not initialized");
        }
        return request.get().getUserPrincipal();
    }

    /**
     * This method checks whether the given role is part of the set of role of the user.
     * 
     * @param role      name of un specific role
     * @return boolean 
     */
    public boolean isUserInRole(final String role) {
        if (request.get() == null) {
            throw new WebSecurityException("Security context not initialized");
        }
        return request.get().isUserInRole(role);
    }

    /**
     * This method removes the HttpServletRequest from ThreadLocal.
     */
    public void clear() {
        this.request.set(null);
        log.debug("Cleanin up security context");
    }

    /**
     * Returns the instance of WebSecurityContext in a singleton manner.
     * 
     * @return WebSecurityContext instance
     */
    public static WebSecurityContext getInstance() {
        return INSTANCE;
    }

}
