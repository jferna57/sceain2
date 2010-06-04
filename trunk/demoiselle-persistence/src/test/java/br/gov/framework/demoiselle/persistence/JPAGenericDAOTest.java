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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

/**
 * @author CETEC/CTJEE
 */
public class JPAGenericDAOTest {

	private DAOMock dao;
	
	private PojoMock entity;
	private List<PojoMock> list;
	
	private EntityManager em;
	private Query query;
	
	@Before
	public void setUp() throws Exception {
		
		em = EasyMock.createMock(EntityManager.class);
		query = EasyMock.createMock(Query.class);
		
		dao = new DAOMock();
		dao.setEntityManager(em);
		
		entity = new PojoMock();
		list = new ArrayList<PojoMock>();
	}

	@After
	public void tearDown() throws Exception {
		em = null;
		query = null;
		dao = null;
		entity = null;
		list = null;
	}

	@Test
	public void testGetAndSetEntityManager() {

		dao.setEntityManager(em);
		assertEquals(em, dao.getEntityManager());
		
		dao.setEntityManager(null);
		assertNull(dao.getEntityManager());
	}

	@Test
	public void testExists() {
		
		PojoMock another = new PojoMock();
		
		EasyMock.expect(em.contains(entity)).andReturn(true);
		EasyMock.expect(em.contains(another)).andReturn(false);
		EasyMock.expect(em.contains(null)).andReturn(false);
		EasyMock.replay(em);
		
		Assert.assertTrue(dao.exists(entity));
		Assert.assertFalse(dao.exists(another));
		Assert.assertFalse(dao.exists(null));
	}

	@Test
	public void testInsert() {
		
		em.persist(entity);
		EasyMock.replay(em);
		
		Assert.assertEquals(entity, dao.insert(entity));
	}

	@Test
	public void testUpdate() {
		
		EasyMock.expect(em.merge(entity)).andReturn(entity);
		EasyMock.replay(em);
		
		dao.update(entity);
	}

	@Test
	public void testRemove() {
		
		em.remove(entity);
		EasyMock.replay(em);
		
		dao.remove(entity);
	}

	@Test
	public void testFindById() {
		
		EasyMock.expect(em.find(PojoMock.class, 123)).andReturn(entity);
		EasyMock.expect(em.find(PojoMock.class, 456)).andReturn(null);
		EasyMock.replay(em);
		
		Assert.assertEquals(entity, dao.findById(123));
		Assert.assertNull(dao.findById(456));
	}

	@Test
	public void testRefresh() {

		em.refresh(entity);
		EasyMock.replay(em);
		
		dao.refresh(entity);
	}

	@Test
	public void testFlushAndClear() {

		em.flush();
		em.clear();
		EasyMock.replay(em);
		
		dao.flushAndClear();
	}

	@Test
	public void testExecuteUpdate() {
		
		EasyMock.expect(query.executeUpdate()).andReturn(100);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("jpql statement")).andReturn(query);
		EasyMock.replay(em);
		
		Assert.assertEquals(100, dao.executeUpdate("jpql statement"));
	}

	@Test
	public void testCountAll() {

		EasyMock.expect(query.getSingleResult()).andReturn(500L);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("select count(this) from PojoMock this")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(500L, dao.countAll().longValue());
	}

	@Test
	public void testFindAll() {

		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("select this from PojoMock this")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findAll());
	}

	@Test
	public void testFindAllPage() {
		
		Page page = new Page(10, 3);
		
		Query query2 = EasyMock.createMock(Query.class);
		EasyMock.expect(query2.getSingleResult()).andReturn(500L);
		EasyMock.replay(query2);

		EasyMock.expect(query.setFirstResult(20)).andReturn(query);
		EasyMock.expect(query.setMaxResults(10)).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);

		EasyMock.expect(em.createQuery("select this from PojoMock this")).andReturn(query);
		EasyMock.expect(em.createQuery("select count(this) from PojoMock this")).andReturn(query2);
		EasyMock.replay(em);

		PagedResult<PojoMock> result = dao.findAll(page);
		Assert.assertNotNull(result);
		Assert.assertEquals(new Long(500), result.getTotalResults());
		Assert.assertEquals(page, result.getPage());
		Assert.assertEquals(list, result.getResults());
	}

	@Test
	public void testFindByJPQL() {

		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("jpql statement")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByJPQL("jpql statement"));
	}

	@Test
	public void testFindByJPQLWithParamsArray() {

		EasyMock.expect(query.setParameter(1, "a")).andReturn(query);
		EasyMock.expect(query.setParameter(2, "b")).andReturn(query);
		EasyMock.expect(query.setParameter(3, "c")).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("jpql statement")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByJPQL("jpql statement", "a", "b", "c"));
	}

	@Test
	public void testFindByJPQLWithParamsMap() {

		Byte aa = new Byte((byte) 111);
		Short bb = new Short((short) 222);
		Integer cc = new Integer(333);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", aa);
		map.put("b", bb);
		map.put("c", cc);
		
		EasyMock.expect(query.setParameter("a", aa)).andReturn(query);
		EasyMock.expect(query.setParameter("b", bb)).andReturn(query);
		EasyMock.expect(query.setParameter("c", cc)).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("jpql statement")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByJPQL("jpql statement", map));
	}

	@Test
	public void testFindByJPQLWithPage() {

		Page page = new Page(10, 3);
		
		Query query2 = EasyMock.createMock(Query.class);
		EasyMock.expect(query2.getSingleResult()).andReturn(500L);
		EasyMock.replay(query2);

		EasyMock.expect(query.setFirstResult(20)).andReturn(query);
		EasyMock.expect(query.setMaxResults(10)).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);

		EasyMock.expect(em.createQuery("jpql from entity order by id")).andReturn(query);
		EasyMock.expect(em.createQuery("select count(1) from entity")).andReturn(query2);
		EasyMock.replay(em);

		PagedResult<PojoMock> result = dao.findByJPQL("jpql from entity order by id", page);
		Assert.assertNotNull(result);
		Assert.assertEquals(new Long(500), result.getTotalResults());
		Assert.assertEquals(page, result.getPage());
		Assert.assertEquals(list, result.getResults());
	}

	@Test
	public void testFindByJPQLWithPageAndParamsArray() {

		Page page = new Page(10, 3);
		
		Query query2 = EasyMock.createMock(Query.class);
		EasyMock.expect(query2.setParameter(1, "a")).andReturn(query);
		EasyMock.expect(query2.setParameter(2, "b")).andReturn(query);
		EasyMock.expect(query2.setParameter(3, "c")).andReturn(query);
		EasyMock.expect(query2.getSingleResult()).andReturn(500L);
		EasyMock.replay(query2);

		EasyMock.expect(query.setFirstResult(20)).andReturn(query);
		EasyMock.expect(query.setMaxResults(10)).andReturn(query);
		EasyMock.expect(query.setParameter(1, "a")).andReturn(query);
		EasyMock.expect(query.setParameter(2, "b")).andReturn(query);
		EasyMock.expect(query.setParameter(3, "c")).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("jpql from entity")).andReturn(query);
		EasyMock.expect(em.createQuery("select count(1) from entity")).andReturn(query2);
		EasyMock.replay(em);

		PagedResult<PojoMock> result = dao.findByJPQL("jpql from entity", page, "a", "b", "c");
		Assert.assertNotNull(result);
		Assert.assertEquals(new Long(500), result.getTotalResults());
		Assert.assertEquals(page, result.getPage());
		Assert.assertEquals(list, result.getResults());
	}

	@Test
	public void testFindByJPQLWithPageAndParamsMap() {

		Byte aa = new Byte((byte) 111);
		Short bb = new Short((short) 222);
		Integer cc = new Integer(333);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", aa);
		map.put("b", bb);
		map.put("c", cc);
		
		Page page = new Page(10, 3);
		
		Query query2 = EasyMock.createMock(Query.class);
		EasyMock.expect(query2.setParameter("a", aa)).andReturn(query);
		EasyMock.expect(query2.setParameter("b", bb)).andReturn(query);
		EasyMock.expect(query2.setParameter("c", cc)).andReturn(query);
		EasyMock.expect(query2.getSingleResult()).andReturn(500L);
		EasyMock.replay(query2);

		EasyMock.expect(query.setFirstResult(20)).andReturn(query);
		EasyMock.expect(query.setMaxResults(10)).andReturn(query);
		EasyMock.expect(query.setParameter("a", aa)).andReturn(query);
		EasyMock.expect(query.setParameter("b", bb)).andReturn(query);
		EasyMock.expect(query.setParameter("c", cc)).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createQuery("jpql from entity")).andReturn(query);
		EasyMock.expect(em.createQuery("select count(1) from entity")).andReturn(query2);
		EasyMock.replay(em);

		PagedResult<PojoMock> result = dao.findByJPQL("jpql from entity", page, map);
		Assert.assertNotNull(result);
		Assert.assertEquals(new Long(500), result.getTotalResults());
		Assert.assertEquals(page, result.getPage());
		Assert.assertEquals(list, result.getResults());
	}

	@Test
	public void testFindByNamedQuery() {

		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNamedQuery("QueryName")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByNamedQuery("QueryName"));
	}

	@Test
	public void testFindByNamedQueryWithParamsArray() {
		
		EasyMock.expect(query.setParameter(1, "a")).andReturn(query);
		EasyMock.expect(query.setParameter(2, "b")).andReturn(query);
		EasyMock.expect(query.setParameter(3, "c")).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNamedQuery("QueryName")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByNamedQuery("QueryName", "a", "b", "c"));
	}

	@Test
	public void testFindByNamedQueryWithParamsMap() {

		Byte aa = new Byte((byte) 111);
		Short bb = new Short((short) 222);
		Integer cc = new Integer(333);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", aa);
		map.put("b", bb);
		map.put("c", cc);
		
		EasyMock.expect(query.setParameter("a", aa)).andReturn(query);
		EasyMock.expect(query.setParameter("b", bb)).andReturn(query);
		EasyMock.expect(query.setParameter("c", cc)).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNamedQuery("QueryName")).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByNamedQuery("QueryName", map));
	}

	@Test
	public void testFindByNativeQuery() {

		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNativeQuery("sql statement", PojoMock.class)).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByNativeQuery("sql statement"));
	}

	@Test
	public void testFindByNativeQueryWithParamsArray() {

		EasyMock.expect(query.setParameter(1, "a")).andReturn(query);
		EasyMock.expect(query.setParameter(2, "b")).andReturn(query);
		EasyMock.expect(query.setParameter(3, "c")).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNativeQuery("sql statement", PojoMock.class)).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByNativeQuery("sql statement", "a", "b", "c"));
	}

	@Test
	public void testFindByNativeQueryWithParamsMap() {

		Byte aa = new Byte((byte) 111);
		Short bb = new Short((short) 222);
		Integer cc = new Integer(333);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", aa);
		map.put("b", bb);
		map.put("c", cc);
		
		EasyMock.expect(query.setParameter("a", aa)).andReturn(query);
		EasyMock.expect(query.setParameter("b", bb)).andReturn(query);
		EasyMock.expect(query.setParameter("c", cc)).andReturn(query);
		EasyMock.expect(query.getResultList()).andReturn(list);
		EasyMock.replay(query);
		
		EasyMock.expect(em.createNativeQuery("sql statement", PojoMock.class)).andReturn(query);
		EasyMock.replay(em);

		Assert.assertEquals(list, dao.findByNativeQuery("sql statement", map));
	}

	@Test
	public void testFindByJPQLWithInvalidStatement() {

		Page page = new Page(10, 3);

		EasyMock.replay(query);

		EasyMock.expect(em.createQuery("invalid jpql")).andReturn(query);
		EasyMock.replay(em);

		try {
			dao.findByJPQL("invalid jpql", page);
			Assert.fail("Must not reach this line!");
		} catch (UnsupportedOperationException e) {
			Assert.assertTrue(true);
		}
	}

	private class PojoMock implements IPojo {
		private static final long serialVersionUID = 1L;
	}
	
	private class DAOMock extends JPAGenericDAO<PojoMock> {
	}
	
}
