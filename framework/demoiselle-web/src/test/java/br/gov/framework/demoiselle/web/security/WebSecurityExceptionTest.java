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
package br.gov.framework.demoiselle.web.security;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author CETEC/CTJEE
 */
public class WebSecurityExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWebSecurityException() {
		try {
			throw new WebSecurityException();
		} catch (Exception e) {
			assertTrue(e instanceof WebSecurityException);
		}
	}

	@Test
	public void testWebSecurityExceptionString() {
		try {
			throw new WebSecurityException("Teste");
		} catch (Exception e) {
			assertEquals("Teste", e.getMessage());
		}
	}

	@Test
	public void testWebSecurityExceptionStringThrowable() {
		try {
			Throwable cause = createMock(Throwable.class);
			replay(cause);
			throw new WebSecurityException("Teste", cause);
		} catch (Exception e) {
			assertEquals("Teste", e.getMessage());
		}
	}

}
