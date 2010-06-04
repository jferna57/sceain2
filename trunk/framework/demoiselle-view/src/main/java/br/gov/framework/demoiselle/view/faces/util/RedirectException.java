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

/**
 * <p>
 * This exception must be thrown whenever a developer needs to handle an exception,
 * treat it, rollback the transaction and redirect to a specific page. 
 * </p>
 * <p>
 * This exception will be handled by ExceptionHandlerPhaseListener.
 * Otherwise the developer can throw any {@link java.lang.RuntimeException} that will be handled
 * by {@link br.gov.framework.demoiselle.web.transaction.WebTransactionServletRequestListener}.
 * </p>
 * 
 * @author CETEC/CTJEE
 * @see RuntimeException
 */
public class RedirectException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String targetPage;
	
	/**
	 * Default constructor.
	 */
	public RedirectException() {
    	super();
    }

	/**
	 * Allows to pass in a message.
	 * 
	 * @param message Message
	 */
    public RedirectException(String message) {
    	super(message);
    }

	/**
	 * Allows to pass in an instance of {@link Throwable} with the reason for the error.
	 *  
	 * @param message
	 * @param cause
	 */
    public RedirectException(String message, Throwable cause) {
        super(message, cause);
    }

	/**
	 * Allows to pass in an instance of {@link Throwable} with the reason for the error.
	 * 
	 * @param message
	 * @param cause
	 * @param targetPage
	 */
    public RedirectException(String message, Throwable cause, String targetPage) {
        super(message, cause);
        this.targetPage = targetPage;
    }

    /**
	 * Returns the taget page.
	 * 
     * @return	a String
     */
    public String getTargetPage() {
		return targetPage;
	}

	/**
	 * Sets the taget page.
	 * 
	 * @param targetPage
	 */
	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}

}
