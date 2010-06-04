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
package br.gov.framework.demoiselle.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.gov.framework.demoiselle.core.transaction.ITransactionResource;

/**
 * <p>
 * Resource of framework transaction context.
 * See CORE Logical module for more details.
 * </p>
 * <p>  
 * HibernateUtil start the session with repository and put the 
 * resource on one ITransactionContext from ContextLocator object.
 * </p>
 * 
 * @author CETEC/CTJEE
 * @see ITransactionResource
 */
public class HibernateTransactionResource implements ITransactionResource {
	
	private Session session = null;
	
	private static Logger log = Logger.getLogger(HibernateTransactionResource.class);
	
	/**
	 * Commit hibernate session and close the connection with repository 
	 *@see br.gov.framework.demoiselle.core.transaction.ITransactionResource#commit()
	 */
	public void commit() {
		log.debug("Hibernate Transaction Resource - Commit.");
		HibernateUtil.getInstance().commit();
	}
	
	/**
	 * Rollback all modify on persist objects
	 *   
	 *@see br.gov.framework.demoiselle.core.transaction.ITransactionResource#rollback()
	 */
	public void rollback() {
		log.debug("Hibernate Transaction Resource - Rollback.");
		HibernateUtil.getInstance().rollback();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public HibernateTransactionResource(Session session) {
		this.session = session;
	}
	 
}