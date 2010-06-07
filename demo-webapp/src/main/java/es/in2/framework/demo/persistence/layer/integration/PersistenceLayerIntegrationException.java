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
package es.in2.framework.demo.persistence.layer.integration;

/**
 * Exception class used by persistence layer integration components.
 * 
 * @author CETEC/CTJEE
 */
public class PersistenceLayerIntegrationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public PersistenceLayerIntegrationException() {
		super();
	}

    /**
     * Constructor with message.
     * 
     * @param message string message
     */
	public PersistenceLayerIntegrationException(String message) {
		super(message);
	}

    /**
     * Constructor with message and cause.
     * 
     * @param message string message
     * @param cause exception cause
     */
	public PersistenceLayerIntegrationException(String message, Throwable cause) {
		super(message, cause);
	}

}
