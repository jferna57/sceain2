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

import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.core.context.ContextLocator;
import es.in2.framework.demo.persistence.hibernate.Professor;

/**
 * @author CETEC/CTJEE
 */
public class JDBCGenericDAOTst {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindString() throws SQLException {

		JDBCMock mock = new JDBCMock();

		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCUtil util = JDBCUtil.getInstance();

		util.beginTransaction(mock.getConnectionMock());

		JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();
		dao.find("select  * from student");

	}

	@Test
	public void testFindStringClassOfA() {
		JDBCMock mock = new JDBCMock();

		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCUtil util = JDBCUtil.getInstance();

		util.beginTransaction(mock.getConnectionMock());

		JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();

		dao.find("from aluno ", Professor.class);
	}

	@Test
	public void testExecute() {
		JDBCMock mock = new JDBCMock();

		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCUtil util = JDBCUtil.getInstance();

		util.beginTransaction(mock.getConnectionMock());

		JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();

		int atualizacoes = dao.execute("update student set id=1,name='test' ");

		Assert.assertEquals(1, atualizacoes);
	}

	@Test
	public void testGetColumnNamesFromSQL() {
		JDBCMock mock = new JDBCMock();

		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();

		String[] columns = dao
				.getColumnNamesFromSQL(" select name,id from student ");

		Assert.assertEquals("name", columns[0]);
		Assert.assertEquals("id", columns[1]);

		Assert.assertTrue(columns.length > 0);
	}

	@Test
	public void testGetTableName() {
		JDBCMock mock = new JDBCMock();

		mock.configure();

		ContextLocator.getInstance().setTransactionContext(
				mock.getContextMock());

		JDBCUtil util = JDBCUtil.getInstance();

		util.beginTransaction(mock.getConnectionMock());

		JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();

		String name = dao.getTableName(" select name,id from student ");

		Assert.assertEquals("student", name);
	}

}
