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
package br.gov.framework.demoiselle.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.layer.IDAO;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

/**
 * <p>
 * Implementation class for Data Access Object interface IDAO.
 * </p>
 * <p>
 * Applications using JPA (Java Persistence API) solution should inherit this
 * generic class in order to reduce amount of code and to leverage reusability.
 * </p>
 * 
 * @param <A>	entity type, which must implement at least {@link IPojo}
 * 
 * @author CETEC/CTJEE
 * @see IDAO
 * @see IPojo
 */
public abstract class JPAGenericDAO<A extends IPojo> implements IDAO<A> {

	private static Logger log = Logger.getLogger(JPAGenericDAO.class);
	
	/* EntityManager must be automatically injected on an AOP fashion by AspectJ */
	@Injection
	protected EntityManager em;
	
	/* a reference to the entity class should be kept in memory for further queries */
	protected Class<IPojo> clazz;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings(value = "unchecked")
	protected JPAGenericDAO() {
        clazz = (Class<IPojo>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

    /**
     * Gets the internal entity manager.
     *
     * @return em entity manager
     */
	public EntityManager getEntityManager() {
		return em;
	}

    /**
     * Sets the internal entity manager.
     *
     * @param em entity manager
     */
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
    
	/*
	 * IDAO interface implementation methods
	 */
	
	/*
	 * Returns true whether the given object is contained in the persistence context.
	 * 
	 * @see br.gov.framework.demoiselle.core.layer.IDAO#exists(java.lang.Object)
	 */
	public boolean exists(A pojo) {
		return em.contains(pojo);
	}

	/*
	 * Inserts the given object into the persistence context.
	 * 
	 * @see br.gov.framework.demoiselle.core.layer.IDAO#insert(java.lang.Object)
	 */
	public Object insert(A pojo) {
		
		log.debug("Persisting entity " + pojo);
		
		em.persist(pojo);
		return pojo;
	}

	/*
	 * Merges the given object into the persistence context.
	 * 
	 * @see br.gov.framework.demoiselle.core.layer.IDAO#update(java.lang.Object)
	 */
	public void update(A pojo) {
		
		log.debug("Merging entity " + pojo);
		
		em.merge(pojo);
	}

	/*
	 * Removes the given object from the persistence context.
	 * 
	 * @see br.gov.framework.demoiselle.core.layer.IDAO#remove(java.lang.Object)
	 */
	public void remove(A pojo) {
		
		log.debug("Removing entity " + pojo);
		
		em.remove(pojo);
	}

	/**
	 * Finds an entity by its identity (i.e. primary key).
	 * 
	 * @param id	identity
	 * @return	the object instance
	 */
	@SuppressWarnings("unchecked")
	public A findById(Serializable id) {
		return (A) em.find(clazz, id);
	}

	/*
	 * Add-on methods aiming at improving JPA reusability
	 */

    /**
     * Refreshes the given entity.
     * 
     * @param pojo to refresh
     */
    public void refresh(final A pojo) {
    	
		log.debug("Refreshing entity " + pojo);
		
        em.refresh(pojo);
    }

    /**
     * Flushes and clears the current persistence context.
     */
    public final void flushAndClear() {
    	
		log.debug("Flushing and cleaning up the persistence context");
		
        em.flush();
        em.clear();
    }

    /**
     * Executes an update or delete statement in JPQL language.
     * 
     * @param jpql
     * @return	the number of entities modified.
     */
    public int executeUpdate(String jpql) {
    	
		log.debug("Executing update JPQL: " + jpql);
		
    	return em.createQuery(jpql).executeUpdate();
    }
	
    /**
     * Retrieves the number of persisted objects for the current class type.
     * 
     * @return the row count
     */
    public Long countAll() {
    	
		log.debug("Counting all existing entities");
    	
    	Query query = em.createQuery("select count(this) from " + clazz.getSimpleName() + " this");
    	Long result = (Long) query.getSingleResult();
    	
    	return result;
    }
    
    /**
     * Retrieves all objects for the current class type.
     * 
     * @return List<A>
     */
    @SuppressWarnings(value = "unchecked")
    public List<A> findAll() {
    	
		log.debug("Retrieving all existing entities");
		
    	Query query = em.createQuery("select this from " + clazz.getSimpleName() + " this");
    	List<A> result = query.getResultList();
    	
    	return result;
    }
    
    /**
 	 * Retrieves a paginated collection of all existing entities.
 	 * 
 	 * @param page	the pagination
	 * @return	a paginated list of entities
     */
    @SuppressWarnings(value = "unchecked")
    public PagedResult<A> findAll(Page page) {
    	
		log.debug("Retrieving a paginated list of all entities");
		
		Long totalResults = new Long(0);
		
    	Query query = em.createQuery("select this from " + clazz.getSimpleName() + " this");
		if (page != null) {
			totalResults = countAll();
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		
		List<A> list = query.getResultList();
		PagedResult<A> result = new PagedResult<A>(page, totalResults, list);
		
		return result;
    }
    
	/*
	 * JPQL (JPA Query Language) finding methods
	 */
	
    private static final String DEBUG_FIND_BY_JPQL       = "Retrieving objects by JPQL: ";
    private static final String DEBUG_FIND_BY_JPQL_PAGED = "Retrieving paginated objects by JPQL: ";
    
	/**
	 * Retrieves objects using JPQL without parameters.
	 * 
	 * @param jpql	a JPQL query string
	 * @return	a list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<A> findByJPQL(String jpql) {
		
		log.debug(DEBUG_FIND_BY_JPQL + jpql);
		
		Query query = em.createQuery(jpql);
		List<A> result = query.getResultList();
		
		return result;
	}

	/**
	 * Retrieves objects using JPQL with positional parameters.
	 * 
	 * @param jpql	a JPQL query string
     * @param params the query parameters
	 * @return	a list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<A> findByJPQL(String jpql, Object... params) {
		
		log.debug(DEBUG_FIND_BY_JPQL + jpql);
		
		Query query = em.createQuery(jpql);
		fillQueryPositionalParameters(query, params);
		List<A> result = query.getResultList();
		
		return result;
	}

	/**
	 * Retrieves objects using JPQL with named parameters.
	 * 
	 * @param jpql	a JPQL query string
     * @param params the query named parameters
	 * @return	a list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<A> findByJPQL(String jpql, Map<String, ? extends Object> params) {
		
		log.debug(DEBUG_FIND_BY_JPQL + jpql);
		
		Query query = em.createQuery(jpql);
		fillQueryNamedParameters(query, params);
		List<A> result = query.getResultList();
		
		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given JPQL string.
	 * 
	 * @param jpql	a JPQL query string
	 * @param page	the pagination
	 * @return	a paginated list of entities
	 */
	public PagedResult<A> findByJPQL(String jpql, Page page) {
		
		log.debug(DEBUG_FIND_BY_JPQL_PAGED + jpql);
		
		Query query = em.createQuery(jpql);
		Query countQuery = createCountQueryForJPQL(jpql);
		
		PagedResult<A> result = createPagedResultForJPQL(query, page, countQuery);
		
		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given JPQL string
	 * with positional parameters.
	 * 
	 * @param jpql	a JPQL query string
	 * @param page	the pagination
     * @param params the query parameters
	 * @return a paginated list of entities
	 */
	public PagedResult<A> findByJPQL(String jpql, Page page, Object... params) {
		
		log.debug(DEBUG_FIND_BY_JPQL_PAGED + jpql);
		
		Query query = em.createQuery(jpql);
		Query countQuery = createCountQueryForJPQL(jpql);
		
		fillQueryPositionalParameters(query, params);
		fillQueryPositionalParameters(countQuery, params);
		
		PagedResult<A> result = createPagedResultForJPQL(query, page, countQuery);
		
		return result;
	}

	/**
	 * Retrieves a paginated collection of entities based on a given JPQL string
	 * with named parameters.
	 * 
	 * @param jpql	a JPQL query string
	 * @param page	the pagination
     * @param params the query named parameters
	 * @return a paginated list of entities
	 */
	public PagedResult<A> findByJPQL(String jpql, Page page, Map<String, ? extends Object> params) {
		
		log.debug(DEBUG_FIND_BY_JPQL_PAGED + jpql);
		
		Query query = em.createQuery(jpql);
		Query countQuery = createCountQueryForJPQL(jpql);
		
		fillQueryNamedParameters(query, params);
		fillQueryNamedParameters(countQuery, params);
		
		PagedResult<A> result = createPagedResultForJPQL(query, page, countQuery);
		
		return result;
	}

	/*
	 * Named query finding methods
	 */
	
    private static final String DEBUG_FIND_BY_NAMED_QUERY = "Retrieving objects by named query: ";

    /**
     * Retrieves entities using a named query without parameters.
     *
     * @param queryName the name of the query
     * @return the list of entities
     */
	@SuppressWarnings("unchecked")
	public List<A> findByNamedQuery(final String queryName) {
		
		log.debug(DEBUG_FIND_BY_NAMED_QUERY + queryName);
		
		Query query = em.createNamedQuery(queryName);
		List<A> result = (List<A>) query.getResultList();
		
		return result;
	}
    
    /**
     * Retrieves entities using a named query with positional parameters.
     *
     * @param queryName the name of the query
     * @param params the query parameters
     * @return the list of entities
     */
    @SuppressWarnings("unchecked")
	public List<A> findByNamedQuery(final String queryName, Object... params) {
    	
		log.debug(DEBUG_FIND_BY_NAMED_QUERY + queryName);
    	
		Query query = em.createNamedQuery(queryName);
		fillQueryPositionalParameters(query, params);
		
		List<A> result = (List<A>) query.getResultList();
		
		return result;
	}
    
    /**
     * Retrieves entities using a named query with named parameters.
     *
     * @param queryName the name of the query
     * @param params the query named parameters
     * @return the list of entities
     */
    @SuppressWarnings("unchecked")
	public List<A> findByNamedQuery(final String queryName,
			Map<String, ? extends Object> params) {
    	
		log.debug(DEBUG_FIND_BY_NAMED_QUERY + queryName);
		
		Query query = em.createNamedQuery(queryName);
		fillQueryNamedParameters(query, params);
		
		List<A> result = (List<A>) query.getResultList();
		
		return result;
	}

	/*
	 * SQL (Native Query Language) finding methods
	 */

    private static final String DEBUG_FIND_BY_NATIVE_QUERY = "Retrieving objects by native query: ";

    /**
     * Retrieves entities using a native SQL query without parameters.
     *
     * @param sql	the native SQL string
     * @return the list of entities
     */
	@SuppressWarnings("unchecked")
	public List<A> findByNativeQuery(String sql) {
		
		log.debug(DEBUG_FIND_BY_NATIVE_QUERY + sql);
		
		Query query = em.createNativeQuery(sql, clazz);
		List<A> result = (List<A>) query.getResultList();
		
		return result;
    }
    
    /**
     * Retrieves entities using a native SQL query with positional parameters.
     *
     * @param sql	the native SQL string
     * @param params the query parameters
     * @return the list of entities
     */
    @SuppressWarnings("unchecked")
	public List<A> findByNativeQuery(String sql, Object... params) {
    	
		log.debug(DEBUG_FIND_BY_NATIVE_QUERY + sql);
		
		Query query = em.createNativeQuery(sql, clazz);
		fillQueryPositionalParameters(query, params);
		List<A> result = (List<A>) query.getResultList();
		
		return result;
    }
    
    /**
     * Retrieves entities using a native SQL query with named parameters.
     *
     * @param sql	the native SQL string
     * @param params the query named parameters
     * @return the list of entities
     */
    @SuppressWarnings("unchecked")
	public List<A> findByNativeQuery(String sql,
			Map<String, ? extends Object> params) {
    	
		log.debug(DEBUG_FIND_BY_NATIVE_QUERY + sql);
		
		Query query = em.createNativeQuery(sql, clazz);
		fillQueryNamedParameters(query, params);
		List<A> result = (List<A>) query.getResultList();
		
		return result;
    }

	/*
	 * Additional support methods
	 */

    /**
     * Sets the given query parameters with the specified values.
     * 
     * @param query	the query
     * @param params	the parameter values
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
     * @param query	the query
     * @param params	the parameter values
     */
	private void fillQueryNamedParameters(Query query, Map<String, ? extends Object> params) {
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, ? extends Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
    }
    
	/**
	 * Creates a paged result for the given queries and pagination parameters.
	 * 
	 * @param query	a prepared Query
	 * @param page	the pagination
	 * @param countQuery	a prepared counting Query
	 * @return	a paginated result
	 */
	@SuppressWarnings("unchecked")
	private PagedResult<A> createPagedResultForJPQL(Query query, Page page, Query countQuery) {
		
		Long totalResults = new Long(0);
		
		if (page != null) {
			totalResults = (Long) countQuery.getSingleResult();
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		
		List<A> list = query.getResultList();
		PagedResult<A> result = new PagedResult<A>(page, totalResults, list);
		
		return result;
	}

	/**
	 * Constructs a counting {@link Query} object for the specified JPQL string.
	 * 
	 * @param jpql	a JPQL query string
	 * @return	a Query
	 */
	private Query createCountQueryForJPQL(String jpql) {
		
    	// TopLink does not accept "select count(1) from Entity e" according to JPA 1.0 spec
    	// @see "JSR 220: EJB 3.0 - JPA", Final Release, page 102
		
		int fromIndex = 0, orderIndex = 0;
		String queryInUpperCase = jpql.toUpperCase();

		// if JPA query starts with FROM then fromIndex as 0 is correct, otherwise find
		// where FROM keyword starts in the query string and set the fromIndex
		if (!queryInUpperCase.startsWith("FROM ")) {
			fromIndex = queryInUpperCase.indexOf(" FROM ");
			if (fromIndex == -1) {
				// couldn't find the FROM keyword in the query
	            throw new UnsupportedOperationException("Invalid JPQL query statement: " + jpql);
			}
		}
		
		String remainderQuery = null;
		orderIndex = queryInUpperCase.indexOf(" ORDER BY ");
		if (orderIndex >= 0) {
			remainderQuery = jpql.substring(fromIndex + 1, orderIndex);
		} else {
			remainderQuery = jpql.substring(fromIndex + 1, jpql.length());
		}
		
		String alias = "1";
		
		// SELECT e.id, e.name FROM Entity e ORDER BY e.name
		// --------------------=====^
		StringTokenizer st = new StringTokenizer(remainderQuery.substring(5));
		st.nextToken();
		if (st.hasMoreTokens())
			alias = (String) st.nextElement();
		
		jpql = "select count(" + alias + ") " + remainderQuery;
		Query query = em.createQuery(jpql);
		
		return query;
	}

}