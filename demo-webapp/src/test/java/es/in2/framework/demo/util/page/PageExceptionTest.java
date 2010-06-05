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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

/**
 * @author CETEC/CTJEE
 */
public class PageExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPageException() {
		try {
			throw new PageException();
		} catch (Exception e) {
			assertTrue(e instanceof PageException);
		}
	}

	@Test
	public void testPageExceptionString() {
		try {
			throw new PageException("Teste");
		} catch (Exception e) {
			assertEquals("Teste", e.getMessage());
		}
	}

	@Test
	public void testPageExceptionStringThrowable() {
		Throwable cause = createMock(Throwable.class);
		try {
			replay(cause);
			throw new PageException("Teste", cause);
		} catch (Exception e) {
			assertEquals("Teste", e.getMessage());
			assertEquals(cause, e.getCause());
		}
	}

}
