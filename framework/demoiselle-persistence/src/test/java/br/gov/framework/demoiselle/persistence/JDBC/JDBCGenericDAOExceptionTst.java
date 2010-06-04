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
package br.gov.framework.demoiselle.persistence.JDBC;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.persistence.hibernate.Professor;

/**
 * @author CETEC/CTJEE
 */
public class JDBCGenericDAOExceptionTst {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnectionNull() {

		try {
			JDBCMock mock = new JDBCMock();

			mock.configureConnectionNull();

			ContextLocator.getInstance().setTransactionContext(
					mock.getContextMock());

			JDBCUtil util = JDBCUtil.getInstance();

			util.beginTransaction(null);

			JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();

			dao.find("select * from name");
		} catch (Exception e) {
			Assert
					.assertEquals(
							e.getMessage(),
							"Error on open JDBC Connection! - Connection is null, plese verify the properties of connections.");
		}

	}
	
	@Test
	public void testConnectionSQLException() {

		try {
			JDBCMock mock = new JDBCMock();

			mock.configureSQLException();

			ContextLocator.getInstance().setTransactionContext(
					mock.getContextMock());

			JDBCUtil util = JDBCUtil.getInstance();

			util.beginTransaction(null);

			JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();

			dao.find("select * from name");
		} catch (Exception e) {
			Assert
					.assertEquals(
							e.getMessage(),
							"Any error on find jdbc dao.");
		}

	}

}
