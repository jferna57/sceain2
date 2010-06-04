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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.easymock.EasyMock;
import org.junit.Assert;

import br.gov.framework.demoiselle.core.transaction.ITransactionContext;

/**
 * @author CETEC/CTJEE
 */
public class JDBCMock {

	ResultSetMetaData resultSetMetaDataMock;
	ResultSet resultSetMock;
	Connection connectionMock;
	JDBCTransactionResource resourceMock;
	ITransactionContext contextMock;
	Statement statementMock;

	public void createResultSetMetaDataMock() {

		try {
			resultSetMetaDataMock = EasyMock
					.createMock(ResultSetMetaData.class);

			EasyMock.expect(this.resultSetMetaDataMock.getColumnCount())
					.andReturn(1).anyTimes();

			EasyMock
					.expect(
							this.resultSetMetaDataMock.getColumnName(EasyMock
									.anyInt())).andReturn("ColumnName")
					.anyTimes();

			EasyMock.replay(this.resultSetMetaDataMock);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

	public void createResultSetMock() {
		try {
			EasyMock.expect(resultSetMock.next()).andReturn(true).times(10);
			EasyMock.expect(resultSetMock.next()).andReturn(false);

			this.createResultSetMetaDataMock();

			EasyMock.expect(this.resultSetMock.getMetaData()).andReturn(
					resultSetMetaDataMock).anyTimes();

			EasyMock.expect(resultSetMock.getObject(EasyMock.anyInt()))
					.andReturn(new Object()).anyTimes();

			EasyMock.replay(resultSetMock);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public void createStatementMock() {
		try {
			this.statementMock = EasyMock.createMock(java.sql.Statement.class);

			EasyMock.expect(
					statementMock.executeQuery(EasyMock.isA(String.class)))
					.andReturn(resultSetMock).anyTimes();

			EasyMock.expect(
					statementMock.executeUpdate(EasyMock.isA(String.class)))
					.andReturn(1).anyTimes();

			EasyMock.replay(statementMock);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public void createStatementSQLExceptionMock() {
		try {
			this.statementMock = EasyMock.createMock(java.sql.Statement.class);

			EasyMock.expect(
					statementMock.executeQuery(EasyMock.isA(String.class)))
					.andThrow(new SQLException()).anyTimes();

			EasyMock.expect(
					statementMock.executeUpdate(EasyMock.isA(String.class)))
					.andReturn(1).anyTimes();

			EasyMock.replay(statementMock);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public void createConnectionSQLException() {

		try {
			connectionMock = EasyMock.createNiceMock(Connection.class);

			EasyMock.expect(connectionMock.createStatement()).andThrow(
					new SQLException()).anyTimes();

			connectionMock.commit();

			connectionMock.setAutoCommit(EasyMock.anyBoolean());

			EasyMock.replay(connectionMock);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public void createConnectionMock() {

		try {
			connectionMock = EasyMock.createNiceMock(Connection.class);

			EasyMock.expect(connectionMock.createStatement()).andReturn(
					statementMock).anyTimes();

			connectionMock.commit();

			connectionMock.setAutoCommit(EasyMock.anyBoolean());

			EasyMock.replay(connectionMock);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public void configure() {
		contextMock = EasyMock.createNiceMock(ITransactionContext.class);

		EasyMock.expect(contextMock.hasResource()).andReturn(true).anyTimes();

		resourceMock = org.easymock.classextension.EasyMock
				.createMock(JDBCTransactionResource.class);

		resultSetMock = EasyMock.createMock(ResultSet.class);

		this.createResultSetMock();

		this.createStatementMock();

		this.createConnectionMock();

		EasyMock.expect(resourceMock.getConnection()).andReturn(connectionMock)
				.anyTimes();

		org.easymock.classextension.EasyMock.replay(resourceMock);

		EasyMock.expect(contextMock.getResource()).andReturn(resourceMock)
				.anyTimes();

		contextMock.setResource(resourceMock);

		EasyMock.replay(contextMock);

	}
	
	public void configureContextHasResourceFalse() {
		contextMock = EasyMock.createNiceMock(ITransactionContext.class);

		EasyMock.expect(contextMock.hasResource()).andReturn(false).anyTimes();

		resourceMock = org.easymock.classextension.EasyMock
				.createMock(JDBCTransactionResource.class);

		resultSetMock = EasyMock.createMock(ResultSet.class);

		this.createResultSetMock();

		this.createStatementSQLExceptionMock();

		this.createConnectionMock();

		EasyMock.expect(resourceMock.getConnection()).andReturn(connectionMock)
				.anyTimes();

		org.easymock.classextension.EasyMock.replay(resourceMock);

		EasyMock.expect(contextMock.getResource()).andReturn(resourceMock)
				.anyTimes();

		contextMock.setResource(resourceMock);

		EasyMock.replay(contextMock);

	}

	public void configureSQLException() {
		contextMock = EasyMock.createNiceMock(ITransactionContext.class);

		EasyMock.expect(contextMock.hasResource()).andReturn(true).anyTimes();

		resourceMock = org.easymock.classextension.EasyMock
				.createMock(JDBCTransactionResource.class);

		resultSetMock = EasyMock.createMock(ResultSet.class);

		this.createResultSetMock();

		this.createStatementSQLExceptionMock();

		this.createConnectionMock();

		EasyMock.expect(resourceMock.getConnection()).andReturn(connectionMock)
				.anyTimes();

		org.easymock.classextension.EasyMock.replay(resourceMock);

		EasyMock.expect(contextMock.getResource()).andReturn(resourceMock)
				.anyTimes();

		contextMock.setResource(resourceMock);

		EasyMock.replay(contextMock);

	}
	
	public void configureConnectionSQLException() {
		contextMock = EasyMock.createNiceMock(ITransactionContext.class);

		EasyMock.expect(contextMock.hasResource()).andReturn(true).anyTimes();

		resourceMock = org.easymock.classextension.EasyMock
				.createMock(JDBCTransactionResource.class);

		resultSetMock = EasyMock.createMock(ResultSet.class);

		this.createResultSetMock();

		this.createStatementSQLExceptionMock();

		this.createConnectionSQLException();

		EasyMock.expect(resourceMock.getConnection()).andReturn(connectionMock)
				.anyTimes();

		org.easymock.classextension.EasyMock.replay(resourceMock);

		EasyMock.expect(contextMock.getResource()).andReturn(resourceMock)
				.anyTimes();

		contextMock.setResource(resourceMock);

		EasyMock.replay(contextMock);

	}

	public void configureConnectionNull() {
		contextMock = EasyMock.createNiceMock(ITransactionContext.class);

		EasyMock.expect(contextMock.hasResource()).andReturn(true).anyTimes();

		resourceMock = org.easymock.classextension.EasyMock
				.createMock(JDBCTransactionResource.class);

		resultSetMock = EasyMock.createMock(ResultSet.class);

		this.createResultSetMock();

		this.createStatementMock();

		connectionMock = null;

		EasyMock.expect(resourceMock.getConnection()).andReturn(connectionMock)
				.anyTimes();

		org.easymock.classextension.EasyMock.replay(resourceMock);

		EasyMock.expect(contextMock.getResource()).andReturn(resourceMock)
				.anyTimes();

		contextMock.setResource(resourceMock);

		EasyMock.replay(contextMock);

	}

	public ResultSetMetaData getResultSetMetaDataMock() {
		return resultSetMetaDataMock;
	}

	public void setResultSetMetaDataMock(ResultSetMetaData resultSetMetaDataMock) {
		this.resultSetMetaDataMock = resultSetMetaDataMock;
	}

	public ResultSet getResultSetMock() {
		return resultSetMock;
	}

	public void setResultSetMock(ResultSet resultSetMock) {
		this.resultSetMock = resultSetMock;
	}

	public Connection getConnectionMock() {
		return connectionMock;
	}

	public void setConnectionMock(Connection connectionMock) {
		this.connectionMock = connectionMock;
	}

	public JDBCTransactionResource getResourceMock() {
		return resourceMock;
	}

	public void setResourceMock(JDBCTransactionResource resourceMock) {
		this.resourceMock = resourceMock;
	}

	public ITransactionContext getContextMock() {
		return contextMock;
	}

	public void setContextMock(ITransactionContext contextMock) {
		this.contextMock = contextMock;
	}

}
