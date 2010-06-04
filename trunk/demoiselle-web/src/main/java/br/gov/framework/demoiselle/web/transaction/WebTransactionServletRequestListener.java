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

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;

/**
 * <p>
 * A ServletRequestListener that can be used to control {@link WebTransactionContext}.
 * </p>
 * 
 * <p>
 * in WEB-INF/web.xml file:
 * </p>  
 * <pre>
 *     &lt;listener&gt;
 *         &lt;listener-class&gt;
 *             br.gov.framework.demoiselle.web.transaction.WebTransactionServletRequestListener
 *         &lt;/listener-class&gt;
 *     &lt;/listener&gt;
 * </pre>
 * 
 * 
 * Um {@link ServletRequestListener} é executado em todas as requisições feitas ao servidor de aplicações.
 * No retorno, pode ser encontrada um {@link Throwable} não capturado durante a requisição e então,
 * o contexto transacional deverá 'desfazer' as alterações caso encontre uma exceção, caso contrário,
 * o contexto será finalizado normalmente, confirmando as alteraçõoes do recurso do contexto transacional.
 * 
 * @author CETEC/CTJEE
 */
public class WebTransactionServletRequestListener implements ServletRequestListener {
	
	private static Logger log = Logger.getLogger(WebTransactionServletRequestListener.class);

	public void requestInitialized(ServletRequestEvent event) {
		log.debug("Transaction: ServletRequest initialized");
		WebTransactionContext.getInstance();
		ContextLocator.getInstance().getTransactionContext().init();
	}
	
	public void requestDestroyed(ServletRequestEvent event) {
		log.debug("Transaction: ServletRequest destroyed");
		String key = "javax.servlet.error.exception";
		ServletRequest request = event.getServletRequest();
		Object error = request.getAttribute(key);
		if (error != null) {
			ContextLocator.getInstance().getTransactionContext().add((Throwable)error);
		}
		ContextLocator.getInstance().getTransactionContext().end();
	}

}
