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
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.core.context.ContextLocator;
import es.in2.framework.demo.core.transaction.ITransactionContext;
import es.in2.framework.demo.core.transaction.ITransactionResource;

/**
 * @author CETEC/CTJEE
 */
public class EntityManagerProxyTest {

	private static final String PERSISTENCE_UNIT_NAME = "puTest";
	
	private EntityManagerProxy proxy;
	
	private EntityManager em;
	private EntityManagerFactory emf;
	private EntityTransaction tx;
	private Query query;
	
	private ITransactionContext context;
	private ITransactionResource resource;
	
	@Before
	public void setUp() throws Exception {
		em = EasyMock.createMock(EntityManager.class);
		emf = EasyMock.createMock(EntityManagerFactory.class);
		tx = EasyMock.createMock(EntityTransaction.class);
		context = EasyMock.createMock(ITransactionContext.class);
		query = EasyMock.createMock(Query.class);
	}

	@After
	public void tearDown() throws Exception {
		em = null;
		emf = null;
		tx = null;
		context = null;
		query = null;
		resource = null;
		proxy = null;
	}

	@Test
	public void testStartTransactionEntityManagerTransactionNotActive() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(tx.isActive()).andReturn(false);
		tx.begin();
		EasyMock.replay(tx);
		
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.startTransaction();
	}

	@Test
	public void testStartTransactionEntityManagerTransactionActive() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(tx.isActive()).andReturn(true);
		tx.begin();
		EasyMock.replay(tx);
		
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.startTransaction();
	}

	@Test
	public void testStartTransactionTransactionContextHasResource() {
		
		String persistenceUnit1 = "puTest";

		EasyMock.expect(tx.isActive()).andReturn(true);
		tx.begin();
		EasyMock.replay(tx);
		
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.replay(em);
		
		resource = new JPATransactionResource(persistenceUnit1, em);
		
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		String persistenceUnit2 = "puTest";

		proxy = new EntityManagerProxy(emf, persistenceUnit2);
		proxy.startTransaction();
	}

	@Test
	public void testClear() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		em.clear();
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.clear();
	}

	@Test
	public void testClose() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		em.close();
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.close();
	}

	@Test
	public void testContains() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(em.contains(EasyMock.anyObject())).andReturn(true);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertTrue(proxy.contains(new Object()));
	}

	@Test
	public void testCreateNamedQuery() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNamedQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.createNamedQuery("testeQueryName"));
	}

	@Test
	public void testCreateNativeQueryString() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNativeQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.createNativeQuery("testeQueryName"));
	}

	@Test
	public void testCreateNativeQueryStringClass() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNativeQuery((String) EasyMock.anyObject(), EasyMock.isA(Class.class))).andReturn(query);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.createNativeQuery("testeQueryName", Object.class));
	}

	@Test
	public void testCreateNativeQueryStringString() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNativeQuery((String) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(query);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.createNativeQuery("testeQueryName", "sql expression test"));
	}

	@Test
	public void testCreateQuery() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.createQuery("jpql expression test"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFind() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(em.find(EasyMock.isA(Class.class), EasyMock.anyObject())).andReturn(new Object());
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.find(Object.class, 1));
	}

	@Test
	public void testFlush() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		em.flush();
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.flush();
	}

	@Test
	public void testGetDelegate() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(em.getDelegate()).andReturn(new Object());
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.getDelegate());
	}

	@Test
	public void testGetFlushMode() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(em.getFlushMode()).andReturn(FlushModeType.COMMIT);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertEquals(FlushModeType.COMMIT, proxy.getFlushMode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetReference() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		EasyMock.expect(em.getReference(EasyMock.isA(Class.class), EasyMock.anyObject())).andReturn(new Object());
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(proxy.getReference(Object.class, 1));
	}

	@Test
	public void testGetTransaction() {

		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);

		EasyMock.replay(tx);

		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertNotNull(tx);
		Assert.assertEquals(tx, proxy.getTransaction());
	}

	@Test
	public void testIsOpen() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);

		EasyMock.replay(tx);

		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.expect(em.isOpen()).andReturn(true);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertTrue(proxy.isOpen());
	}

	@Test
	public void testJoinTransaction() {
		
		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);

		em.joinTransaction();
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.joinTransaction();
	}

	@Test
	public void testLock() {

		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);

		Object entity = new Object();
		LockModeType lockMode = LockModeType.WRITE;

		em.lock(entity, lockMode);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.lock(entity, lockMode);
	}

	@Test
	public void testMerge() {
		
		resource = new JPATransactionResource(PERSISTENCE_UNIT_NAME, em);
		
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		context.setResource(resource);
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		context.setResource(resource);
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		Object entity = new Object();

		EasyMock.expect(tx.isActive()).andReturn(false);
		tx.begin();
		EasyMock.replay(tx);
		
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.expect(em.merge(entity)).andReturn(entity);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		Assert.assertEquals(entity, proxy.merge(entity));
	}

	@Test
	public void testPersist() {
		
		resource = new JPATransactionResource(PERSISTENCE_UNIT_NAME, em);
		
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		context.setResource(resource);
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		context.setResource(resource);
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		Object entity = new Object();

		EasyMock.expect(tx.isActive()).andReturn(false);
		tx.begin();
		EasyMock.replay(tx);
		
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		em.persist(entity);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.persist(entity);
	}

	@Test
	public void testRefresh() {

		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		Object entity = new Object();
		
		em.refresh(entity);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.refresh(entity);
	}

	@Test
	public void testRemove() {
		
		resource = new JPATransactionResource(PERSISTENCE_UNIT_NAME, em);
		
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		context.setResource(resource);
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		context.setResource(resource);
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		Object entity = new Object();

		EasyMock.expect(tx.isActive()).andReturn(false);
		tx.begin();
		EasyMock.replay(tx);
		
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		em.remove(entity);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.remove(entity);
	}

	@Test
	public void testSetFlushMode() {

		EasyMock.expect(context.hasResource()).andReturn(false);
		context.setResource((ITransactionResource) EasyMock.anyObject());
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);
		
		em.setFlushMode(FlushModeType.AUTO);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		proxy.setFlushMode(FlushModeType.AUTO);
	}

	@Test
	public void testGetEntityManagerNotSamePU() {
		
		// let's try using a different persistence unit name
		resource = new JPATransactionResource(PERSISTENCE_UNIT_NAME + "Other", em);
		
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);

		EasyMock.replay(tx);

		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.expect(em.isOpen()).andReturn(true);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		try {
			proxy.isOpen();
			Assert.fail("Must not reach this line!");
		} catch (PersistenceException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testGetEntityManagerNotJPAResource() {
		
		// let's try using any kind of transaction resource implementation
		resource = new ITransactionResource() {
			public void commit() {}
			public void rollback() {}
		};
		
		EasyMock.expect(context.hasResource()).andReturn(true);
		EasyMock.expect(context.getResource()).andReturn(resource).times(2);
		EasyMock.replay(context);
		
		ContextLocator.getInstance().setTransactionContext(context);

		EasyMock.replay(tx);

		EasyMock.expect(em.getTransaction()).andReturn(tx);
		EasyMock.expect(em.isOpen()).andReturn(true);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andReturn(em);
		EasyMock.replay(emf);
		
		proxy = new EntityManagerProxy(emf, PERSISTENCE_UNIT_NAME);
		try {
			proxy.isOpen();
			Assert.fail("Must not reach this line!");
		} catch (PersistenceException e) {
			Assert.assertTrue(true);
		}
	}

}
