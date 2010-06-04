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
package br.gov.framework.demoiselle.util.layer.integration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unitary tests concerning lazy proxy manager class LazyCreateProxy.
 * 
 * @author CETEC/CTJEE
 * @see LazyCreateProxy
 */
public class LazyCreateProxyTest {

	@Test
	public void testCreateProxy() {
		
		IMockObject mock = (IMockObject) LazyCreateProxy.newInstance(MockObject.class);
		
		assertEquals(0, mock.getValue());
	}

	@Test
	public void testCreateProxyWithParams() {
		
		IMockObject mock = (IMockObject) LazyCreateProxy.newInstance(
				MockObject.class, new Class<?>[] { Integer.TYPE },
				new Object[] { 1 });
		assertEquals(1, mock.getValue());

		IMockObject mock2 = (IMockObject) LazyCreateProxy.newInstance(
				MockObject.class, new Class<?>[] { Integer.TYPE, Double.TYPE },
				new Object[] { 123, 3.1415 });
		assertEquals(123, mock2.getValue());
		assertEquals(3.1415, mock2.getHiddenValue(), 0.1);
		
		IMockObject mock3 = (IMockObject) LazyCreateProxy.newInstance(
				MockObject.class, new Class<?>[] { String.class },
				new Object[] { "my name" });
		assertEquals("MY NAME", mock3.getName());
	}

	@Test
	public void testThrowsException() {
		
		IMockObject mock = (IMockObject) LazyCreateProxy.newInstance(MockObject.class);

		try {
			mock.myMethodException();
		} catch (FileNotFoundException e) {
			Assert.assertNotNull(e);
		} catch (Exception e) {
			Assert.fail("Exception modified: " + e.getClass().getName());
		}
	}

	@Test
	public void testGetInternalClass() {
		
		LazyCreateProxy proxy = new LazyCreateProxy(IMockObject.class);
		
		assertNotNull(proxy);
		assertEquals(proxy.getInternalClass(), IMockObject.class);
	}

	@Test
	public void testInvoke() {

		IMockObject mock = (IMockObject) LazyCreateProxy.newInstance(MockObject.class);
		assertNotNull(mock);
		
		mock.setValue(515);
		assertEquals(515, mock.getValue());
	}

}
