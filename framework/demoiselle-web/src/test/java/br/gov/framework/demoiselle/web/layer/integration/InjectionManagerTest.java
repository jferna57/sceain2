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

import java.lang.reflect.Proxy;

import org.junit.Test;

import app.business.impl.UseCase4BusinessController;
import app.business.implementation.DerivedUseCaseBusinessController;
import app.business.implementation.UseCase2BusinessController;
import app.business.implementation.UseCase3BusinessController;
import app.business.implementation.UseCaseBusinessController;
import app.persistence.implementation.UseCase2DAO;
import app.persistence.implementation.UseCaseDAO;
import br.gov.framework.demoiselle.util.layer.integration.LayerIntegrationException;
import br.gov.framework.demoiselle.util.layer.integration.LazyCreateProxy;

/**
 * InjectionManager test
 * 
 * @author CETEC/CTJEE
 */
public class InjectionManagerTest {
	
	@Test
	public void testGetFactoryInvalidField() {
		try {
			new InjectionManager().execute(new ObjectStub());
			fail("Invalid Field");
		} catch (LayerIntegrationException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testExecuteNoAnnotation() {
		new InjectionManager().execute(new Object());
	}
	
	/**
	 * Testing if the UseCaseDAO instance was
	 * correctly injected in an annotated field
	 * in UseCaseBusinessController instance
	 */
	@Test
	public void testExecuteWithDAO() {
		UseCaseBusinessController bc = new UseCaseBusinessController();
		new InjectionManager().execute(bc);
		
		if (Proxy.isProxyClass(bc.getDao().getClass())) {
			assertEquals(UseCaseDAO.class, ((LazyCreateProxy) Proxy.getInvocationHandler(bc.getDao())).getInternalClass());
		} else {
			assertEquals(UseCaseDAO.class.getName(), bc.getDao().getClass());
		}
	}
	
	/**
	 * Testing if the IDAO implementation was 
	 * correctly injected in an annotated field
	 * in UseCaseBusinessController instance with
	 * the name given by the programmer
	 */
	@Test
	public void testExecuteWithDAOAndFactoryInField() {
		UseCase2BusinessController bc = new UseCase2BusinessController();
		new InjectionManager().execute(bc);
		
		assertEquals(UseCase2DAO.class, bc.getDao().getClass());
	}
	
	/**
	 * Testing if the factory called was the factory indicated in class annotation
	 */
	@Test
	public void testExecuteWithDAOAndFactoryInClass() {
		UseCase3BusinessController bc = new UseCase3BusinessController();
		new InjectionManager().execute(bc);
		
		assertEquals(UseCase2DAO.class, bc.getDao().getClass());
	}
	
	/**
	 * Testing if the factory called was the factory indicated in package annotation
	 */
	@Test
	public void testExecuteWithDAOAndFactoryInPackage() {
		UseCase4BusinessController bc = new UseCase4BusinessController();
		new InjectionManager().execute(bc);
		
		assertEquals(UseCase2DAO.class, bc.getDao().getClass());
	}
	
	@Test
	public void testExecuteWithBusinessController() {
		UseCaseBusinessController bc = new UseCaseBusinessController();
		new InjectionManager().execute(bc);
		
		if (Proxy.isProxyClass(bc.getBc().getClass())) {
			assertEquals(UseCaseBusinessController.class, ((LazyCreateProxy) Proxy.getInvocationHandler(bc.getBc())).getInternalClass());
		} else {
			assertEquals(UseCaseBusinessController.class, bc.getBc().getClass());
		}
	}
	
	@Test
	public void testExecuteSuperclassInjection() {
		DerivedUseCaseBusinessController bc = new DerivedUseCaseBusinessController();
		new InjectionManager().execute(bc);
		
		if (Proxy.isProxyClass(bc.getBc().getClass())) {
			assertEquals(UseCaseBusinessController.class, ((LazyCreateProxy) Proxy.getInvocationHandler(bc.getBc())).getInternalClass());
		} else {
			assertEquals(UseCaseBusinessController.class, bc.getBc().getClass());
		}
	}
	
}
