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
package br.gov.framework.demoiselle.web.init;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author CETEC/CTJEE
 */
public class WebInitializationExceptionTest {

	@Test
	public void testWebInitializationException() {
		try {
			throw new WebInitializationException();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof WebInitializationException);
		}
	}

	@Test
	public void testWebInitializationExceptionString() {
		try {
			throw new WebInitializationException("Any Message");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof WebInitializationException);
			Assert.assertEquals("Any Message", e.getMessage());
		}
	}

	@Test
	public void testWebInitializationExceptionStringThrowable() {
		Throwable cause = EasyMock.createMock(Throwable.class);
		EasyMock.replay(cause);
		try {
			throw new WebInitializationException("Any Message", cause);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof WebInitializationException);
			Assert.assertEquals("Any Message", e.getMessage());
			Assert.assertEquals(cause, e.getCause());
		}
	}

}
