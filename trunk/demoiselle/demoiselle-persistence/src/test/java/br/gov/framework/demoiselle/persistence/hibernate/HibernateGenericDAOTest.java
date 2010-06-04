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

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;
import br.gov.framework.demoiselle.util.page.PagedResult;

public class HibernateGenericDAOTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExists() {		
		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);
		ITransactionContext context = hibernate.getContextMock();		
		ContextLocator.getInstance().setTransactionContext(context);
		//HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();
		
//		Assert.assertTrue(dao.exists(hibernate.getAluno1()));
//		Assert.assertFalse(dao.exists(hibernate.getAluno2()));
//		Assert.assertTrue(dao.exists(hibernate.getProfessor1()));
	}
	

	
	@Test
	public void testFindByExampleA() {

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(null);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		IPojo ipojo = EasyMock.createMock(IPojo.class);
		EasyMock.replay(ipojo);

		List<IPojo> list = dao.findByExample(ipojo);

		Assert.assertTrue(list.size() > 0);

	}

	@Test
	public void testGetMatchMode() {
		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();
		MatchMode mode = dao.getMatchMode();

		Assert.assertEquals(MatchMode.ANYWHERE, mode);
	}

	
	
	@Test
	public void testSetMatchMode() {
		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();
		dao.setMatchMode(MatchMode.END);
		MatchMode mode = dao.getMatchMode();

		Assert.assertEquals(MatchMode.END, mode);
	}
	
	@Test
	public void testFindByExampleAPage() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher1");

		Professor professor1 = new Professor();
		professor1.setNome("teacher2");

		Professor professor2 = new Professor();
		professor2.setNome("teacher3");

		array.add(professor);

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		Professor professor3 = new Professor();
		professor3.setNome("teacher4");

		PagedResult<IPojo> result = dao.findByExample(professor1, hibernate
				.getPageMock());

		Assert.assertNotNull(result);

	}
		
	@Test
	public void testGetTotalResultsCriteria() {

		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("professor");

		Professor professor1 = new Professor();
		professor1.setNome("professor1");

		Professor professor2 = new Professor();
		professor2.setNome("professor2");

		array.add(professor);

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		Professor professor3 = new Professor();
		professor3.setNome("professor");

		PagedResult<IPojo> result = dao.findByExample(professor1, hibernate
				.getPageMock());

		Long total = result.getTotalResults();

		Assert.assertTrue(total.longValue() > 0);

	}

	@Test
	public void testGetTotalResultsQuery() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("professor");

		Professor professor1 = new Professor();
		professor1.setNome("professor1");

		Professor professor2 = new Professor();
		professor2.setNome("professor2");

		array.add(professor);

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		Professor professor3 = new Professor();
		professor3.setNome("professor");

		int total = dao.getTotalResults(hibernate.getCriteriaMock());

		Assert.assertTrue(total > 0);

	}

	@Test
	public void testGetPersistentClass() {

		/*
		 * ArrayList<IPojo> array = new ArrayList<IPojo>();
		 * 
		 * Professor professor = new Professor(); professor.setNome("teacher1");
		 * 
		 * Professor professor1 = new Professor();
		 * professor1.setNome("teacher2");
		 * 
		 * Professor professor2 = new Professor();
		 * professor2.setNome("teacher3");
		 * 
		 * array.add(professor);
		 * 
		 * HibernateMock hibernate = new HibernateMock();
		 * hibernate.configure(array);
		 * 
		 * ITransactionContext context = hibernate.getContextMock();
		 * 
		 * ContextLocator.getInstance().setTransactionContext(context);
		 * 
		 * HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();
		 * 
		 * IPojo pojo = EasyMock.createMock(IPojo.class);
		 * 
		 * EasyMock.replay(pojo);
		 * 
		 * Class<IPojo> classPojo = dao.getPersistentClass(pojo);
		 * 
		 * Assert.assertNotNull(classPojo);
		 * 
		 * HibernateGenericDAOStub<Professor> daoStub = new
		 * HibernateGenericDAOStub<Professor>();
		 * 
		 * try { Class<Professor> cls = daoStub.getPersistentClass(professor); }
		 * catch (Exception e) { e.printStackTrace(); }
		 * 
		 * Assert.assertNotNull(classPojo);
		 */

	}

	@Test
	public void testGetSession() {

		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher1");

		Professor professor1 = new Professor();
		professor1.setNome("teacher2");

		Professor professor2 = new Professor();
		professor2.setNome("teacher3");

		array.add(professor);

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		IPojo pojo = EasyMock.createMock(IPojo.class);

		EasyMock.replay(pojo);

		Session session = dao.getSession();

		Assert.assertNotNull(session);
	}

	
	
	@Test
	public void testGetSessionBoolean() {

		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher1");

		Professor professor1 = new Professor();
		professor1.setNome("teacher2");

		Professor professor2 = new Professor();
		professor2.setNome("teacher3");

		array.add(professor);

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		IPojo pojo = EasyMock.createMock(IPojo.class);

		EasyMock.replay(pojo);

		Session session = dao.getSession(true);

		Assert.assertNotNull(session);
	}

	
	
	@Test
	public void testFindById() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		Serializable serializable = hibernate.getSerializableMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();
		IPojo ipojo = dao.findById(IPojo.class, serializable);
		Assert.assertNotNull(ipojo);

	}

	
	
	@Test
	public void testInsert() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		dao.insert(professor);
	}

	@Test
	public void testRemove() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		dao.remove(professor);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testMakeTransiente() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		dao.makeTransiente(professor);
	}

	@Test
	public void testUpdate() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		dao.update(professor);
	}

	@Test
	public void testFindHQLString() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		dao.findHQL(" * from teacher");
	}

	@Test
	public void testFindHQLStringPage() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		dao.findHQL(" * from teacher", hibernate.getPageMock());
	}

	@Test
	public void testFindString() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		dao.find("select * from teacher");
	}

	@Test
	public void testFindStringPage() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		assertNotNull(dao);

		dao.find("select * from teacher", hibernate.getPageMock());
	}

	@Test
	public void testFindCriteriaCriteria() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teacher");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		dao.findCriteria(hibernate.getCriteriaMock());
	}

	@Test
	public void testFindCriteriaCriteriaPage() {
		ArrayList<IPojo> array = new ArrayList<IPojo>();

		Professor professor = new Professor();
		professor.setNome("teache");

		HibernateMock hibernate = new HibernateMock();
		hibernate.configure(array);

		ITransactionContext context = hibernate.getContextMock();

		ContextLocator.getInstance().setTransactionContext(context);

		HibernateGenericDAO<IPojo> dao = new HibernateGenericDAO<IPojo>();

		dao.findCriteria(hibernate.getCriteriaMock(), hibernate.getPageMock());
	}
	
}