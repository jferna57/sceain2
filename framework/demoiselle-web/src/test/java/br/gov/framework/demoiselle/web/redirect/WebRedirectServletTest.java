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
package br.gov.framework.demoiselle.web.redirect;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.action.ILoaderAction;
import br.gov.framework.demoiselle.web.redirect.IRedirectAction;
import br.gov.framework.demoiselle.web.redirect.WebRedirectServlet;

/**
 * @author CETEC/CTJEE
 */
public class WebRedirectServletTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testdoGet() {
		
		HttpServletRequest req = createMock(HttpServletRequest.class);
		expect(req.getParameter("param1")).andReturn("value1");
		expect(req.getParameter("param2")).andReturn("valueWrong");
		replay(req);

		HttpServletResponse res = createMock(HttpServletResponse.class);
		replay(res);

		IRedirectAction action1 = createMock(IRedirectAction.class);
		expect(action1.getParameter()).andReturn("param1");
		expect(action1.getValue()).andReturn("value1");
		action1.setRequest(req);
		action1.setResponse(res);
		action1.execute();
		replay(action1);

		IRedirectAction action2 = createMock(IRedirectAction.class);
		expect(action2.getParameter()).andReturn("param2");
		expect(action2.getValue()).andReturn("value2");
		replay(action2);

		Collection<IRedirectAction> actions = new ArrayList<IRedirectAction>();
		actions.add(action1);
		actions.add(action2);

		ILoaderAction<IRedirectAction> loader = createMock(ILoaderAction.class);
		expect(loader.getActions()).andReturn(actions);
		replay(loader);

		WebRedirectServlet servlet = new WebRedirectServlet();
		servlet.setLoader(loader);
		try {
			servlet.doGet(req, res);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		verify(action1);
		verify(action2);
		verify(loader);
		verify(req);
		verify(res);
	}

	@Test
	public void testInit() {

		ServletConfig config = EasyMock.createMock(ServletConfig.class);
		expect(config.getInitParameter("REDIRECT_LOADER_ACTION_CLASS"))
				.andReturn("br.gov.framework.demoiselle.web.redirect.LoaderStub");
		replay(config);

		WebRedirectServlet servlet = new WebRedirectServlet();
		try {
			servlet.init(config);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testInitErroActionClassParameter() {

		ServletConfig config = EasyMock.createMock(ServletConfig.class);
		expect(config.getInitParameter("REDIRECT_LOADER_ACTION_CLASS"))
				.andReturn("");
		replay(config);

		WebRedirectServlet servlet = new WebRedirectServlet();
		try {
			servlet.init(config);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testInitErrorInvalidLoader() {

		ServletConfig config = EasyMock.createMock(ServletConfig.class);
		expect(config.getInitParameter("REDIRECT_LOADER_ACTION_CLASS"))
				.andReturn("InvalidClass");
		replay(config);

		WebRedirectServlet servlet = new WebRedirectServlet();
		try {
			servlet.init(config);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof WebRedirectException);
		}
	}

}
