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
package br.gov.framework.demoiselle.view.faces.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;
import br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel;

/**
 * @author CETEC/CTJEE
 */
public class PagedResultDataModelTest {

	private Page page;
	private List<Bean> list;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		page = new Page(2,1);
		list = new ArrayList<Bean>();
		list.add(new Bean("name0", "value0", 0));
		list.add(new Bean("name1", "value1", 1));
		list.add(new Bean("name2", "value2", 2));
		list.add(new Bean("name3", "value3", 3));
		list.add(new Bean("name4", "value4", 4));
		list.add(new Bean("name5", "value5", 5));
		list.add(new Bean("name6", "value6", 6));
		list.add(new Bean("name7", "value7", 7));
		list.add(new Bean("name8", "value8", 8));
		list.add(new Bean("name9", "value9", 9));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		list.clear();
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#PagedResultDataModel()}.
	 */
	@Test
	public void testPagedResultDataModel() {
		PagedResultDataModel<Bean> pageResult = new PagedResultDataModel<Bean>();
		assertNotNull(pageResult);		
	}
	
	/**
	 * Test method for {@link PagedResultDataModel#bind(PagedResult)}.
	 */
	@Test
	public void testBind() {		
		PagedResultDataModel<Bean> pageResult = new PagedResultDataModel<Bean>();
		PagedResult<Bean> pg = new PagedResult<Bean>(new Page(50), 1000l, list);
		pageResult.bind(pg);
		
		assertEquals(new Integer(50), pageResult.getPage().getMaxResults());
		assertEquals(1000l, pageResult.getRowCount());
		assertEquals(list, pageResult.getWrappedData());		
	}
	
	/**
	 * Test method for {@link PagedResultDataModel#bind(java.util.Collection, Page)}.
	 */
	@Test
	public void testBindList() {		
		PagedResultDataModel<Bean> pageResult = new PagedResultDataModel<Bean>();
		Collection<Bean> listBean = new ArrayList<Bean>();
		for(int i = 0; i < 100; i ++)
			listBean.add(new Bean("name_" + i, "value_" + i, i));		
		Page page = new Page(50);
		pageResult.bind(listBean, page);
		assertEquals(new Integer(50), pageResult.getPage().getMaxResults());
		assertEquals(100l, pageResult.getRowCount());
		assertEquals(listBean, pageResult.getWrappedData());		
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#PagedResultDataModel(br.gov.framework.demoiselle.util.page.Page, int, java.util.List)}.
	 */
	@Test
	public void testPagedResultDataModelPageIntListOfT() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(new Page(50), 1000, list);
		
		assertNotNull(pageResult);		
		assertEquals(new Integer(50), pageResult.getPage().getMaxResults());
		assertEquals(1000, pageResult.getRowCount());
		assertEquals(list, pageResult.getWrappedData());
	}
	
	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#PagedResultDataModel(Page, long, java.util.Collection)}.
	 */
	@Test
	public void testPagedResultDataModelPageIntListOfTd() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(new PagedResult<Bean>(new Page(50), 1000l, list));
		
		assertNotNull(pageResult);		
		assertEquals(new Integer(50), pageResult.getPage().getMaxResults());
		assertEquals(1000, pageResult.getRowCount());
		assertEquals(list, pageResult.getWrappedData());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#isRowAvailable()}.
	 */
	@Test
	public void testIsRowAvailableTrue() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, 1000, list);
		
		pageResult.setRowIndex(0);		
		assertTrue(pageResult.isRowAvailable());
		pageResult.setRowIndex(list.size()-1);
		assertTrue(pageResult.isRowAvailable());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#isRowAvailable()}.
	 * For empty list
	 */
	@Test
	public void testIsRowAvailableFalseListNull() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, 1000, null);
		
		assertFalse(pageResult.isRowAvailable());
	}
	
	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#isRowAvailable()}.
	 * For empty no list, but index not valid
	 */
	@Test
	public void testIsRowAvailableFalseListNotNull() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, 1000, list);
		
		assertFalse(pageResult.isRowAvailable());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#isRowAvailable()}.
	 * For index out
	 */
	@Test
	public void testIsRowAvailableFalseIndexOutArray() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, 1000, list);
		
		pageResult.setRowIndex(-1);
		assertFalse(pageResult.isRowAvailable());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getRowCount()}.
	 */
	@Test
	public void testGetRowCount() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, 500, null);
		
		assertEquals(500, pageResult.getRowCount());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getRowData()}.
	 */
	@Test
	public void testGetRowData() {
		page = new Page(2,1);
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, list.size(), list);
		
		pageResult.setRowIndex(0);		
		assertEquals("name0", ((Bean)pageResult.getRowData()).name);
		pageResult.setRowIndex(1);		
		assertEquals("name1", ((Bean)pageResult.getRowData()).name);
		pageResult.setRowIndex(2);		
		assertEquals("name0", ((Bean)pageResult.getRowData()).name);
	}
	
	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getRowData()}.
	 */
	@Test
	public void testGetRowDataListNull() {
		page = new Page(2,1);
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(page, list.size(), null);
		
		pageResult.setRowIndex(0);		
		assertTrue(pageResult.getRowData() == null);		
	}
	
	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getRowData()}.
	 */
	@Test
	public void testGetRowDataListIsRowAvailable() {		
		try {
			PagedResultDataModel<Bean> 
				pageResult = new PagedResultDataModel<Bean>(page, list.size(), list);
			
			pageResult.setRowIndex(-1);		
			pageResult.getRowData();
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getRowIndex()}.
	 */
	@Test
	public void testGetRowIndex() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(null, 100, null);
		
		pageResult.setPage(new Page(3));
		
		pageResult.setRowIndex(0);
		assertEquals(0, pageResult.getRowIndex());
		
		pageResult.setRowIndex(1);
		assertEquals(1, pageResult.getRowIndex());
		
		pageResult.setRowIndex(2);
		assertEquals(2, pageResult.getRowIndex());
		
		pageResult.setRowIndex(3);
		assertEquals(0, pageResult.getRowIndex());
		
		pageResult.setRowIndex(4);
		assertEquals(1, pageResult.getRowIndex());
		
		pageResult.setRowIndex(5);
		assertEquals(2, pageResult.getRowIndex());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getWrappedData()}.
	 */
	@Test
	public void testGetWrappedData() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(new Page(50), 1000, list);
		
		assertEquals(list, pageResult.getWrappedData());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#setWrappedData(java.lang.Object)}.
	 */
	@Test
	public void testSetWrappedData() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>();
		
		pageResult.setWrappedData(list);
		assertEquals(list, pageResult.getWrappedData());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#isEmpty()}.
	 */
	@Test
	public void testIsEmptyListNull() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(null, 0, null);
		
		assertTrue(pageResult.isEmpty());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#isEmpty()}.
	 */
	@Test
	public void testIsEmptyListEmpty() {
		list.clear();
		assertTrue(list.isEmpty());
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(null, 0, list);
		
		assertTrue(pageResult.isEmpty());
	}

	/**
	 * Test method for 
	 * {@link br.gov.framework.demoiselle.view.faces.util.PagedResultDataModel#getPage()}.
	 */
	@Test
	public void testGetSetPage() {
		PagedResultDataModel<Bean> 
			pageResult = new PagedResultDataModel<Bean>(null, 0, list);
		
		pageResult.setPage(page);
		assertEquals(page, pageResult.getPage());
	}

	/**
	 * Classe utilitária para realização dos testes
	 */
	class Bean{
		public String name;
		public String value;
		public long number;
		public Bean(String name, String value, long number) {
			super();
			this.name = name;
			this.value = value;
			this.number = number;
		}
	}
	
}