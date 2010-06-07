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

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.core.context.ContextLocator;

/**
 * @author CETEC/CTJEE
 */
public class JDBCTransactionResourceTst {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConnection() {

		JDBCMock mock = new JDBCMock();
		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCTransactionResource resource = new JDBCTransactionResource(mock
				.getConnectionMock());

		Connection connection = resource.getConnection();

		Assert.assertNotNull(connection);
	}

	@Test
	public void testCommit() {
		JDBCMock mock = new JDBCMock();
		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCTransactionResource resource = new JDBCTransactionResource(mock
				.getConnectionMock());
		resource.commit();
	}

	@Test
	public void testRollback() {
		JDBCMock mock = new JDBCMock();
		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCTransactionResource resource = new JDBCTransactionResource(mock
				.getConnectionMock());
		resource.rollback();
	}

}
