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
package br.gov.framework.demoiselle.util.page;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.gov.framework.demoiselle.util.page.Page;

/**
 * Unitary tests concerning Page class.
 * 
 * @author CETEC/CTJEE
 * @see Page
 */
public class PageTest {

	@Test
	public void testGetMaxResults() {
		Page page = new Page(20);
		assertEquals(new Integer(20), page.getMaxResults());
	}

	@Test
	public void testGetFirstResult() {
		Page page = new Page(20);
		assertEquals(new Integer(0), page.getFirstResult());
	}

	@Test
	public void testGetFirstResultWithPageNumber() {
		Page page = new Page(20, 2);
		assertEquals(new Integer(20), page.getFirstResult());
	}
	
	@Test
	public void testGetPageNumber() {
		Page page = new Page(20, 2);
		assertEquals(new Integer(2), page.getPageNumber());
	}

	@Test
	public void testToString() {
		Page page = new Page(20, 2);
		assertEquals("Page [pageNumber=2, maxResults=20]", page.toString());
	}

}
