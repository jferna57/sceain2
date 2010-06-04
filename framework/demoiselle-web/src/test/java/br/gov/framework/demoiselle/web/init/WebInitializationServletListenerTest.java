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
package br.gov.framework.demoiselle.web.init;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Test;

import br.gov.framework.demoiselle.core.action.ILoaderAction;
import br.gov.framework.demoiselle.web.init.IInitializationAction;
import br.gov.framework.demoiselle.web.init.WebInitializationServletContextListener;

/**
 * @author CETEC/CTJEE
 */
public class WebInitializationServletListenerTest {
	
	@Test
	@SuppressWarnings("unchecked")
	public void testExecuteAllActions() {
		
		IInitializationAction action1 = createMock(IInitializationAction.class);
		action1.setServletContext(null);
		action1.execute();
		replay(action1);

		IInitializationAction action2 = createMock(IInitializationAction.class);
		action2.setServletContext(null);
		action2.execute();
		replay(action2);
		
		Collection<IInitializationAction> 
			actions = new ArrayList<IInitializationAction>(2);
		actions.add(action1);
		actions.add(action2);
		
		ILoaderAction<IInitializationAction> loader = createMock(ILoaderAction.class);
		expect(loader.getActions()).andReturn(actions);
		replay(loader);
		
		WebInitializationServletContextListener 
			listner = new WebInitializationServletContextListener();
		
		listner.setLoader(loader);
		listner.execute();
		
		verify(action1);
		verify(action2);
		verify(loader);
	}
	
	@Test
	public void testContextInitializedWithInitParameterInvalid() {
		
		ServletContext ctx = createMock(ServletContext.class);
		expect(ctx.getInitParameter("INIT_LOADER_ACTION_CLASS"))
			.andReturn("InvalidClassName");
		replay(ctx);
		
		ServletContextEvent evt = new ServletContextEvent(ctx);

		WebInitializationServletContextListener 
			listener = new WebInitializationServletContextListener();
		
		try {
			listener.contextInitialized(evt);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testContextInitializedWithInitParameter() {
		
		ServletContext ctx = createMock(ServletContext.class);
		expect(ctx.getInitParameter("INIT_LOADER_ACTION_CLASS"))
			.andReturn("br.gov.framework.demoiselle.web.init.LoaderStub");
		replay(ctx);
		
		ServletContextEvent evt = new ServletContextEvent(ctx);
		WebInitializationServletContextListener 
			listener = new WebInitializationServletContextListener();
		
		try {
			listener.contextInitialized(evt);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testContextDestroyed() {
		
		ServletContext ctx = createMock(ServletContext.class);
		expect(ctx.getInitParameter("INIT_LOADER_ACTION_CLASS"))
			.andReturn("br.gov.framework.demoiselle.web.init.LoaderStub");
		replay(ctx);
		
		ServletContextEvent evt = new ServletContextEvent(ctx);
		WebInitializationServletContextListener 
			listener = new WebInitializationServletContextListener();
		
		try {
			listener.contextDestroyed(evt);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
