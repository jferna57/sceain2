package es.in2.framework.demo.persistence.JDBC;

import java.sql.Connection;

import org.apache.log4j.Logger;

import es.in2.framework.demo.core.transaction.ITransactionResource;

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
 
