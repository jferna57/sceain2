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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.layer.IFacade;
import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;

/**
 * @author CETEC/CTJEE
 * @see GenericInjectionManager
 */
public class GenericInjectionManagerTest {

	private MockManager manager;

	@Before
	public void setUp() throws Exception {
		manager = new MockManager();
	}

	@After
	public void tearDown() throws Exception {
		manager = null;
	}

	@Test
	public void testGetFieldsToInject() {

		MockObject obj = new MockObject();
		Collection<Field> fields = manager.getFieldsToInject(obj);

		Assert.assertNotNull(fields);
		Assert.assertEquals(6, fields.size());

		for (Field field : fields) {
			Assert.assertEquals(1, field.getName().length());
		}
	}

	@Test
	public void testGetFactory() {

		MockObject obj = new MockObject();

		for (Field field : manager.getFieldsToInject(obj)) {

			Injection annotation = field.getAnnotation(Injection.class);
			InjectionContext context = new InjectionContext(annotation,
					field.getType(), obj.getClass());

			IFactory<?> factory = manager.getFactory(context);
			Assert.assertNotNull(factory);

			Class<?> clazz = factory.getClass();
			String name = field.getName();

			if (name.matches("[abc]")) {
				Assert.assertEquals(MockFactory.class, clazz);
			} else {
				Assert.assertEquals(MockFactory2.class, clazz);
			}
		}
	}

	@Test
	public void testGetDefaultFactory() {

		Assert.assertEquals(MockFactory.class, manager.getDefaultFactory(IMockType.class).getClass());
		Assert.assertEquals(MockFactory.class, manager.getDefaultFactory(IMockType2.class).getClass());

		try {
			manager.getDefaultFactory(IFacade.class);
			Assert.fail("Could not reach this line!");
		} catch (LayerIntegrationException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testContainsInterface() {

		Assert.assertTrue(manager.containsInterface(MockObject.class, IFacade.class));
		Assert.assertTrue(manager.containsInterface(MockObject.class, Serializable.class));

		Assert.assertFalse(manager.containsInterface(MockObject.class, Comparable.class));
		Assert.assertFalse(manager.containsInterface(MockObject.class, List.class));
	}

	@SuppressWarnings({ "unused", "serial" })
	private class SuperMockObject implements Serializable {

		@Injection
		private IMockType b;

		@Injection(factory = MockFactory2.class)
		private IMockType d;

	}

	@SuppressWarnings({ "unused", "serial" })
	private class MockObject extends SuperMockObject implements IFacade {

		@Injection
		private IMockType a;

		@Injection
		private MyUseCaseInterface b;

		@Injection
		private IMockType2 c;

		@Injection(factory = MockFactory2.class)
		private IMockType2 e;
		

	}

	private class MockManager extends GenericInjectionManager {
	}

	private interface MyGenericInterface extends IMockType {
	}

	private interface MyUseCaseInterface extends MyGenericInterface {
	}

}
