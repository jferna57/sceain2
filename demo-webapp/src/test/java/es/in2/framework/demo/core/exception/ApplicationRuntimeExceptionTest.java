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
package es.in2.framework.demo.core.exception;

import java.util.ArrayList;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.in2.framework.demo.core.exception.ApplicationRuntimeException;
import es.in2.framework.demo.core.message.IMessage;


/**
 * @author CETEC/CTJEE
 */
public class ApplicationRuntimeExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testApplicationRuntimeExceptionIMessage() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test").anyTimes();
		EasyMock.replay(messageMock);
		try {
			throw new ApplicationRuntimeException(messageMock);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
		}
	}

	@Test
	public void testApplicationRuntimeExceptionIMessageObjectArray() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test {0} {1} {2}").anyTimes();
		EasyMock.replay(messageMock);
		try {
			throw new ApplicationRuntimeException(messageMock, "String 1", "String 2", "String 3");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
			Assert.assertEquals("Test String 1 String 2 String 3", e.getMessage());
		}
	}

	@Test
	public void testApplicationRuntimeExceptionIMessageThrowable() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test").anyTimes();
		Throwable cause = EasyMock.createMock(Throwable.class);
		EasyMock.replay(messageMock);
		EasyMock.replay(cause);
		try {
			throw new ApplicationRuntimeException(messageMock, cause);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
			Assert.assertEquals(cause, e.getCause());
		}
	}

	@Test
	public void testApplicationRuntimeExceptionIMessageThrowableObjectArray() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test").anyTimes();
		Throwable cause = EasyMock.createMock(Throwable.class);
		EasyMock.replay(messageMock);
		EasyMock.replay(cause);
		try {
			ArrayList<Throwable> array = new ArrayList<Throwable>();
			array.add(cause);
			throw new ApplicationRuntimeException(messageMock, array);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
		}
	}

	@Test
	public void testApplicationRuntimeExceptionIMessageThrowableStringArray() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test {0} {1} {2}").anyTimes();
		Throwable cause = EasyMock.createMock(Throwable.class);
		EasyMock.replay(messageMock);
		EasyMock.replay(cause);
		try {
			throw new ApplicationRuntimeException(messageMock, cause,
					"String 1", "String 2", "String 3");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
			Assert.assertEquals(cause, e.getCause());
			Assert.assertEquals("Test String 1 String 2 String 3", e.getMessage());
		}
	}

	@Test
	public void testGetMessage() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test").anyTimes();
		EasyMock.replay(messageMock);
		try {
			throw new ApplicationRuntimeException(messageMock);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
			ApplicationRuntimeException applicationRuntimeExcepion = (ApplicationRuntimeException) e;
			String message = applicationRuntimeExcepion.getMessage();
			Assert.assertEquals("Test", message);
		}
	}

	@Test
	public void testGetObjectMessage() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test").anyTimes();
		EasyMock.replay(messageMock);
		try {
			throw new ApplicationRuntimeException(messageMock);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
			ApplicationRuntimeException applicationRuntimeExcepion = (ApplicationRuntimeException) e;
			IMessage message = applicationRuntimeExcepion.getObjectMessage();
			Assert.assertEquals(message, messageMock);
		}
	}

	@Test
	public void testGetArgs() {
		IMessage messageMock = EasyMock.createMock(IMessage.class);
		EasyMock.expect(messageMock.getLabel()).andReturn("Test").anyTimes();
		EasyMock.replay(messageMock);
		try {
			throw new ApplicationRuntimeException(messageMock,"String 1");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ApplicationRuntimeException);
			ApplicationRuntimeException applicationRuntimeExcepion = (ApplicationRuntimeException) e;
			Object[] object = applicationRuntimeExcepion.getArguments();
			Assert.assertNotNull(object);
			Assert.assertEquals(object[0], "String 1");
		}
	}

}
