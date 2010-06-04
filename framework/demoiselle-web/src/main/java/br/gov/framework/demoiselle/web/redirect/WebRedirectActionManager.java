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
package br.gov.framework.demoiselle.web.redirect;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.action.IActionManager;
import br.gov.framework.demoiselle.core.action.ILoaderAction;

/**
 * Class that Web Redirect Servlets delegate the action execution
 * 
 * @author CETEC/CTJEE 
 */
public class WebRedirectActionManager implements IActionManager<IRedirectAction> {
	
	private static final Logger log = Logger.getLogger(WebRedirectServlet.class);

	private ILoaderAction<IRedirectAction> loader;

	private ServletRequest request;
	private ServletResponse response;

	public WebRedirectActionManager(HttpServletRequest req, HttpServletResponse resp) {
		this.request = req;
		this.response = resp;
	}
	
	public void setLoader(ILoaderAction<IRedirectAction> loader) {
		this.loader = loader;
	}	
	
	public void execute() {
		Collection<IRedirectAction> actions =  loader.getActions();
		for (IRedirectAction action : actions) {
			String parameterValue = request.getParameter(action.getParameter());
			if (parameterValue != null && !parameterValue.trim().equals("") && parameterValue.trim().equals(action.getValue())) {
				action.setRequest(request);
				action.setResponse(response);
				action.execute();
				if (action != null)
					log.debug("Action execute OK!");
			}
		}
	}

}
