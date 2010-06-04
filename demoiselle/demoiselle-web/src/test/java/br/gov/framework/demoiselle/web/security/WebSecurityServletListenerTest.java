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
package br.gov.framework.demoiselle.web.security;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.web.security.WebSecurityContext;
import br.gov.framework.demoiselle.web.security.WebSecurityException;
import br.gov.framework.demoiselle.web.security.WebSecurityServletRequestListener;

/**
 * @author CETEC/CTJEE
 */
public class WebSecurityServletListenerTest {
	
	private WebSecurityServletRequestListener listener;

	@Before
	public void setUp() throws Exception {
		listener = new WebSecurityServletRequestListener();
	}

	@After
	public void tearDown() throws Exception {
		listener.requestDestroyed(null);
	}

	@Test
	public void testRequestInitializedWithHttpServletRequest() {
		
		ServletContext ctx = createMock(ServletContext.class);
		replay(ctx);
		
		Principal principal = createMock(Principal.class);
		replay(principal);
		
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getUserPrincipal()).andReturn(principal);
		replay(request);
		
		ServletRequestEvent sre = new ServletRequestEvent(ctx, request);
		
		WebSecurityServletRequestListener listener = new WebSecurityServletRequestListener();
		listener.requestInitialized(sre);
		
		assertEquals(principal, WebSecurityContext.getInstance().getUserPrincipal());
	}

	@Test
	public void testRequestInitializedWithServletRequest() {
		
		ServletContext ctx = createMock(ServletContext.class);
		replay(ctx);
		
		ServletRequest request = createMock(ServletRequest.class);
		replay(request);
		
		ServletRequestEvent sre = new ServletRequestEvent(ctx, request);
		
		WebSecurityServletRequestListener listener = new WebSecurityServletRequestListener();
		listener.requestInitialized(sre);

		try {
			WebSecurityContext.getInstance().getUserPrincipal();
			fail();
		} catch (WebSecurityException e) {
			assertTrue(true);
		}
	}

}
