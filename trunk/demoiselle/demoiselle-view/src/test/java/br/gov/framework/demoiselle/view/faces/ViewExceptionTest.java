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
package br.gov.framework.demoiselle.view.faces;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author CETEC/CTJEE
 */
public class ViewExceptionTest {

	@Test
	public void testViewException() {
		try {
			throw new ViewException();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ViewException);
		}
	}

	@Test
	public void testViewExceptionString() {
		try {
			throw new ViewException("Test");
		} catch (Exception e) {
			Assert.assertEquals("Test", e.getMessage());
		}
	}

	@Test
	public void testViewExceptionStringThrowable() {
		Throwable cause = EasyMock.createMock(Throwable.class);
		try {
			EasyMock.replay(cause);
			throw new ViewException("Test", cause);
		} catch (Exception e) {
			Assert.assertEquals("Test", e.getMessage());
			Assert.assertEquals(cause, e.getCause());
		}
	}

}
