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
package br.gov.framework.demoiselle.view.faces.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.layer.IViewController;
import br.gov.framework.demoiselle.core.message.IMessageContext;
import br.gov.framework.demoiselle.core.security.ISecurityContext;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;
import br.gov.framework.demoiselle.view.faces.util.ManagedBeanUtil;

/**
 * Abstract class that implements the view layer controller for JSF.
 * 
 * @author CETEC/CTJEE
 * @see IViewController
 */
public abstract class AbstractManagedBean implements IViewController {

	private static Logger log = Logger.getLogger(AbstractManagedBean.class);

	private AbstractManagedBeanConfig config = AbstractManagedBeanConfig.getInstance();

	protected ITransactionContext transactionContext = ContextLocator.getInstance().getTransactionContext();
	protected IMessageContext messageContext = ContextLocator.getInstance().getMessageContext();
	protected ISecurityContext securityContext = ContextLocator.getInstance().getSecurityContext();

	ServletContext context;

	/**
	 * Cleans up resources and invalidates the current user session.
	 */
	@SuppressWarnings("unchecked")
	public void logout() {
		
		log.debug("Cleaning up resources after logging out from the session");
		
		transactionContext = null;
		messageContext = null;
		
		HttpSession session = ManagedBeanUtil.getRequest().getSession();
		
		Enumeration en = session.getAttributeNames();
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			session.removeAttribute(name);
		}
		
		Map<String, Object> sessionMap = ManagedBeanUtil.getExternalContext().getSessionMap();
		if (!sessionMap.isEmpty()) {
			sessionMap.clear();
		}
		
		session.invalidate();
	}

	/**
	 * Sets the stored servlet context.
	 * 
	 * @param context	ServletContext
	 */
	void setContext(ServletContext context) {
		this.context = context;
	}

	/**
	 * Returns the maximum number of pages.
	 * 
	 * @return number of pages
	 */
	public Integer getMaxPages() {
		return config.getMaxPages();
	}

	/**
	 * Returns the number of records per page.
	 * 
	 * @return number of records
	 */
	public Integer getRows() {
		return config.getRows();
	}

}
