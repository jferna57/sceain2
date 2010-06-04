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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.gov.framework.demoiselle.core.transaction.TransactionType;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.web.transaction.JBossTransactionManagerLookup;
import br.gov.framework.demoiselle.web.transaction.WebTransactionAction;
import br.gov.framework.demoiselle.web.transaction.WebTransactionContext;

/**
 * @author CETEC/CTJEE
 */
public class WebTransactionActionTest {

	@Before
	public void setUp() throws Exception {
		WebTransactionAction action = new WebTransactionAction();
		action.execute();
	}
	
	@Test
	public void testExecuteTransactionContextWithJTA() {
		assertEquals(WebTransactionContext.getInstance().getType(),
				TransactionType.JTA);	
	}
	
	@Test
	public void testExecuteTransactionWithLOCAL() {
		
		WebTransactionActionConfig config = ConfigurationLoader.load(WebTransactionActionConfig.class);
		
		config.setType("LOCAL");
		
		WebTransactionContext context = WebTransactionContext.getInstance();
		
		context.setType(TransactionType.LOCAL);
		
		assertEquals(WebTransactionContext.getInstance().getType(),
				TransactionType.LOCAL);	
	}
	
	@Test
	public void testExecuteWithJBossTransactionManagerLookup() {
		assertEquals(WebTransactionContext.getInstance()
				.getJNDITransactionManagerLookup()
				.getClass().getName(),
				JBossTransactionManagerLookup.class.getName());
	}

}