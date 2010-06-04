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
package br.gov.framework.demoiselle.view.faces.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import br.gov.framework.demoiselle.view.faces.util.ManagedBeanUtilStub;

/**
 * Unitary tests concerning AbstractManagedBean class.
 * 
 * @author CETEC/CTJEE
 * @see AbstractManagedBean
 */
public class AbstractManagedBeanTest {
	
	private class MyBean extends AbstractManagedBean {
	}

	private MyBean bean = new MyBean();

	@Test
	public void testLogout() {
		try {
			ManagedBeanUtilStub.configure();
			bean.logout();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetMaxPages() {
		assertEquals(new Integer(15), bean.getMaxPages());
	}

	@Test
	public void testGetRows() {
		assertEquals(new Integer(5), bean.getRows());
	}

	@Test
	public void testSetContext() {
		ServletContext context = EasyMock.createMock(ServletContext.class);
		bean.setContext(context);
		assertEquals(context, bean.context);
	}

}