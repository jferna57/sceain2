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
package br.gov.framework.demoiselle.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;

/**
 * A class based on Proxy design pattern intended to capture calls made
 * to an EntityManager. The latter will be encapsulated into the proxy instance.
 * 
 * @author CETEC/CTJEE
 * @see EntityManager
 */
public class EntityManagerProxy implements EntityManager {
	
	private static Logger log = Logger.getLogger(EntityManagerProxy.class);
	
	private EntityManagerFactory emf;
	private String persistenceUnit;
	
	/**
	 * Class constructor.
	 * 
	 * @param emf Entity Manager Factory
	 * @param persistenceUnit Persistence Unit Name
	 */
	public EntityManagerProxy(EntityManagerFactory emf, String persistenceUnit) {
		this.emf = emf;
		this.persistenceUnit = persistenceUnit;
	}

	/**
	 * Retrieves the instance of EntityManager assigned to the current transaction context.
	 * If still not allocated, an instance will be asked to the respective EntityManagerFactory.
	 * 
	 * @return	an EntityManager
	 */
	private EntityManager getEntityManager() {
		log.debug("Retrieving an entity manager for persistence unit " + persistenceUnit);
		
		ITransactionContext ctx = ContextLocator.getInstance().getTransactionContext();

		JPATransactionResource res;
		if (ctx.hasResource()) {
			if (ctx.getResource() instanceof JPATransactionResource) {
				res = ((JPATransactionResource) ctx.getResource());
				String activePersistenceUnit = res.getPersistenceUnit();
				if (!persistenceUnit.equals(activePersistenceUnit)) {
					throw new PersistenceException(
							"Fail on creating transaction resource for persistence unit ["
									+ persistenceUnit
									+ "], there is already another for ["
									+ activePersistenceUnit + "]");
				}
			} else {
				throw new PersistenceException(
						"It is necessary that the TransactionContext has a "
								+ JPATransactionResource.class.getName()
								+ ", but was found a "
								+ ctx.getResource().getClass().getName());
			}
		} else {
			res = new JPATransactionResource(persistenceUnit, emf.createEntityManager());
			ctx.setResource(res);
		}
		
		return res.getEntityManager();
	}

	/**
	 * Opens an user transaction if still not open.
	 */
	protected void startTransaction() {
		log.debug("Starting transaction");
		
		EntityTransaction tx = getEntityManager().getTransaction();
		if (!tx.isActive())
			tx.begin();
	}
	
	public void clear() {
		getEntityManager().clear();
	}

	public void close() {
		getEntityManager().close();
	}

	public boolean contains(Object entity) {
		return getEntityManager().contains(entity);
	}

	public Query createNamedQuery(String name) {
		return getEntityManager().createNamedQuery(name);
	}

	public Query createNativeQuery(String sqlString) {
		return getEntityManager().createNativeQuery(sqlString);
	}

	@SuppressWarnings("unchecked")
	public Query createNativeQuery(String sqlString, Class resultClass) {
		return getEntityManager().createNativeQuery(sqlString, resultClass);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return getEntityManager().createNativeQuery(sqlString, resultSetMapping);
	}

	public Query createQuery(String qlString) {
		return getEntityManager().createQuery(qlString);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return getEntityManager().find(entityClass, primaryKey);
	}

	public void flush() {
		getEntityManager().flush();
	}

	public Object getDelegate() {
		return getEntityManager().getDelegate();
	}

	public FlushModeType getFlushMode() {
		return getEntityManager().getFlushMode();
	}

	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return getEntityManager().getReference(entityClass, primaryKey);
	}

	public EntityTransaction getTransaction() {
		return getEntityManager().getTransaction();
	}

	public boolean isOpen() {
		return getEntityManager().isOpen();
	}

	public void joinTransaction() {
		getEntityManager().joinTransaction();
	}

	public void lock(Object entity, LockModeType lockMode) {
		getEntityManager().lock(entity, lockMode);
	}

	public <T> T merge(T entity) {
		startTransaction();
		return getEntityManager().merge(entity);
	}

	public void persist(Object entity) {
		startTransaction();
		getEntityManager().persist(entity);
	}

	public void refresh(Object entity) {
		getEntityManager().refresh(entity);
	}

	public void remove(Object entity) {
		startTransaction();
		getEntityManager().remove(entity);
	}

	public void setFlushMode(FlushModeType flushMode) {
		getEntityManager().setFlushMode(flushMode);
	}
	
}
