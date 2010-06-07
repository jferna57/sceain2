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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import es.in2.framework.demo.core.bean.IPojo;
import es.in2.framework.demo.core.layer.IDAO;

/**
 * <p>
 * Implementations of methods defined by IDAO.
 * Applications using native JDBC solution 
 * will may inherit this class in order to reduce 
 * the amount of interface implementations IDAO.
 * </p>
 * <p>
 * This class uses BeanUtils for populating JavaBeans properties via reflection.
 * </p>
 * 
 * @author CETEC/CTJEE
 *
 * @param <A>
 * @see IDAO
 * @see IPojo 
 */
@SuppressWarnings("unchecked")
public abstract class JDBCGenericDAO<A extends IPojo> implements IDAO<A> {
	
	private static final Logger log = Logger.getLogger(JDBCGenericDAO.class);
 
	/**
	 * Execute a SQL query and return List of Map<field, value>.
	 * Each row return by query is a Map<field, value>.
	 * 
	 * @param sql
	 * @return List from sql
	 * @throws PersistenceJDBCException
	 */
	public final List find(String sql) throws PersistenceJDBCException {
		List coll = new ArrayList();
		try {
			if (JDBCUtil.getInstance().getConnection() != null) {
				Statement st = JDBCUtil.getInstance().getConnection().createStatement();
				String[] campos = getColumnNamesFromSQL(sql);
				ResultSet resultSet = st.executeQuery(sql);
				while (resultSet.next()) {
					int qtdColunas = resultSet.getMetaData().getColumnCount();
					Object[] objeto = new Object[qtdColunas];
					HashMap<String, Object> mapaResult = new HashMap<String, Object>();
					for (int i = 1; i <= qtdColunas; i++) {
						objeto[i-1] = resultSet.getObject(i);
						String columnName = resultSet.getMetaData().getColumnName(i)!=null?resultSet.getMetaData().getColumnName(i):campos[i-1];
						if (columnName == null) columnName = String.valueOf(i);
						mapaResult.put(columnName, resultSet.getObject(i));
					}
					coll.add(mapaResult);
				}
			} else {
				throw new PersistenceJDBCException("connection is null");
			}
		} catch (SQLException e) {
			throw new PersistenceJDBCException("Any error on find jdbc dao.", e);
		}
		log.debug("Find SQL on JDBCGenericDAO invoke OK!");
		return coll;
	}
	
	/**
	 * Return the list of objects of class
	 * 
	 * @param sql
	 * @param clazz
	 * @return List
	 * @throws PersistenceJDBCException
	 */
	public final List<A> find(String sql, Class<A> clazz) throws PersistenceJDBCException {
		List<A> result = new ArrayList<A>();
		List resultados = find(sql);
		for (Object obj : resultados) {
			try {
				A jdbcLoader = clazz.newInstance();
				buildObject((HashMap<String, Object>)obj, jdbcLoader);
				result.add(jdbcLoader);
			} catch (IllegalAccessException e) {
				throw new PersistenceJDBCException("Ilegal access exception on find jdbc dao.", e);
			} catch (InstantiationException e) {
				throw new PersistenceJDBCException("Instantiation exception on find jdbc dao.", e);
			}
		}
		log.debug("Find SQL and result a list of objects on JDBCGenericDAO invoke OK!");
		return result;
	}
	
	/**
	 * <p>
	 * Sets several properties of a bean, defined in a HashMap
	 * through method <b>setProperty</b> of BeanUtils.
	 * </p>
	 * <p>
	 * <b>setProperty</b> sets the specified property value,
	 * performing type conversions as required
	 * to conform to the type of the destination property.
	 * Its parameters are:
	 * <ul>
	 * <li>bean - Bean on which setting is to be performed
	 * <li>name - Property name (can be nested/indexed/mapped/combo)
	 * <li>value - Value to be set 
	 * </ul>
	 * </p> 
	 * 
	 * @param map
	 * @param object
	 * @throws PersistenceJDBCException
	 */
	private void buildObject(HashMap<String, Object> map, A object) throws PersistenceJDBCException {
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object o = map.get(key);
			Field field = null;
			field = getFieldByName(object, key);
			if (field != null && o != null) {
				try {
					BeanUtils.setProperty(object, field.getName(), o);
				} catch (IllegalAccessException e) {
					throw new PersistenceJDBCException("Illegal access exception on buildObject dao.", e);
				} catch (InvocationTargetException e) {
					throw new PersistenceJDBCException("Invocation target exception on buildObject dao.", e);
				}
			}
		}
	}

	/**
	 * Retrieves a field of a table/attribute of an object by its name.
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws PersistenceJDBCException
	 */
	private Field getFieldByName(A object, String name) throws PersistenceJDBCException {
		
		if (object == null || name == null)
			return null;
		
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field field : fields)
				if (field.getName().equalsIgnoreCase(name))
					return field;
		} catch (SecurityException e) {
			throw new PersistenceJDBCException(
					"Security exception occurred when retrieving a field by its name", e);
		}
		
		return null;
	}
	
	/**
	 * Executes a native command SQL. Like 'executeUpdate'
	 * 
	 * @param sql
	 * @return Number of record affected
	 * @throws PersistenceJDBCException
	 */
	public final int execute(String sql) throws PersistenceJDBCException {
		
		if (sql == null)
			throw new PersistenceJDBCException("SQL is null");
		
		int result = 0;
		try {
			if (JDBCUtil.getInstance().getConnection() != null) {
				Statement st = JDBCUtil.getInstance().getConnection().createStatement();
				result = st.executeUpdate(sql);
			} else {
				throw new PersistenceJDBCException("connection is null");
			}
		} catch (SQLException e) {
			throw new PersistenceJDBCException("Error on execute a SQL: " + sql , e);
		}
		
		return result;
	}
	
	/**
	 * Get the column names from command SQL
	 * 
	 * @param sql
	 * @return Array of columns
	 * @throws PersistenceJDBCException
	 */
	protected String[] getColumnNamesFromSQL(String sql) throws PersistenceJDBCException {
		
		if (sql == null)
			return null;
		
		String sqlMin = (sql.toLowerCase()).trim();
		int begin = sqlMin.indexOf("select ");
		if (begin == 0)
			begin = begin + begin + "select".length();
		else
			return null;
		
		int fim = sqlMin.indexOf(" from ");
		String fields = null;
		if (fim != -1)
			fields = (sql.substring(begin + 1, fim + 1)).trim();
		else
			return null;
		
		String[] listFields = null;
		if (fields.indexOf(",") > -1)
			listFields = fields.split(",");
		else if (fields.indexOf(" ") > -1)
			listFields = fields.split(" ");
		else
			return new String[] { fields };
		
		List<String> list = new ArrayList<String>();
		for (String c : listFields)
			if (c != null && c.length() > 0)
				list.add(c.trim());
		
		listFields = list.toArray(new String[] {});
		
		return listFields;
	}

	/**
	 * Get the table name from command SQL
	 * 
	 * @param sql
	 * @return The table name
	 * @throws PersistenceJDBCException
	 */
	protected String getTableName(String sql) throws PersistenceJDBCException {
		
		if (sql == null)
			return null;
		sql = sql.trim();
		String sqlMin = (sql.toLowerCase()).trim();
		
		int begin = sqlMin.indexOf("select ");
		if (begin != 0)
			return null;
		
		int end = sqlMin.indexOf(" from ");
		if (end == -1)
			return null;
		
		return ((sql.trim()).substring(end + " from ".length(), sql.length())).trim().split(" ")[0];
	}

}
 
