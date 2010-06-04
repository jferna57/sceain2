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

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shale.test.base.AbstractViewControllerTestCase;
import org.apache.shale.test.mock.MockFacesContext;
import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.message.Severity;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;

/**
 * Unitary tests concerning ManagedBeanUtil class.
 * 
 * @author CETEC/CTJEE
 * @see ManagedBeanUtil
 */
public class ManagedBeanUtilTest extends AbstractViewControllerTestCase {

	public ManagedBeanUtilTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBeanExpressionFromValueBidingExpression() {
		
		String result = ManagedBeanUtil.getBeanExpressionFromValueBidingExpression("teste1.teste2");
		assertEquals("teste1}", result);

		result = ManagedBeanUtil.getBeanExpressionFromValueBidingExpression("teste1");
		assertEquals("teste1", result);

		result = ManagedBeanUtil.getBeanExpressionFromValueBidingExpression(null);
		assertNull(result);
	}

	@Test
	public void testGetAttributeOrMethodNameFromValueBidingExpression() {
		
		String result = null;		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression(".a");
		assertEquals("a", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("a.b");
		assertEquals("b", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("a.");
		assertEquals("", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("a.b.c");
		assertEquals("c", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("a.b.");
		assertEquals("", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression(".a.b");
		assertEquals("b", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression(".a.b.");
		assertEquals("", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("#{a.b}");
		assertEquals("b", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("#{a.b.}");
		assertEquals("", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("teste.metodo()");
		assertEquals("metodo()", result);
		
		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("#{teste.metodo()}");
		assertEquals("metodo()", result);

		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("teste.subteste.metodo()");
		assertEquals("metodo()", result);

		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression("metodo()");
		assertNull(result);

		result = ManagedBeanUtil.getAttributeOrMethodNameFromValueBidingExpression(null);
		assertNull(result);
	}

	@Test
	public void testGetSeverity() {

		Severity severity = Severity.INFO;
		javax.faces.application.FacesMessage.Severity severityFaceMessage = 
			ManagedBeanUtil.getSeverity(severity);
		assertEquals(FacesMessage.SEVERITY_INFO, severityFaceMessage);

		severity = Severity.WARNING;
		severityFaceMessage = ManagedBeanUtil.getSeverity(severity);
		assertEquals(FacesMessage.SEVERITY_WARN, severityFaceMessage);

		severity = Severity.FATAL;
		severityFaceMessage = ManagedBeanUtil.getSeverity(severity);
		assertEquals(FacesMessage.SEVERITY_FATAL, severityFaceMessage);

		severity = Severity.ERROR;
		severityFaceMessage = ManagedBeanUtil.getSeverity(severity);
		assertEquals(FacesMessage.SEVERITY_ERROR, severityFaceMessage);

		severity = Severity.ERROR;
		severityFaceMessage = ManagedBeanUtil.getSeverity(severity);
		FacesMessage faceMessage = createMock(FacesMessage.class);
		replay(faceMessage);

		Assert.assertNotSame(faceMessage, severityFaceMessage);
	}

	@Test
	public void testAddMessages() {

		List<FacesMessage> list = new ArrayList<FacesMessage>();

		FacesMessage faces = createMock(FacesMessage.class);
		EasyMock.expect(faces.getDetail()).andReturn("Test");
		EasyMock.expect(faces.getSummary()).andReturn("Test");
		EasyMock.expect(faces.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(faces);

		list.add(faces);
		ManagedBeanUtil.addMessages(list);
	}

//	@Test
//	public void testAddMessagesListOfIMessage() {
//		
//		IMessage message = EasyMock.createMock(IMessage.class);
//		EasyMock.expect(message.getKey()).andReturn("framework.demoiselle.view.faces.datascroller.rows");
//		EasyMock.expect(message.getResourceName()).andReturn("demoiselle");
//		EasyMock.expect(message.getLabel()).andReturn("Test");
//		EasyMock.expect(message.getSeverity()).andReturn(Severity.INFO);
//		EasyMock.expect(message.getLocale()).andReturn(new Locale("pt","BR"));
//		replay(message);
//		
//		List<IMessage> list = new ArrayList<IMessage>();
//		list.add(message);
//		
//		ManagedBeanUtil.addMessages(list);
//	}

//	@Test
//	public void testAddMessageIMessage() {
//
//		IMessage message = EasyMock.createMock(IMessage.class);
//		EasyMock.expect(message.getKey()).andReturn("framework.demoiselle.view.faces.datascroller.rows");
//		EasyMock.expect(message.getResourceName()).andReturn("demoiselle");
//		EasyMock.expect(message.getLabel()).andReturn("Test");
//		EasyMock.expect(message.getSeverity()).andReturn(Severity.INFO);
//		EasyMock.expect(message.getLocale()).andReturn(new Locale("pt","BR"));
//		replay(message);
//
//		ManagedBeanUtil.addMessage(message);
//	}

	@Test
	public void testAddMessageFacesMessage() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage(message);
	}

	@Test
	public void testAddMessageFacesMessageThrowable() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		Throwable cause = EasyMock.createMock(Throwable.class);
		replay(cause);
		
		ITransactionContext transactionCtx = EasyMock.createMock(ITransactionContext.class);
		transactionCtx.add(cause);
		replay(transactionCtx);
		
		ContextLocator.getInstance().setTransactionContext(transactionCtx);
		
		ManagedBeanUtil.addMessage(message, cause);
	}

	@Test
	public void testAddMessageStringStringStringSeverity() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage("String", "String", "String", FacesMessage.SEVERITY_INFO);
	}

	@Test
	public void testAddMessageString() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage("String", "String", "String", FacesMessage.SEVERITY_INFO);
	}

	@Test
	public void testAddMessageStringSeverity() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage("String", FacesMessage.SEVERITY_INFO);
	}

	@Test
	public void testAddMessageStringString() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage("String", "String");
	}

	@Test
	public void testAddMessageStringStringSeverity() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage("String", "String", FacesMessage.SEVERITY_INFO);
	}

	@Test
	public void testAddMessageStringStringString() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		ManagedBeanUtil.addMessage("String", "String", "String");
	}

	@Test
	public void testAddMessageStringStringStringSeverityThrowable() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		Throwable cause = EasyMock.createMock(Throwable.class);
		replay(cause);
		
		ITransactionContext transactionCtx = EasyMock.createMock(ITransactionContext.class);
		transactionCtx.add(cause);
		replay(transactionCtx);
		
		ContextLocator.getInstance().setTransactionContext(transactionCtx);
		
		ManagedBeanUtil.addMessage("String", "String", "String", FacesMessage.SEVERITY_WARN, cause);
	}

	@Test
	public void testGetSession() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		HttpSession session = ManagedBeanUtil.getSession();
		Assert.assertNotNull(session);
	}

	@Test
	public void testGetServletContext() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_FATAL);
		replay(message);

		ServletContext servlet = ManagedBeanUtil.getServletContext();
		Assert.assertNotNull(servlet);
	}

	@Test
	public void testGetRequest() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		HttpServletRequest request = ManagedBeanUtil.getRequest();
		Assert.assertNotNull(request);
	}

	@Test
	public void testSetRequest() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(requestMock);

		ManagedBeanUtil.setRequest(requestMock);

		HttpServletRequest request = ManagedBeanUtil.getRequest();

		Assert.assertNotNull(request);
	}

	@Test
	public void testGetRemoteIP() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);

		EasyMock.expect(requestMock.getRemoteAddr()).andReturn("10.0.0.0");

		EasyMock.replay(requestMock);

		ManagedBeanUtil.setRequest(requestMock);

		String ip = ManagedBeanUtil.getRemoteIP();

		Assert.assertEquals("10.0.0.0", ip);
	}

	@Test
	public void testSetResponse() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		HttpServletResponse responseMock = EasyMock.createMock(HttpServletResponse.class);

		EasyMock.replay(responseMock);

		ManagedBeanUtil.setResponse(responseMock);

		HttpServletResponse response = ManagedBeanUtil.getResponse();

		Assert.assertEquals(responseMock, response);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetManagedBean() {
		
		FacesMessage message = EasyMock.createMock(FacesMessage.class);
		EasyMock.expect(message.getDetail()).andReturn("Test");
		EasyMock.expect(message.getSummary()).andReturn("Summary");
		EasyMock.expect(message.getSeverity()).andReturn(FacesMessage.SEVERITY_INFO);
		replay(message);

		MockFacesContext.getCurrentInstance().getApplication()
				.createValueBinding("nome").setValue(
						MockFacesContext.getCurrentInstance(), new Object());

		Object object = ManagedBeanUtil.getManagedBean("nome");

		Assert.assertNotNull(object);

	}

	@Test
	public void testGetFullUrl() {

		ManagedBeanUtilStub.configure();

		String result = ManagedBeanUtil.getFullUrl("/teste", true, true);

		assertEquals("http://0.0.0.0:1212/teste", result);
	}

	@Test
	public void testGetProtocol() {

		ManagedBeanUtilStub.configure();

		String result = ManagedBeanUtil.getProtocol();

		assertEquals("http://", result);
	}

	@Test
	public void testGetFullUrlWithContextPath() {

		ManagedBeanUtilStub.configure();

		String result = ManagedBeanUtil.getFullUrlWithContextPath("/teste", true, true);

		assertEquals("http://localhost:1212/test/teste", result);
	}

}
