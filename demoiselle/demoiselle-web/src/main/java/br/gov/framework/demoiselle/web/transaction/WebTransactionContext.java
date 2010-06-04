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

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;
import br.gov.framework.demoiselle.core.transaction.ITransactionResource;
import br.gov.framework.demoiselle.core.transaction.TransactionType;

/**
 * Implementation of framework Demoiselle transactional context 
 * This implementation controls only a transactional resource and
 * this resource needs to implement interface {@link ITransactionResource}.
 * 
 * @author CETEC/CTJEE
 * @see ITransactionContext
 * @see ITransactionResource
 */
public class WebTransactionContext implements ITransactionContext {
	
	private static Logger log = Logger.getLogger(WebTransactionContext.class);
	
	private static final WebTransactionContext instance = new WebTransactionContext();
	
	private IJNDITransactionManagerLookup lookup;
	private TransactionType type;
	private ThreadLocal<Integer> stack = new ThreadLocal<Integer>();
	private ThreadLocal<ITransactionResource> resource = new ThreadLocal<ITransactionResource>();
	private ThreadLocal<Throwable> throwable = new ThreadLocal<Throwable>();
	
	/**
	 * Construtor privado que inicializa o contexto transacional
	 * com os valores de configuração padrão.
	 * Também indica ao localizador de contextos do framework a sua própria instancia.
	 */
	private WebTransactionContext() {
		setType(TransactionType.LOCAL);
		ContextLocator.getInstance().setTransactionContext(this);
		log.debug("Transaction Context Created");
	}
	
	IJNDITransactionManagerLookup getJNDITransactionManagerLookup() {
		return lookup;
	}
	
	public void setJNDITransactionManagerLookup(IJNDITransactionManagerLookup lookup) {
		this.lookup = lookup;
		log.debug("JNDI Transaction Manager Lookup Class "+lookup.getClass().getName());
	}
	
	TransactionType getType() {
		return type;
	}
	 
	public void setType(TransactionType type) {
		this.type = type;
		log.debug("Transaction Type: "+type);
	}

	/**
	 * Inicializa o controle transacional.
	 */
	public void init() {
		if (stack.get() == null) {
			stack.set(0);
		} else {
			stack.set(stack.get()+1);
		}
		log.debug("Init ("+stack.get()+ ")");

		switch (type) {
		case LOCAL:
			break;
		case JTA:
			String jndi;
			if (lookup == null) {
				jndi = IJNDITransactionManagerLookup.DEFAULT_USER_TRANSACTION_NAME;
			} else {
				jndi = lookup.getUserTransactionName();
			}
			try {
				InitialContext ctx = new InitialContext();
				UserTransaction tx = (UserTransaction) ctx.lookup(jndi);
				tx.begin();
			} catch (Exception e) {
				throw new WebTransactionException("ERROR: Lookup JTA Transaction in jndi:"+jndi, e);
			}
			break;
		default:
			throw new WebTransactionException("Transaction type "+type+" not implemented");
		}
	}
	
	/**
	 * Confirmação das alterações no recurso do contexto.
	 * Caso o controle transacional seja JTA, será delegado à uma {@link UserTransaction} a tarefa
	 * de confirmar as alterações do recurso.
	 */
	public void end() {
		log.debug("End ("+stack.get() +")");

		switch (type) {
		case LOCAL:
			if (stack.get() == 0) {
				try {
					if (resource.get() != null) {
						if (throwable.get() == null) {
							resource.get().commit();
							log.debug("Commit Local Transaction Resource");
						} else {
							resource.get().rollback();
							log.debug("Rollback Local Transaction");
						}
					}
				} finally {
					clear();
				}
			} else {
				stack.set(stack.get() -1);
			}
			break;
		case JTA:
			if (stack.get() == 0) {
				clear();
				String jndi;
				if (lookup == null) {
					jndi = IJNDITransactionManagerLookup.DEFAULT_USER_TRANSACTION_NAME;
				} else {
					jndi = lookup.getUserTransactionName();
				}				
				try {
					if (throwable.get() == null) {
						InitialContext ctx = new InitialContext();
						UserTransaction tx = (UserTransaction) ctx.lookup(jndi);
						tx.commit();
						log.debug("Commit JTA Transaction");
					} else {					
						InitialContext ctx = new InitialContext();
						UserTransaction tx = (UserTransaction) ctx.lookup(jndi);
						tx.rollback();
						log.debug("Rollback JTA Transaction");
					}
				} catch (Exception e) {
					throw new WebTransactionException("ERROR: Lookup JTA Transaction in jndi:"+jndi, e);
				}
			} else {
				stack.set(stack.get() -1);
			}
			break;
		default:
			throw new WebTransactionException("Transaction type "+type+" not implemented");
		}
	}
	 
	/**
	 * Desfazer as alterações no recurso do contexto.
	 * Caso o controle transacional seja JTA, será delegado à uma {@link UserTransaction} a tarefa
	 * de desfazer as alterações do recurso.
	 */
	public void add(Throwable cause) {
		log.debug("Add Throwable in Context ");
		throwable.set(cause);
	}
	 
	public void setResource(ITransactionResource resource) {
		this.resource.set(resource);
		log.debug("Set Resource "+resource.getClass().getName());
	}
	 
	public ITransactionResource getResource() {
		return this.resource.get();
	}

	public boolean hasResource() {
		return this.resource.get() != null;
	}
	
	public void clear() {
		stack.set(null);
		resource.set(null);
		throwable.set(null);
	}

	public static WebTransactionContext getInstance() {
		return instance;
	}
	
}
 
