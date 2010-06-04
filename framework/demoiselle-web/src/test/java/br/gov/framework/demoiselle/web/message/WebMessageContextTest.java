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
package br.gov.framework.demoiselle.web.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.core.message.Severity;

/**
 * @author CETEC/CTJEE
 */
public class WebMessageContextTest {
	
	private IMessage message;

	@Before
	public void setUp() throws Exception {
		message = new IMessage() {
			public String getKey() {
				return "ERROR_0001";
			}
			public String getLabel() {
				return "Error Message (Argument:{0})";
			}
			public Locale getLocale() {
				return Locale.US;
			}
			public String getResourceName() {
				return "error";
			}
			public Severity getSeverity() {
				return Severity.ERROR;
			}
			public String toString() {
				return this.getKey()+":"+this.getLabel();
			}
		};
	}
	
	@Test
	public void testAddMessage() {
		WebMessageContext.getInstance().addMessage(message);
		assertTrue("The message can not be found.",WebMessageContext.getInstance().getMessages().contains(message));
	}
		
	@Test
	public void testClear() {
		WebMessageContext.getInstance().addMessage(message);
		WebMessageContext.getInstance().clear();
		assertEquals(0, WebMessageContext.getInstance().getMessages().size());
	}

	@Test
	public void testGetMessages() {
		WebMessageContext.getInstance().clear();
		WebMessageContext.getInstance().addMessage(message);
		assertEquals(1, WebMessageContext.getInstance().getMessages().size());
	}
	
	@Test
	public void testGetMessagesWithArgument() {
		WebMessageContext.getInstance().clear();
		WebMessageContext.getInstance().addMessage(message, "replace001");
		IMessage msg = WebMessageContext.getInstance().getMessages().iterator().next();
		assertEquals("Error Message (Argument:replace001)", msg.getLabel());
	}

	@Test
	public void testGetMessagesWithArgumentAndInternationalization() {
		WebMessageContext.getInstance().clear();
		WebMessageContext.getInstance().addMessage(message, "replace001");
		IMessage msg = WebMessageContext.getInstance().getMessages(new Locale("pt_BR")).iterator().next();
		assertEquals("Mensagem de Erro (Argumento:replace001)", msg.getLabel());
	}

}
