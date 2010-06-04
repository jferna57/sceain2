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
package br.gov.framework.demoiselle.web.transaction;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;

/**
 * @author CETEC/CTJEE
 */
public class WebTransactionServletRequestListenerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRequestInitialized() {
	}

	@Test
	public void testRequestDestroyed() {

		WebTransactionServletRequestListener web = new WebTransactionServletRequestListener();

		ServletRequestEvent event = EasyMock
				.createMock(ServletRequestEvent.class);

		ServletRequest request = EasyMock.createMock(ServletRequest.class);
		EasyMock.expect(request.getParameter(EasyMock.isA(String.class)))
				.andReturn("parameter").anyTimes();
		EasyMock.expect(request.getAttribute(EasyMock.isA(String.class)))
				.andReturn(new Throwable()).anyTimes();
		EasyMock.replay(request);

		EasyMock.expect(event.getServletRequest()).andReturn(request)
				.anyTimes();
		EasyMock.replay(event);

		ITransactionContext context = EasyMock
				.createMock(ITransactionContext.class);

		context.end();
		context.add(EasyMock.isA(Throwable.class));
		context.init();
		EasyMock.replay(context);

		ContextLocator.getInstance().setTransactionContext(context);
		web.requestInitialized(event);
		web.requestDestroyed(event);
	}

}
