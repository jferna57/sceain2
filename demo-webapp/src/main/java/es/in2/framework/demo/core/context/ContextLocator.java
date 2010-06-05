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
package es.in2.framework.demo.core.context;

import es.in2.framework.demo.core.message.IMessageContext;
import es.in2.framework.demo.core.security.ISecurityContext;
import es.in2.framework.demo.core.transaction.ITransactionContext;

/**
 * Implements a context location mechanism. This locator must be used by each
 * context implementation so that it is made available to other layers.
 * 
 * @author CETEC/CTJEE
 * @see IMessageContext
 * @see ISecurityContext
 * @see ITransactionContext
 */
public class ContextLocator {

	private static ContextLocator instance = new ContextLocator();

	private IMessageContext messageCtx;
	private ISecurityContext securityCtx;
	private ITransactionContext transactionCtx;

	private ContextLocator() {
	}

	/**
	 * Sets the message context on context.
	 * 
	 * @param messageCtx
	 */
	public void setMessageContext(IMessageContext messageCtx) {
		this.messageCtx = messageCtx;
	}

	/**
	 * Sets the security context on context.
	 * 
	 * @param securityCtx
	 */
	public void setSecurityContext(ISecurityContext securityCtx) {
		this.securityCtx = securityCtx;
	}

	/**
	 * Sets the transaction context on context.
	 * 
	 * @param transactionCtx
	 */
	public void setTransactionContext(ITransactionContext transactionCtx) {
		this.transactionCtx = transactionCtx;
	}

	/**
	 * Gets the message context.
	 * 
	 * @return message context
	 */
	public IMessageContext getMessageContext() {
		return messageCtx;
	}

	/**
	 * Gets the security context.
	 * 
	 * @return security context
	 */
	public ISecurityContext getSecurityContext() {
		return securityCtx;
	}

	/**
	 * Gets the transaction context.
	 * 
	 * @return transaction context
	 */
	public ITransactionContext getTransactionContext() {
		return transactionCtx;
	}

	/**
	 * <p>
	 * Retrieves an instance of this class. It is actually an implementation of
	 * the design pattern <b>singleton</b> to ensure that a single instance of
	 * this class is created throughout the Virtual Machine.
	 * </p>
	 * 
	 * <p>
	 * As the class constructor is <b>private</b>, it is not possible to
	 * instantiate objects with <b>new</b> operator.
	 * </p>
	 * 
	 * @return instance of ContextLocator
	 */
	public static ContextLocator getInstance() {
		return instance;
	}

}