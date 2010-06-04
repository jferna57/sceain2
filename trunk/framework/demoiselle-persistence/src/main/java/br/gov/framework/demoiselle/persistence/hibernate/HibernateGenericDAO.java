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
package br.gov.framework.demoiselle.persistence.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.layer.IDAO;
import br.gov.framework.demoiselle.persistence.IORMDAO;
import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

/**
 * <p>
 * Implementations of methods defined by IDAO and IORMDAO.
 * </p>
 * <p>
 * Applications using Hibernate solution could inherit this class in order to
 * reduce the amount of interface implementations.
 * </p>
 * 
 * @author CETEC/CTJEE
 * 
 * @param <A>
 *            entity type, which must implement at least {@link IPojo}
 * @see IDAO
 * @see IORMDAO
 * @see IPojo
 */
@SuppressWarnings("unchecked")
public class HibernateGenericDAO<A extends IPojo> implements IORMDAO<A> {

	private static Logger log = Logger.getLogger(HibernateGenericDAO.class);

	/**
	 * @see https://www.hibernate.org/328.html
	 */
	// private Class<IPojo> clazz;

	private MatchMode matchMode = MatchMode.ANYWHERE;

	/**
	 * Default constructor.
	 */
	public HibernateGenericDAO() {
		// clazz = (Class<IPojo>) ((ParameterizedType)
		// getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Return hibernate match mode
	 * 
	 * @return MatchMode
	 */
	public MatchMode getMatchMode() {
		return this.matchMode;
	}

	/**
	 * Change type of Match Mode in findByExample method. MatchMode is LIKE type
	 * of an search STRING field There are actual options: ANYWHERE (like
	 * '%search%') END (like '%search') EXACT (like 'search') START (like
	 * 'search%')
	 * 
	 * @param matchMode
	 */
	public void setMatchMode(MatchMode matchMode) {
		this.matchMode = matchMode;
	}

	public List<A> findByExample(A pojo) {

		PagedResult<A> results = findByExample(pojo, null);
		List<A> result = (List<A>) results.getResults();

		return result;
	}

	/**
	 * findByExample with paginated result
	 * 
	 * @param pojo
	 * @param page
	 *            the pagination
	 * @return List of pojo paged
	 */
	public PagedResult<A> findByExample(A pojo, Page page) {

		Long totalResults = new Long(0);
		Class<?> persistentClass = getPersistentClass(pojo);
		Criteria crit = getSession().createCriteria(persistentClass);
		Example example = Example.create(pojo);
		example.enableLike(this.matchMode);
		crit.add(example);

		if (page != null) {
			totalResults = new Long(getTotalResults(crit));
			crit.setFirstResult(page.getFirstResult());
			crit.setMaxResults(page.getMaxResults());
		}

		List<A> list = crit.list();
		PagedResult<A> result = new PagedResult<A>(page, totalResults, list);

		return result;
	}

	/**
	 * Get the number of records affected with hibernate query object. Do not
	 * load full records for get this information. Scroll method is used for
	 * pointer records and move to the last record and get its number.
	 * 
	 * @param criteria
	 * @return Total number of records
	 */
	public int getTotalResults(Criteria criteria) {

		ScrollableResults results = criteria.scroll();
		results.last();

		int result = results.getRowNumber();
		results.close();
		result++;

		log.debug("(" + result + ") rows in Criteria search paginated.");

		return result;
	}

	/**
	 * Get the number of records affected with hibernate query object. Do not
	 * load full records for get this information. Scroll method is used for
	 * pointer records and move to the last record and get its number.
	 * 
	 * @param query
	 * @return Total number of records
	 */
	public int getTotalResults(Query query) {

		ScrollableResults results = query.scroll();
		results.last();

		int result = results.getRowNumber();
		result++;

		log.debug("(" + result + ") rows in Query search paginated.");

		return result;
	}

	/**
	 * Get the class of generic type information.
	 * 
	 * @param pojo
	 * @return Class of IPojo
	 */
	public Class<A> getPersistentClass(A pojo) {

		Class<A> result = null;
		if (this.getClass().equals(HibernateGenericDAO.class)) {
			String strPersistentClass = getApplicationClassName(pojo);
			try {
				result = (Class<A>) Class.forName(strPersistentClass);
			} catch (ClassNotFoundException e) {
				result = null;
			}
		} else {
			Class<A> actual = (Class<A>) getClass();
			Type type = actual.getGenericSuperclass();
			ParameterizedType paramType = (ParameterizedType) type;
			Type[] types = paramType.getActualTypeArguments();
			Type typeResult = types[0];
			result = (Class<A>) typeResult;
		}

		return result;
	}

	/**
	 * Return the hibernate session. Case not exists a session, create it and
	 * return.
	 * 
	 * @return Hiberante Session.
	 */
	protected Session getSession() {
		return this.getSession(true);
	}

	/**
	 * Return Hiberante session. If the parameter is 'true', verify if exist any
	 * hibernate session and create a session if no exists.
	 * 
	 * @param open
	 *            TRUE = if no present any session, create one and return. FALSE
	 *            = return the session before created.
	 * @return Hibernate Session
	 */
	protected Session getSession(boolean open) {
		Session result = HibernateUtil.getInstance().getSession(open);
		if (result == null)
			throw new PersistenceHibernateException(
					"Without hibernate session on DAO. Please initialize a session");
		return result;
	}

	/*
	 * @see
	 * br.gov.framework.demoiselle.persistence.IORMDAO#findById(java.lang.Class,
	 * java.io.Serializable)
	 */
	public A findById(Class<A> pojoClass, Serializable id) {
		A obj = null;
		Session session = this.getSession();

		if (pojoClass == null)
			throw new PersistenceHibernateException(
					"Parameter pojoClass in findById is null.");

		if (id == null)
			throw new PersistenceHibernateException(
					"Parameter id in findById is null.");

		try {
			obj = (A) session.load(pojoClass, id);
			Hibernate.initialize(obj);
		} catch (HibernateException ex) {
			throw new PersistenceHibernateException(
					"Hibernate exception on findById", ex);
		}

		return obj;
	}

	/**
	 * @see br.gov.framework.demoiselle.persistence.IORMDAO#findById(java.io.Serializable)
	 * @deprecated consider still using {@link #findById(Class, Serializable)}
	 */
	// @SuppressWarnings("deprecation")
	public A findById(Serializable id) {
		throw new PersistenceHibernateException("Feature not implemented.");
		// return findById(clazz, id);
	}

	/**
	 * Return true if pojo is object persistent Pojo object not fixed on session
	 * hibernate after this method executed.
	 */
	public boolean exists(A pojo) {
		boolean exist = false;
		try {
			Session session = this.getSession();
			if (session != null)
				exist = session.contains(pojo);
		} catch (HibernateException ex) {
			throw new PersistenceHibernateException("Error on search pojo "
					+ pojo.getClass() + ": " + pojo.toString(), ex);
		}				
		return exist;
	}

	/**
	 * This method is important when work if lazy fields with proxies. Proxies
	 * not return the real name of objects.
	 * 
	 * @param pojo
	 * @return String represent the full name of persistent class
	 */
	private String getApplicationClassName(A pojo) {
		String className = pojo.getClass().getName();
		if (className.indexOf("$$") >= 0) {
			className = pojo.getClass().getSuperclass().getName();
		}
		return className;
	}

	/**
	 * Make the pojo persistent and insert on fisic repository. Fixed on
	 * hibernate session.
	 * 
	 * @param pojo
	 *            Pojo object to persist
	 * @return Object The key of object after persist.
	 */
	public Object insert(A pojo) {
		Object result = null;

		if (pojo == null)
			throw new PersistenceHibernateException(
					"Parameter pojo in insert is null.");

		try {
			Session session = this.getSession();
			if (session != null)
				result = (Object) session.save(pojo);
		} catch (HibernateException ex) {
			throw new PersistenceHibernateException("Error on insert pojo "
					+ pojo.getClass() + ": " + pojo.toString(), ex);
		}

		return result;
	}

	/**
	 * Make the pojo object transient and delete fisic from repository.
	 * 
	 * @param pojo
	 *            Object pojo to transform in transient object
	 */
	public void remove(A pojo) {

		if (pojo == null)
			throw new PersistenceHibernateException(
					"Parameter pojo in remove is null.");

		try {
			Session session = this.getSession();
			session.delete(pojo);
		} catch (HibernateException ex) {
			throw new PersistenceHibernateException("Error on remove pojo "
					+ pojo.getClass() + ": " + pojo.toString(), ex);
		}
	}

	/**
	 * Method flagged to be further removed.
	 * 
	 * @deprecated consider using the method makeTransient(Object)
	 * @param persistentObject
	 * @see HibernateGenericDAO#makeTransient(Object)
	 */
	protected void makeTransiente(Object persistentObject) {
		makeTransient(persistentObject);
	}

	/**
	 * This method NOT remove the object from persistent repository. Only remove
	 * the reference from hibernate session.
	 * 
	 * @param persistentObject
	 *            Object to remove from hibernate session.
	 */
	protected void makeTransient(Object persistentObject) {
		Session session = getSession();
		session.evict(persistentObject);
	}

	/**
	 * Deletes physically from repository and removes the reference from
	 * hibernate session.
	 * 
	 * @param pojo
	 *            Object to remove from persistent repository.
	 */
	public void update(A pojo) {

		if (pojo == null)
			throw new PersistenceHibernateException(
					"Parameter update in insert is null.");

		try {
			Session session = getSession();
			session.update(pojo);
		} catch (HibernateException ex) {
			throw new PersistenceHibernateException("Error on update pojo "
					+ pojo.getClass() + ": " + pojo.toString(), ex);
		}
	}

	/*
	 * HQL (Hibernate Query Languague) finding methods
	 */

	/**
	 * Retrieves a list of entities based on a given HQL string.
	 * 
	 * @param hql
	 *            an HQL query string
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<A> findHQL(String hql) throws PersistenceHibernateException {

		Query query = createQueryHQL(hql);
		List<A> list = query.list();

		return list;
	}

	/**
	 * Retrieves a list of entities based on a given HQL string with positional
	 * parameters.
	 * 
	 * @param hql
	 *            an HQL query string
	 * @param params
	 *            the query parameters
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<A> findHQL(String hql, Object... params)
			throws PersistenceHibernateException {

		Query query = createQueryHQL(hql);
		fillQueryPositionalParameters(query, params);
		List<A> list = query.list();

		return list;
	}

	/**
	 * Retrieves a list of entities based on a given HQL string with named
	 * parameters.
	 * 
	 * @param hql
	 *            an HQL query string
	 * @param params
	 *            the query named parameters
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<A> findHQL(String hql, Map<String, ? extends Object> params)
			throws PersistenceHibernateException {

		Query query = createQueryHQL(hql);
		fillQueryNamedParameters(query, params);
		List<A> list = query.list();

		return list;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given HQL string.
	 * 
	 * @param hql
	 *            HQL query
	 * @param page
	 *            the pagination
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<A> findHQL(String hql, Page page)
			throws PersistenceHibernateException {

		Query query = createQueryHQL(hql);
		PagedResult<A> result = createPagedResult(query, page);

		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given HQL string
	 * with positional parameters.
	 * 
	 * @param hql
	 *            HQL query
	 * @param page
	 *            the pagination
	 * @param params
	 *            the query parameters
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<A> findHQL(String hql, Page page, Object... params)
			throws PersistenceHibernateException {

		Query query = createQueryHQL(hql);
		fillQueryPositionalParameters(query, params);
		PagedResult<A> result = createPagedResult(query, page);

		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given HQL string
	 * with named parameters.
	 * 
	 * @param hql
	 *            HQL query
	 * @param page
	 *            the pagination
	 * @param params
	 *            the query named parameters
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<A> findHQL(String hql, Page page,
			Map<String, ? extends Object> params)
			throws PersistenceHibernateException {

		Query query = createQueryHQL(hql);
		fillQueryNamedParameters(query, params);
		PagedResult<A> result = createPagedResult(query, page);

		return result;
	}

	/*
	 * SQL (Native Query Language) finding methods
	 */

	/**
	 * Retrieves a list of entities based on a given native SQL string.
	 * 
	 * @param sql
	 *            a SQL query string
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<Serializable> find(String sql)
			throws PersistenceHibernateException {

		SQLQuery query = createQueryNativeSQL(sql);
		List<Serializable> result = query.list();

		return result;
	}

	/**
	 * Retrieves a list of entities based on a given native SQL string with
	 * positional parameters.
	 * 
	 * @param sql
	 *            a SQL query string
	 * @param params
	 *            the query parameters
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<Serializable> find(String sql, Object... params)
			throws PersistenceHibernateException {

		SQLQuery query = createQueryNativeSQL(sql);
		fillQueryPositionalParameters(query, params);
		List<Serializable> list = query.list();

		return list;
	}

	/**
	 * Retrieves a list of entities based on a given native SQL string with
	 * named parameters.
	 * 
	 * @param sql
	 *            a SQL query string
	 * @param params
	 *            the query named parameters
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<Serializable> find(String sql,
			Map<String, ? extends Object> params)
			throws PersistenceHibernateException {

		SQLQuery query = createQueryNativeSQL(sql);
		fillQueryNamedParameters(query, params);
		List<Serializable> list = query.list();

		return list;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given native SQL
	 * string.
	 * 
	 * @param sql
	 *            a SQL query string
	 * @param page
	 *            the pagination
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<Serializable> find(String sql, Page page)
			throws PersistenceHibernateException {

		SQLQuery query = createQueryNativeSQL(sql);
		PagedResult<Serializable> result = createNativePagedResult(query, page);

		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given native SQL
	 * string with positional parameters.
	 * 
	 * @param sql
	 *            a SQL query string
	 * @param page
	 *            the pagination
	 * @param params
	 *            the query parameters
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<Serializable> find(String sql, Page page,
			Object... params) throws PersistenceHibernateException {

		SQLQuery query = createQueryNativeSQL(sql);
		fillQueryPositionalParameters(query, params);
		PagedResult<Serializable> result = createNativePagedResult(query, page);

		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given native SQL
	 * string with named parameters.
	 * 
	 * @param sql
	 *            a SQL query string
	 * @param page
	 *            the pagination
	 * @param params
	 *            the query named parameters
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<Serializable> find(String sql, Page page,
			Map<String, ? extends Object> params)
			throws PersistenceHibernateException {

		SQLQuery query = createQueryNativeSQL(sql);
		fillQueryNamedParameters(query, params);
		PagedResult<Serializable> result = createNativePagedResult(query, page);

		return result;
	}

	/*
	 * Criteria API based finding methods
	 */

	/**
	 * Retrieves a list of entities based on a given criteria.
	 * 
	 * @param criteria
	 *            Hibernate Criteria object
	 * @return a list of entities
	 * @throws PersistenceHibernateException
	 */
	public List<A> findCriteria(Criteria criteria)
			throws PersistenceHibernateException {
		List<A> list = criteria.list();
		return list;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given criteria.
	 * 
	 * @param criteria
	 *            Hibernate Criteria object
	 * @param page
	 *            the pagination
	 * @return a paginated list of entities
	 * @throws PersistenceHibernateException
	 */
	public PagedResult<A> findCriteria(Criteria criteria, Page page)
			throws PersistenceHibernateException {

		Long totalResults = new Long(0);

		if (page != null) {
			totalResults = new Long(getTotalResults(criteria));
			criteria.setFirstResult(page.getFirstResult());
			criteria.setMaxResults(page.getMaxResults());
		}

		List<A> list = criteria.list();
		PagedResult<A> result = new PagedResult<A>(page, totalResults, list);

		return result;
	}

	/*
	 * Additional support methods
	 */

	/**
	 * Sets the given query parameters with the specified values.
	 * 
	 * @param query
	 *            the query
	 * @param params
	 *            the parameter values
	 */
	private void fillQueryPositionalParameters(Query query, Object... params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
	}

	/**
	 * Sets the given query parameters with the specified map values.
	 * 
	 * @param query
	 *            the query
	 * @param params
	 *            the parameter values
	 */
	private void fillQueryNamedParameters(Query query,
			Map<String, ? extends Object> params) {
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, ? extends Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
	}

	/**
	 * Creates an instance of {@link Query} using the specified HQL string.
	 * 
	 * @param hql
	 *            the HQL query string
	 * @return a Query
	 */
	private Query createQueryHQL(String hql) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		return query;
	}

	/**
	 * Creates an instance of {@link SQLQuery} using the specified native SQL
	 * string.
	 * 
	 * @param hql
	 *            the HQL query string
	 * @return a SQLQuery
	 */
	private SQLQuery createQueryNativeSQL(String sql) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		return query;
	}

	/**
	 * Creates a paged result for the given query and pagination.
	 * 
	 * @param query
	 *            a prepared Query
	 * @param page
	 *            the pagination
	 * @return a paginated result
	 */
	private PagedResult<A> createPagedResult(Query query, Page page) {

		Long totalResults = new Long(0);

		if (page != null) {
			totalResults = new Long(getTotalResults(query));
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}

		List<A> list = query.list();
		PagedResult<A> result = new PagedResult<A>(page, totalResults, list);

		return result;
	}

	/**
	 * Creates a paged result for the given native query and pagination.
	 * 
	 * @param query
	 *            a prepared Query
	 * @param page
	 *            the pagination
	 * @return a paginated result
	 */
	private PagedResult<Serializable> createNativePagedResult(Query query,
			Page page) {

		Long totalResults = new Long(0);

		if (page != null) {
			totalResults = new Long(getTotalResults(query));
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}

		List<Serializable> list = query.list();
		PagedResult<Serializable> result = new PagedResult<Serializable>(page,
				totalResults, list);

		return result;
	}

}