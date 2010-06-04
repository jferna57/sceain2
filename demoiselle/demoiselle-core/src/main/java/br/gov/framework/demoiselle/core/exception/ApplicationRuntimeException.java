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
package br.gov.framework.demoiselle.core.exception;

import java.text.MessageFormat;

import br.gov.framework.demoiselle.core.message.IMessage;

/**
 * The goal of this class is defining a standard exception for application.
 * This exception uses a standard message implementing {@link IMessage} to make easy its handling by framework modules.
 * @author CETEC/CTJEE
 * @see IMessage  
 */
public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private IMessage objectMessage;
	private Object[] arguments;	

	/**
	 * Wraps the implementation of <@link IMessage>.
	 * @param objectMessage message 
	 */
	public ApplicationRuntimeException(IMessage objectMessage) {
		super(objectMessage.getLabel());
		this.objectMessage = objectMessage;
	}
	
	/**
	 * Allows to pass in a variable number of Object type arguments.
	 * @param objectMessage message 
	 * @param arguments arguments to interpolate message
	 */
	public ApplicationRuntimeException(IMessage objectMessage, Object... arguments) {
		super(objectMessage.getLabel());
		this.objectMessage = objectMessage;
		this.arguments = arguments;
	}
	
	/**
	 * Allows to pass in an instance of <@link Throwable> with the reason for the error. 
	 * @param objectMessage message
	 * @param cause throwable cause
	 */
	public ApplicationRuntimeException(IMessage objectMessage, Throwable cause) {
		super(objectMessage.getLabel(), cause);
		this.objectMessage = objectMessage;
	}

	/**
	 * Allows to pass in an instance of <@link Throwable> that identify the cause of exception.
	 * In addition, it allows to pass in a variable number of arguments.
	 * @param objectMessage message
	 * @param cause throwable cause
	 * @param arguments arguments to interpolate message
	 */
	public ApplicationRuntimeException(IMessage objectMessage, Throwable cause, Object... arguments) {
		super(objectMessage.getLabel(), cause);
		this.objectMessage = objectMessage;
		this.arguments = arguments;
	}

	/**
	 * Get object message  
	 * @return object message
	 */
	public IMessage getObjectMessage() {
		return this.objectMessage;
	}
	
	/**
	 * Get arguments
	 * @return list of arguments
	 */
	public Object[] getArguments() {
		return this.arguments;
	}
	
	/**
	 * <p>
	 * Returns the detail message string of this throwable. 
	 * If some defined argument exists, the message will be  
	 * interpolated with arguments in the order in which 
	 * they were passed.
	 * </p>
	 * <p>
	 * For example, the message may contain
	 * terms {0}, {1}, {2}, etc. Each argument will be interpolated
	 * according to its position into array.  
	 * 
	 * @return the detail message string
	 */
	@Override
	public String getMessage() {
		if (arguments == null) {
			return super.getMessage();
		} else {
			MessageFormat formatter = new MessageFormat("");
			formatter.applyPattern(objectMessage.getLabel());
			return formatter.format(arguments);
		}
	}
	
}
 
