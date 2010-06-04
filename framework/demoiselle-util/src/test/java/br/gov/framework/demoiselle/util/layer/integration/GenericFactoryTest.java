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
import static junit.framework.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unitary tests concerning generic factory behavior.
 * 
 * @author CETEC/CTJEE
 * @see GenericFactory
 */
public class GenericFactoryTest {

	private static final int INTEGER_VALUE = 123;
	private static final double DOUBLE_VALUE = 9.731;

	private MockFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new MockFactory();
	}

	@After
	public void tearDown() throws Exception {
		factory = null;
	}

	@Test
	public void testCreateWithLazyCreateProxyClass() {
		
		IMockObject obj = factory.createWithLazyCreateProxyClass();
		assertNotNull(obj);
		assertNull(obj.getName());
		assertEquals(obj.getValue(), 0);
	}

	@Test
	public void testCreateWithLazyCreateProxyClassOneParam() {
		
		IMockObject obj = factory.createWithLazyCreateProxyClassOneParam();
		assertNotNull(obj);
		assertNull(obj.getName());
		assertEquals(obj.getValue(), INTEGER_VALUE);
	}

	@Test
	public void testCreateWithLazyCreateProxyClassTwoParams() {
		
		IMockObject obj = factory.createWithLazyCreateProxyClassTwoParams();
		assertNotNull(obj);
		assertNull(obj.getName());
		assertEquals(obj.getValue(), INTEGER_VALUE);
		assertEquals(obj.getHiddenValue(), DOUBLE_VALUE, 0.1);
	}

	class MockFactory extends GenericFactory {

		public IMockObject createWithLazyCreateProxyClass() {
			return createWithLazyCreateProxy(MockObject.class);
		}
		
		public IMockObject createWithLazyCreateProxyClassOneParam() {
			return createWithLazyCreateProxy(MockObject.class,
					new Class[] { Integer.TYPE },
					new Object[] { INTEGER_VALUE });
		}
		
		public IMockObject createWithLazyCreateProxyClassTwoParams() {
			return createWithLazyCreateProxy(MockObject.class,
					new Class[] { Integer.TYPE, Double.TYPE },
					new Object[] { INTEGER_VALUE, DOUBLE_VALUE });
		}
		
	}
	
}
