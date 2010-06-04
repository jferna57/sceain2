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

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;

/**
 * @author CETEC/CTJEE
 */
public class HibernateTransactionResourceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCommit() {
		
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);
		ITransactionContext context = hibernate.getContextMock();

		HibernateTransactionResource resource = new HibernateTransactionResource(
				hibernate.getSessionMock());

		context.setResource(resource);

		ContextLocator.getInstance().setTransactionContext(context);

		resource.commit();
	}

	@Test
	public void testRollback() {
		
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		HibernateTransactionResource resource = new HibernateTransactionResource(
				hibernate.getSessionMock());

		ITransactionContext context = hibernate.getContextMock();
		context.setResource(resource);

		ContextLocator.getInstance().setTransactionContext(context);

		resource.rollback();
	}

	@Test
	public void testGetSession() {
		
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		HibernateTransactionResource resource = new HibernateTransactionResource(
				hibernate.getSessionMock());

		ITransactionContext context = hibernate.getContextMock();
		context.setResource(resource);

		ContextLocator.getInstance().setTransactionContext(context);

		Session session = resource.getSession();
		Assert.assertEquals(session, hibernate.getSessionMock());
	}

	@Test
	public void testSetSession() {
		
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);
		ITransactionContext context = hibernate.getContextMock();

		HibernateTransactionResource resource = new HibernateTransactionResource(
				hibernate.getSessionMock());
		context.setResource(resource);
		ContextLocator.getInstance().setTransactionContext(context);

		resource.setSession(hibernate.getSessionMock());
	}

}
