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
package br.gov.framework.demoiselle.core.transaction;


/**
 * Transactional context is responsible to delimit beginning and end of a
 * request. It stores transactional resources and performs commit or rollback
 * depending on context state.
 * 
 * @author CETEC/CTJEE
 * @see ITransactionResource
 * @see TransactionType
 */
public interface ITransactionContext {

	/**
	 * Report beginning of an execution. It may be called for each new execution.
	 *  
	 */	
	public void init();

	/** 
	 * Reports end of an execution. It may be called for each execution final.
	 * The last stack execution will cause transactional resource performs commit
	 * or rollback.
	 */
	public void end();
	
	/**
	 * Reports to context that a context exception occurred, so 
	 * transactional resources will perform rollback. 
	 * 
	 * @param cause exception reason
	 */
	public void add(Throwable cause);

	/**
	 * Defines a transactional resource to context. 
	 * 
	 * @param resource transactional resource
	 */
	public void setResource(ITransactionResource resource);

	/**
	 * Get a transactional resource.
	 * 
	 * @return transactional resource
	 */
	public ITransactionResource getResource();

	/**
	 * Defines the context type.
	 * 
	 * @param type context type
	 */
	public void setType(TransactionType type);

	/**
	 * Verifies whether some transactional context was added
	 * to context 
	 * 
	 * @return true if a transactional resource exists in the context
	 */
	public boolean hasResource();
	
}
