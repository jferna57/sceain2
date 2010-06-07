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
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import es.in2.framework.demo.core.context.ContextLocator;
import es.in2.framework.demo.core.transaction.ITransactionContext;
import es.in2.framework.demo.core.transaction.ITransactionResource;
import es.in2.framework.demo.util.config.ConfigurationException;
import es.in2.framework.demo.util.config.ConfigurationLoader;

/**
 * Utility class used to JDBC configurations
 * and also responsible to insert a connection into
 * Transactional Control defined in Logical Module CORE.
 * @author CETEC/CTJEE
 * @see Logger
 * @see PersistenceJDBCException
 * @see DataSource
 *
 */
public class JDBCUtil {

	private String driver = null;
	private String url = null;
	private String user = null;
	private String pass = null;
	private String urlJNDI = null;
	
	private Logger log = Logger.getLogger(JDBCUtil.class);
	
	private JDBCUtil() {
		fillConfiguration();
	}
	
	protected JDBCUtil clone() throws PersistenceJDBCException {
		throw new PersistenceJDBCException("JDBCUtil not cloneable.");
	}

	private static JDBCUtil instance = new JDBCUtil();
	
	public static JDBCUtil getInstance() {
		return instance;
	}
	
	public void setDataSourceJNDI(String url) {
		urlJNDI = url;
	}

	/**
	 * Sets the attributes of JDBCUtil with values loaded from
	 * {@ JDBCConfig}.
	 */
	private void fillConfiguration() {
		JDBCConfig configuration = null;
		try {
			configuration = ConfigurationLoader.load(JDBCConfig.class);
		} catch (ConfigurationException ce) {
			log.debug("No fill configuration jdbc from file properties.");
		}
		if (configuration != null && configuration.getProperties() != null) {
			
			if (isEmpty(this.driver))
				this.driver = configuration.getProperties().getProperty("driver");
			
			if (isEmpty(this.pass))
				this.pass = configuration.getProperties().getProperty("pass");
			
			if (isEmpty(this.url))
				this.url = configuration.getProperties().getProperty("url");
			
			if (isEmpty(this.user))
				this.user = configuration.getProperties().getProperty("user");
		}
	}
	
	/**
	 * A string is null if its value is equal to null or its length is equal to zero.
	 * @param string
	 * @return
	 */
	private boolean isEmpty(String string) {
		return string==null||string.trim().length()==0;
	}
	
	
	/**
	 * If attribute urlJNDI is null, open the connection by URL.
	 * Otherwise, gets the connection through JDNI.  
	 * @return a object representing the connection
	 */
	public Connection openConnection() {
		log.debug("Invoke open connection on jdbc util.");
		Connection result = null;
		if (urlJNDI != null) {
			result = openConnectionJNDI();
		} else {
			result = openConnectionURL();
		}
		
		if (result != null)
			beginTransaction(result);
		else
			throw new PersistenceJDBCException("Error on open JDBC Connection! - Connection is null, plese verify the properties of connections.");
		
		return result;
	}
	
	/**
	 * 
	 * @return connection object
	 */
	private Connection openConnectionJNDI() {
		Connection connection = null;
		if (urlJNDI != null) {
			DataSource ds = getDataSourceFromJNDI(urlJNDI);
			try {
				connection = ds.getConnection();
			} catch (SQLException e) { throw new PersistenceJDBCException("JNDI invalid. URL=" + url, e); }
		}
		
		putConnectionContext(connection);
		
		return connection;
	}
	
	/**
	 * Sets the connection as a resource of transaction context.
	 * @param connection
	 */
	private void putConnectionContext(Connection connection) {
		if (connection != null) {
			JDBCTransactionResource resource = new JDBCTransactionResource(connection);
			ITransactionContext transactionContext = ContextLocator.getInstance().getTransactionContext();
			if (transactionContext != null) {
				
				if (transactionContext.hasResource())
					throw new PersistenceJDBCException("Transaction context contain a invalid resource.");
				
				transactionContext.setResource(resource);
			}
		} else {
			log.debug("Connection is null...");
		}
	}
	
	/**
	 * 
	 * @return connection object
	 */
	private Connection openConnectionURL() {
		Connection connection = null;

		if (connection == null && url != null) {
			try {
				Class.forName(driver);
				if (user != null && user.length()>0 && pass != null && pass.length()>0)
					connection = DriverManager.getConnection(url, user, pass);
				else
					connection = DriverManager.getConnection(url);
			} catch(SQLException ex) {
				throw new PersistenceJDBCException("Error on open JDBC connection.", ex);
			} catch(ClassNotFoundException ex) {
				throw new PersistenceJDBCException("Without class driver.", ex);
			} 
		}
		
		putConnectionContext(connection);
		
		return connection;
	}

	/**
	 * Retrieves the connection from transaction context. 
	 * @return
	 */
	private Connection getConnectionFromTransactionContext() {
		ITransactionResource resource = null;
		JDBCTransactionResource jdbcResource = null;
		
		Connection result = null;
		
		ITransactionContext transactionContext = ContextLocator.getInstance().getTransactionContext();

		if (transactionContext == null)
			throw new PersistenceJDBCException("Without Transaction Context.");

		if (transactionContext.hasResource()) {
			resource = transactionContext.getResource();
			
			if (resource instanceof JDBCTransactionResource) {
				jdbcResource = (JDBCTransactionResource)resource;
				
				result = jdbcResource.getConnection();
			} else {
				throw new PersistenceJDBCException("Transaction context NOT contain a JDBCTransactionResource resource.");
			}
		}
		
		return result;
	}
	
	public Connection getConnection() {
		Connection result = getConnectionFromTransactionContext();
		
		if (result == null) {
			result = openConnection();
		}

		return result;
	}
	
	public void closeConnection() {
		log.debug("Invoke close connection on jdbc util.");
		Connection connection = getConnection();
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceJDBCException("Error on JDBC connection close.", e);
			} finally {
				connection = null;
			}
		}
	}
	
	public void beginTransaction(Connection connection) throws PersistenceJDBCException {
		log.debug("Start transaction on jdbc util.");
		if (connection != null) {
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				throw new PersistenceJDBCException("Error on JDBC beginTransaction.", e);
			}
		}
	}
	
	public void commitTransaction() throws PersistenceJDBCException {
		log.debug("Commit transaction on jdbc util.");
		Connection connection = getConnection();
		if (connection != null) {
			try {
				connection.commit();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new PersistenceJDBCException("Error on JDBC commit transaction.", e);
			} finally {
				closeConnection();
			}
		}
	}

	public void rollbackTransaction() throws PersistenceJDBCException {
		log.debug("Rollback transaction on jdbc util.");
		Connection connection = getConnection();
		if (connection != null) {
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new PersistenceJDBCException("Error on JDBC rollback", e);
			} finally {
				closeConnection();
			}
		}
	}

	public String getDriver() { return driver; }
	public void setDriver(String driver) { this.driver = driver; }
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
	public void setUser(String user) { this.user = user; }
	public void setPass(String pass) { this.pass = pass; }

	private DataSource getDataSourceFromJNDI(String jndiName) throws PersistenceJDBCException {
		log.debug("Get datasource from jndi tree: " + jndiName);
		DataSource result = null;
		Object obj = null;
		try {
			InitialContext initialContext = new InitialContext();
			if (System.getProperty(Context.INITIAL_CONTEXT_FACTORY, "").startsWith("org.jnp")) {
				obj = initialContext.lookup("java:/" + jndiName);
			} else {
				obj = initialContext.lookup("java:comp/env/" + jndiName);
			}
			
			if (obj instanceof DataSource)
				result = (DataSource)obj;
			
		} catch (Throwable throwable) {
			result = null;
		}
		return result;
	}
	
}
 
