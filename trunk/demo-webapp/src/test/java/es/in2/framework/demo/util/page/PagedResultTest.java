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
package es.in2.framework.demo.util.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.util.page.Page;
import es.in2.framework.demo.util.page.PageException;
import es.in2.framework.demo.util.page.PagedResult;


/**
 * Unitary tests concerning PagedResult class.
 * 
 * @author CETEC/CTJEE
 * @see PagedResult
 */
public class PagedResultTest {

	private Page page;
	private Collection<Object> results;
	
	@Before
	public void setUp() throws Exception {
		page = new Page(2);
		results = new ArrayList<Object>();
		results.add(new Object());
		results.add(new Object());
	}

	@Test
	public void testGetPage() {
		PagedResult<Object> pagedResult = new PagedResult<Object>(page, 5L, results);
		assertEquals(page, pagedResult.getPage());
	}

	@Test
	public void testGetTotalResults() {
		PagedResult<Object> pagedResult = new PagedResult<Object>(page, 5L, results);
		assertEquals(new Long(5L), pagedResult.getTotalResults());
	}

	@Test
	public void testGetResults() {
		PagedResult<Object> pagedResult = new PagedResult<Object>(page, 5L, results);
		assertEquals(results, pagedResult.getResults());
	}
	
	@Test
	public void testCreateWithMoreResults() {
		results.add(new Object());
		try {
			new PagedResult<Object>(page, 5L, results);
			fail();
		} catch (PageException e) {
			assertTrue(true);
		}
	}

}
