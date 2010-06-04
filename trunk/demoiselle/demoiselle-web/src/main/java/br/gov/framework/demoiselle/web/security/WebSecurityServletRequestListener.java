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

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * <p>
 * A ServletRequestListener that can be used to initialize SecurityContext in order to handle HTTP request events.
 * </p>
 * 
 * <p>
 * Settings on WEB-INF/web.xml descriptor file:
 * </p>  
 * <pre>
 *     &lt;listener&gt;
 *         &lt;listener-class&gt;br.gov.framework.web.security.WebSecurityServletListener&lt;/listener-class&gt;
 *     &lt;/listener&gt;
 * </pre>
 * 
 * @author CETEC/CTJEE
 */
public class WebSecurityServletRequestListener implements ServletRequestListener {
	
	private static Logger log = Logger.getLogger(WebSecurityServletRequestListener.class);

	/**
	 * When a request is initialized this method is called,
	 * placing this request, if a HttpServletRequest,
	 * into the WebSecurityContext.
	 */
	public void requestInitialized(ServletRequestEvent sre) {
		log.debug("Request initialized");
		
		ServletRequest request = sre.getServletRequest();
		if (request instanceof HttpServletRequest) {
			WebSecurityContext.getInstance().setRequest((HttpServletRequest)request);
		}
	}

	/**
	 * Removes the request from the security context.
	 */
	public void requestDestroyed(ServletRequestEvent sre) {
		WebSecurityContext.getInstance().clear();
		log.debug("Request destroyed");
	}

}
