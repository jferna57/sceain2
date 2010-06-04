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
import javax.persistence.EntityTransaction;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author CETEC/CTJEE
 */
public class JPATransactionResourceTest {

	private static final String PERSISTENCE_UNIT_NAME = "pu";

	private JPATransactionResource resource;
	
	private EntityManager em;
	private EntityTransaction tx;
	
	@Before
	public void setUp() throws Exception {
		
		em = EasyMock.createMock(EntityManager.class);
		tx = EasyMock.createMock(EntityTransaction.class);
		
		resource = new JPATransactionResource(PERSISTENCE_UNIT_NAME, em);
	}

	@After
	public void tearDown() throws Exception {
		em = null;
		tx = null;
		resource = null;
	}

	@Test
	public void testGetEntityManager() {
		Assert.assertEquals(em, resource.getEntityManager());
	}

	@Test
	public void testGetPersistenceUnit() {
		Assert.assertEquals(PERSISTENCE_UNIT_NAME, resource.getPersistenceUnit());
	}

	@Test
	public void testCommitTransactionInactive() {
		
		// if no transaction is active, nothing must be done
		EasyMock.expect(tx.isActive()).andReturn(false);
		EasyMock.replay(tx);
		
		// entity manager must be closed at the end
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		em.close();
		EasyMock.replay(em);
		
		resource.commit();
	}

	@Test
	public void testCommitTransactionActive() {
		
		// if a transaction is active, it must be committed
		tx.begin();
		EasyMock.expect(tx.isActive()).andReturn(true);
		tx.commit();
		EasyMock.replay(tx);
		
		// entity manager must be closed at the end
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		em.close();
		EasyMock.replay(em);
		
		resource.commit();
	}

	@Test
	public void testRollbackTransactionInactive() {
		
		// if no transaction is active, nothing must be done
		EasyMock.expect(tx.isActive()).andReturn(false);
		EasyMock.replay(tx);
		
		// entity manager must be closed at the end
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		em.close();
		EasyMock.replay(em);
		
		resource.rollback();
	}

	@Test
	public void testRollbackTransactionActive() {
		
		// if a transaction is active, it must be rolled back
		tx.begin();
		EasyMock.expect(tx.isActive()).andReturn(true);
		tx.rollback();
		EasyMock.replay(tx);
		
		// entity manager must be closed at the end
		EasyMock.expect(em.getTransaction()).andReturn(tx);
		em.close();
		EasyMock.replay(em);
		
		resource.rollback();
	}

}
