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

import static org.junit.Assert.assertNotNull;

import org.easymock.classextension.EasyMock;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;

/**
 * @author CETEC/CTJEE
 */
public class HibernateUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertNotNull(HibernateUtil.getInstance());
	}

	@Test
	public void testSetConfiguration() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);
		HibernateUtil.getInstance().setConfiguration(
				hibernate.configurationMock);
		Configuration configuration = HibernateUtil.getInstance()
				.getConfiguration();
		Assert.assertEquals(hibernate.getConfigurationMock(), configuration);

	}

	@Test
	public void testSetSessionFactory() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);
		HibernateUtil.getInstance().setSessionFactory(
				hibernate.getSessionFactoryMock());
		Assert.assertEquals(hibernate.getSessionFactoryMock(), HibernateUtil
				.getInstance().getSessionFactory());
	}

	@Test
	public void testConfigure() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		IPojo ipojo = EasyMock.createMock(IPojo.class);

		hibernate.createConfiguration("name", ipojo);
		HibernateUtil.getInstance().setConfiguration(
				hibernate.configurationMock);
		Configuration configuration = HibernateUtil.getInstance()
				.getConfiguration();
		Assert.assertEquals(hibernate.getConfigurationMock(), configuration);
		HibernateUtil.getInstance().configure();

	}

	@Test
	public void testGetSession() {

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		ITransactionContext context = hibernate.getContextMock();

		HibernateUtil.getInstance().setTransactionContext(context);

		Assert.assertNotNull(HibernateUtil.getInstance().getSession());
	}

	@Test
	public void testGetSessionBoolean() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		ITransactionContext context = hibernate.getContextMock();

		HibernateUtil.getInstance().setTransactionContext(context);

		Assert.assertNotNull(HibernateUtil.getInstance().getSession(true));
	}

	@Test
	public void testCommit() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		ITransactionContext context = hibernate.getContextMock();

		HibernateUtil.getInstance().setTransactionContext(context);

		HibernateUtil.getInstance().commit();
	}

	@Test
	public void testRollback() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		ITransactionContext context = hibernate.getContextMock();

		HibernateUtil.getInstance().setTransactionContext(context);

		HibernateUtil.getInstance().rollback();
	}

	@Test
	public void testClone() {
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		ITransactionContext context = hibernate.getContextMock();

		HibernateUtil.getInstance().setTransactionContext(context);
		try {

			HibernateUtil.getInstance().clone();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof PersistenceHibernateException);
		}
	}

}
