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
package br.gov.framework.demoiselle.web.transaction;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.web.init.IInitializationAction;

/**
 * Initialization web application action.
 * This action sets existing transactional context through 
 * configuration file of Demoiselle.
 * If is defined type JTA, transactional context will work with 
 * a JTA implementation of application server, searching for a JNDI name defined
 * by a class that implements {@link IJNDITransactionManagerLookup}.
 * If transactional control is LOCAL, nothing is set in this action, because framework
 * works with default values.
 *  
 * @author CETEC/CTJEE
 * @see IInitializationAction
 */
public class WebTransactionAction implements IInitializationAction {
	
	private static Logger log = Logger.getLogger(WebTransactionAction.class);
 
	public void execute() {
		log.debug("executing");
		
		WebTransactionActionConfig config = ConfigurationLoader.load(WebTransactionActionConfig.class);
		
		WebTransactionContext ctx = WebTransactionContext.getInstance();
		
		if (config.getType() != null) {
			ctx.setType(config.getType());
		}
		
		if (config.getManagerLookupClass() != null) {
			try {
				Class<?> managerClass = Class.forName(config.getManagerLookupClass());
				IJNDITransactionManagerLookup manager = (IJNDITransactionManagerLookup) managerClass.newInstance();
				ctx.setJNDITransactionManagerLookup(manager);
			} catch (Exception e) {
				throw new WebTransactionException("Error: Could not set JNDI Transaction Manager Lookup", e);
			}
		}
	}

	public void setServletContext(ServletContext context) {
	}
	 
}
 
