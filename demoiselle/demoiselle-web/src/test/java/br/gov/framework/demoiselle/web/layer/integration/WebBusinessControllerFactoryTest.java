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
package br.gov.framework.demoiselle.web.layer.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;

import app.business.IUseCaseBusinessController;
import app.business.implementation.UseCaseBusinessController;
import br.gov.framework.demoiselle.core.layer.IBusinessController;
import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;
import br.gov.framework.demoiselle.util.layer.integration.LazyCreateProxy;

/**
 * WebBusinessControllerFactory injection test
 * 
 * @author CETEC/CTJEE
 */
public class WebBusinessControllerFactoryTest {

	IFactory<IBusinessController> bcFactory;
	
	/**
	 * Instantiate a factory
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		bcFactory = new WebBusinessControllerFactory();
	}

	/**
	 * This test verify if the class instantiated by WebBusinessControllerFactory is the class expected
	 * by convetion name. In this test the given interface is "IUseCaseBusinessController" and the 
	 * expected class to be instanciated is, by convention, "implementation.UseCaseBusinessController".
	 */
	@Test
	public void testCreate() {
		InjectionContext ctx = new InjectionContext(null, IUseCaseBusinessController.class, Object.class);
		IBusinessController bc = bcFactory.create(ctx);
		
		if (Proxy.isProxyClass(bc.getClass())) {
			assertEquals(UseCaseBusinessController.class, ((LazyCreateProxy) Proxy.getInvocationHandler(bc)).getInternalClass());
		} else {
			assertEquals(UseCaseBusinessController.class, bc.getClass());
		}
	}
	
	/**
	 * This test verify if the class instantiated by WebBusinessControllerFactory is the class expected
	 * with the name given by the programmer. In this test the given name is "UseCaseBusinessController" and the 
	 * expected class to be instanciated is "UseCaseBusinessController".
	 */
	@Test
	public void testCreateWithName() throws Exception {
		
		Injection annotation = new Injection() {
			public Class<? extends Annotation> annotationType() {
				return Injection.class;
			}
			public String[] parameters() {
				return new String[]{""};
			}
			public String name() {
				return "app.business.implementation.UseCaseBusinessController";
			}
			@SuppressWarnings("unchecked")
			public Class<? extends IFactory> factory() {
				return IFactory.class;
			}
		};
		
		InjectionContext ctx = new InjectionContext(annotation, IUseCaseBusinessController.class, Object.class);
		IBusinessController bc = bcFactory.create(ctx);
		
		if (Proxy.isProxyClass(bc.getClass())) {
			assertEquals(UseCaseBusinessController.class, ((LazyCreateProxy) Proxy.getInvocationHandler(bc)).getInternalClass());
		} else {
			assertEquals(UseCaseBusinessController.class, bc.getClass());
		}
	}
	
	/**
	 * This test verify if the WebBusinessControllerFactory throws an exception is the class expected
	 * if the name given by the programmer was wrong or doesn't exists.
	 */
	@Test
	public void testCreateWithNameFalse() throws Exception {
		
		Injection annotation = new Injection() {
			public Class<? extends Annotation> annotationType() {
				return Injection.class;
			}
			public String[] parameters() {
				return new String[]{""};
			}
			public String name() {
				return "WrongUseCaseBusinessController";
			}
			@SuppressWarnings("unchecked")
			public Class<? extends IFactory> factory() {
				return IFactory.class;
			}
		};

		InjectionContext ctx = new InjectionContext(annotation, IUseCaseBusinessController.class, Object.class);
		try {
			bcFactory.create(ctx);
			fail();
		} catch (WebLayerIntegrationException e) {
			assertTrue(true);
		}
	}
	
}
