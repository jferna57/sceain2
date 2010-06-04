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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

/**
 * @author CETEC/CTJEE
 */
public class WebSecurityContextTest {

	@Test
	public void testGetUserPrincipal() {
		
		Principal principal = createMock(Principal.class);
		replay(principal);

		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getUserPrincipal()).andReturn(principal);
		replay(request);

		WebSecurityContext ctx = WebSecurityContext.getInstance();
		ctx.setRequest(request);
		assertEquals(principal, ctx.getUserPrincipal());
	}

	@Test
	public void testClear() {
		
		Principal principal = createMock(Principal.class);
		replay(principal);

		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getUserPrincipal()).andReturn(principal);
		replay(request);

		WebSecurityContext ctx = WebSecurityContext.getInstance();
		ctx.clear();
	}

	@Test
	public void testGetUserPrincipalNoRequest() {
		
		WebSecurityContext ctx = WebSecurityContext.getInstance();
		ctx.setRequest(null);

		try {
			ctx.getUserPrincipal();
			fail();
		} catch (WebSecurityException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testIsUserInRoleYes() {
		
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.isUserInRole("roleYes")).andReturn(true);
		replay(request);

		WebSecurityContext ctx = WebSecurityContext.getInstance();
		ctx.setRequest(request);
		assertTrue(ctx.isUserInRole("roleYes"));
	}

	@Test
	public void testIsUserInRoleNo() {
		
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.isUserInRole("roleNo")).andReturn(false);
		replay(request);

		WebSecurityContext ctx = WebSecurityContext.getInstance();
		ctx.setRequest(request);
		assertFalse(ctx.isUserInRole("roleNo"));
	}

	@Test
	public void testIsUserInRoleRequestNull() {

		WebSecurityContext ctx = WebSecurityContext.getInstance();
		ctx.setRequest(null);
		
		try {
			ctx.isUserInRole("roleNo");
		} catch (Exception e) {
			assertTrue(e instanceof WebSecurityException);
		}
	}

}