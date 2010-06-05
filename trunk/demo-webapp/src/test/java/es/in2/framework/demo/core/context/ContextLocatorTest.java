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
package es.in2.framework.demo.core.context;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.core.context.ContextLocator;
import es.in2.framework.demo.core.message.IMessageContext;
import es.in2.framework.demo.core.security.ISecurityContext;
import es.in2.framework.demo.core.transaction.ITransactionContext;


/**
 * @author CETEC/CTJEE
 */
public class ContextLocatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetMessageContext() {
		IMessageContext messageContextMock = EasyMock
				.createMock(IMessageContext.class);
		EasyMock.replay(messageContextMock);
		ContextLocator.getInstance().setMessageContext(messageContextMock);
		IMessageContext messageContext = ContextLocator.getInstance()
				.getMessageContext();
		Assert.assertEquals(messageContextMock, messageContext);
	}

	@Test
	public void testSetSecurityContext() {
		ISecurityContext securityContextMock = EasyMock
				.createMock(ISecurityContext.class);
		EasyMock.replay(securityContextMock);
		ContextLocator.getInstance().setSecurityContext(securityContextMock);
		ISecurityContext securityContext = ContextLocator.getInstance()
				.getSecurityContext();
		Assert.assertEquals(securityContextMock, securityContext);
	}

	@Test
	public void testSetTransactionContext() {
		ITransactionContext transactionContextMock = EasyMock
				.createMock(ITransactionContext.class);
		EasyMock.replay(transactionContextMock);
		ContextLocator.getInstance().setTransactionContext(
				transactionContextMock);
		ITransactionContext transactionContext = ContextLocator.getInstance()
				.getTransactionContext();
		Assert.assertEquals(transactionContextMock, transactionContext);
	}

	@Test
	public void testGetInstance() {
		Assert.assertNotNull(ContextLocator.getInstance());
	}

}
