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
package br.gov.framework.demoiselle.view.faces.util;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.classextension.EasyMock;

/**
 * A stub class for managed beans tests.
 * 
 * @author CETEC/CTJEE
 */
public class ManagedBeanUtilStub {

	@SuppressWarnings("unchecked")
	public static void configure() {

		HttpServletRequest request = EasyMock
				.createNiceMock(HttpServletRequest.class);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		replay(response);
		HttpSession session = EasyMock.createNiceMock(HttpSession.class);

		session.setAttribute("Test", null);
		session.setAttribute("ReportName", null);

		Enumeration<String> en = EasyMock.createMock(Enumeration.class);
		expect(en.hasMoreElements()).andReturn(true).times(2);
		expect(en.hasMoreElements()).andReturn(false).times(1);
		expect(en.nextElement()).andReturn("Test").times(1);
		expect(en.nextElement()).andReturn("ReportName").anyTimes();
		replay(en);
		expect(session.getAttributeNames()).andReturn(en);
		replay(session);

		expect(request.getSession()).andReturn(session).anyTimes();
		expect(request.getParameter("ReportName")).andReturn("reportTest");
		expect(request.getContextPath()).andReturn("/test");
		expect(request.getLocalPort()).andReturn(1212);
		expect(request.getLocalName()).andReturn("0.0.0.0");
		replay(request);
		
		Map map = new HashMap<String, Object>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		ExternalContext externalContext = (ExternalContext) EasyMock.createMock(ExternalContext.class);
		expect(externalContext.getSessionMap()).andReturn(map);
		
		Iterator it = EasyMock.createMock(Iterator.class);
		expect(it.hasNext()).andReturn(true).times(1);
		replay(it);		
		
		EasyMock.replay(new Object[] {externalContext});
		
		ManagedBeanUtil.setRequest(request);
		ManagedBeanUtil.setResponse(response);
		ManagedBeanUtil.setExternalContext(externalContext);
	}

	@SuppressWarnings("unchecked")
	public static void configureReportNameNull() {

		HttpServletRequest request = EasyMock
				.createNiceMock(HttpServletRequest.class);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		replay(response);
		HttpSession session = EasyMock.createNiceMock(HttpSession.class);

		session.setAttribute("Test", null);

		Enumeration<String> en = EasyMock.createMock(Enumeration.class);
		expect(en.hasMoreElements()).andReturn(true).times(1);
		expect(en.hasMoreElements()).andReturn(false).times(1);
		expect(en.nextElement()).andReturn("Test").times(1);

		replay(en);
		expect(session.getAttributeNames()).andReturn(en);
		replay(session);

		expect(request.getSession()).andReturn(session).anyTimes();

		expect(request.getParameter("ReportName")).andReturn(null);
		expect(request.getLocalName()).andReturn("0.0.0.0");
		replay(request);
		ManagedBeanUtil.setRequest(request);
		ManagedBeanUtil.setResponse(response);
	}

	@SuppressWarnings("unchecked")
	public static void configureIOException() {

		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		replay(response);
		HttpSession session = EasyMock.createNiceMock(HttpSession.class);

		session.setAttribute("Test", null);

		Enumeration<String> en = EasyMock.createMock(Enumeration.class);
		expect(en.hasMoreElements()).andReturn(true).times(1);
		expect(en.hasMoreElements()).andReturn(false).times(1);
		expect(en.nextElement()).andReturn("Test").times(1);

		replay(en);
		expect(session.getAttributeNames()).andReturn(en);
		replay(session);

		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
		expect(request.getParameter("ReportName")).andReturn(null).times(1)
				.andThrow(new Exception()).times(1).andReturn("null");

		expect(request.getLocalName()).andReturn("0.0.0.0");
		replay(request);
		ManagedBeanUtil.setRequest(request);
		ManagedBeanUtil.setResponse(response);
	}

}
