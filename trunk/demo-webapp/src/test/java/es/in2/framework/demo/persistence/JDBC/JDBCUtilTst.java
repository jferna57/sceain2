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
package es.in2.framework.demo.persistence.JDBC;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.core.context.ContextLocator;

/**
 * @author CETEC/CTJEE
 */
public class JDBCUtilTst {

	static String jndiFactory = "";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClone() {
		try {
			JDBCUtil util = JDBCUtil.getInstance();
			util.clone();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof PersistenceJDBCException);
		}
	}

	@Test
	public void testGetInstance() {
		JDBCUtil util = JDBCUtil.getInstance();
		Assert.assertNotNull(util);
	}

	@Test
	public void testSetDataSourceJNDI() {
		JDBCUtil util = JDBCUtil.getInstance();
		util.setDataSourceJNDI("jndi://");
	}

	@Test
	public void testBeginTransaction() {
		JDBCMock mock = new JDBCMock();
		mock.configure();
		JDBCUtil.getInstance().beginTransaction(mock.getConnectionMock());
	}

	@Test
	public void testCommitTransaction() {
		JDBCMock mock = new JDBCMock();
		mock.configure();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		JDBCUtil.getInstance().commitTransaction();
	}

	@Test
	public void testRollbackTransaction() {
		JDBCMock mock = new JDBCMock();
		mock.configure();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		JDBCUtil.getInstance().rollbackTransaction();
	}

	@Test
	public void testGetDriver() {
		JDBCMock mock = new JDBCMock();
		mock.configure();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		JDBCUtil.getInstance().setDriver("driver");
		Assert.assertEquals("driver", JDBCUtil.getInstance().getDriver());
	}

	@Test
	public void testGetUrl() {
		JDBCMock mock = new JDBCMock();
		mock.configure();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		JDBCUtil.getInstance().setUrl("url");
		Assert.assertEquals("url", JDBCUtil.getInstance().getUrl());
	}

	@Test
	public void testSetUser() {

		JDBCMock mock = new JDBCMock();
		mock.configure();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		JDBCUtil.getInstance().setUser("user");

	}

	@Test
	public void testSetPass() {
		JDBCMock mock = new JDBCMock();
		mock.configure();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		JDBCUtil.getInstance().setPass("pass");

	}

	@Test
	public void testOpenConnection() {

		JDBCUtilTst.jndiFactory = System
				.getProperty("java.naming.factory.initial");

		System
				.setProperty("java.naming.factory.initial",
						"br.gov.framework.demoiselle.persistence.JDBC.MockInitialContextFactory");

		JDBCMock mock = new JDBCMock();
		mock.configureContextHasResourceFalse();
		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());
		try {

			InitialContext context = new InitialContext();
			context.bind("teste", new Object());
		} catch (NamingException e) {
			Assert.fail();
		}

		JDBCUtil.getInstance().openConnection();
		JDBCUtil.getInstance().closeConnection();

		if (JDBCUtilTst.getJndiFactory() != null) {

			System.setProperty("java.naming.factory.initial", JDBCUtilTst
					.getJndiFactory());
		}

	}

	public static String getJndiFactory() {
		return jndiFactory;
	}

	public static void setJndiFactory(String jndiFactory) {
		JDBCUtilTst.jndiFactory = jndiFactory;
	}

}
