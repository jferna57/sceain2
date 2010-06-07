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
package es.in2.framework.demo.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import es.in2.framework.demo.core.transaction.ITransactionResource;

/**
 * Transaction resource implementation for JPA.
 * 
 * @author CETEC/CTJEE
 * @see ITransactionResource
 */
public class JPATransactionResource implements ITransactionResource {
	
	private static Logger log = Logger.getLogger(JPATransactionResource.class);
	
	private EntityManager entityManager;
	private String persistenceUnit;
	
	/**
	 * Class constructor.
	 * 
	 * @param persistenceUnit Persistence Unit Name.
	 * @param entityManager Entity Manager
	 */
	public JPATransactionResource(String persistenceUnit, EntityManager entityManager) {
		this.persistenceUnit = persistenceUnit;
		this.entityManager = entityManager;
	}

	/**
	 * Retrieves the entity manager associated with transaction resource.
	 * 
	 * @return an EntityManager.
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	/**
	 * Retrieves the persistence unit name associated with trasaction resource.
	 * 
	 * @return Persistence unit name.
	 */
	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public void commit() {
		log.debug("Committing JPA resource on persistence unit \"" + persistenceUnit + "\"");
		
		EntityTransaction tx = entityManager.getTransaction();
		if (tx.isActive()) {
			tx.commit();
		}
		
		entityManager.close();
	}

	public void rollback() {
		log.debug("Rolling back JPA resource on persistence unit=\"" + persistenceUnit + "\"");

		EntityTransaction tx = entityManager.getTransaction();
		if (tx.isActive()) {
			tx.rollback();
		}
		
		entityManager.close();
	}
	 
}