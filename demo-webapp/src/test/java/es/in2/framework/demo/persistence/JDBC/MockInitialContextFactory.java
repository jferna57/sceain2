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
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;

/**
 * @author CETEC/CTJEE
 */
public class MockInitialContextFactory implements InitialContextFactory {

	@SuppressWarnings("unchecked")
	public Context getInitialContext(Hashtable env) {
		InitialContext initial = EasyMock.createMock(InitialContext.class);
		try {

			Connection connection = EasyMock.createMock(Connection.class);
			try {
				connection.setAutoCommit(EasyMock.anyBoolean());
				connection.close();
			} catch (SQLException e1) {
				Assert.fail();
			}
			EasyMock.replay(connection);

			DataSource dataSource = EasyMock.createMock(DataSource.class);

			try {
				EasyMock.expect(dataSource.getConnection()).andReturn(
						connection);
			} catch (Exception e) {
				Assert.fail();
			}

			EasyMock.replay(dataSource);

			EasyMock.expect(initial.lookup(EasyMock.isA(String.class)))
					.andReturn(dataSource);
			initial
					.bind(EasyMock.isA(String.class), EasyMock
							.isA(Object.class));
		} catch (NamingException e) {
			Assert.fail(e.getMessage());
		}
		EasyMock.replay(initial);
		return initial;
	}

}
