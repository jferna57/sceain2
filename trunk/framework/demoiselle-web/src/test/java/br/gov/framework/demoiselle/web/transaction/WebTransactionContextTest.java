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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import br.gov.framework.demoiselle.core.transaction.ITransactionResource;
import br.gov.framework.demoiselle.core.transaction.TransactionType;
import br.gov.framework.demoiselle.web.transaction.WebTransactionContext;

/**
 * @author CETEC/CTJEE
 */
public class WebTransactionContextTest {

	@Test
	public void testCommitWithLocal() {
		
		ITransactionResource resource = createMock(ITransactionResource.class);
		resource.commit();
		replay(resource);

		WebTransactionContext.getInstance().setType(TransactionType.LOCAL);
		WebTransactionContext.getInstance().init();
		WebTransactionContext.getInstance().init();
		WebTransactionContext.getInstance().setResource(resource);
		WebTransactionContext.getInstance().end();
		WebTransactionContext.getInstance().end();

		verify(resource);
	}

	@Test
	public void testRollbackWithLocal() {
		
		ITransactionResource resource = createMock(ITransactionResource.class);
		resource.rollback();
		replay(resource);

		WebTransactionContext.getInstance().setType(TransactionType.LOCAL);
		WebTransactionContext.getInstance().init();
		WebTransactionContext.getInstance().init();
		WebTransactionContext.getInstance().setResource(resource);
		WebTransactionContext.getInstance().end();
		WebTransactionContext.getInstance().add(new RuntimeException("Test Error"));
		WebTransactionContext.getInstance().end();

		verify(resource);
	}

}
