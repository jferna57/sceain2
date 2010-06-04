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

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.transaction.ITransactionResource;

/**
 * Represents a JDBC connection.
 * 
 * @author CETEC/CTJEE
 */
public class JDBCTransactionResource implements ITransactionResource {
	
	private static final Logger log = Logger.getLogger(JDBCTransactionResource.class);
	
	private Connection connection = null;
	
	public JDBCTransactionResource(Connection connection) {
		this.connection = connection;
	}
 
	/**
	 * Gets the object Connection 
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Tries to make effective the operations upon database
	 */
	public void commit() {
		JDBCUtil.getInstance().commitTransaction();
		log.debug("Commit invoked OK on JDBCTransactionResource");
	}
	 
	/**
	 * Reverts all the operations executed in a database by
	 * a transaction that have failed.
	 */
	public void rollback() {
		JDBCUtil.getInstance().rollbackTransaction();
		log.debug("Rollback invoked OK on JDBCTransactionResource");
	}
	 
}
 
