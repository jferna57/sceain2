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
package br.gov.framework.demoiselle.web.layer.integration;

import br.gov.framework.demoiselle.web.layer.integration.InjectionManager;

/**
 * This aspect has the responsibility of intercepting instances of classes that implement
 * IBusinessController, IViewController, and IFacade, invoking the InjectionManager class.
 * 
 * <p>
 * <h1>Basic Knowledge</h1>
 * An <b>aspect</b> defines a specific function that can affect several parts of a system,
 * for example, object sharing. An aspect, as an ordinary Java class, can define members
 * (attributes and methods) and an aspect hierarchy through definition of specialized
 * aspects.
 * </p>
 * 
 * @author CETEC/CTJEE
 */
public aspect InjectionAspect {

	/**
	 * <p>
	 * <b>Pointcuts</b> are formed by composition of join points, through operators
	 * && (and), || (or) and ! (not). By using pointcuts we can get argument value
	 * methods, objects executing, attributes and exceptions of join points. 
	 * </p>
	 * 
	 * <p>
	 * The pointcut designator <b>execution</b> defines a execution of a method or
	 * constructor identified by a signature.      
	 * </p>	  
	 */
	pointcut injectionIBusinessController(): execution(public br.gov.framework.demoiselle.core.layer.IBusinessController+.new(..));
	
	pointcut injectionIViewController(): execution(public br.gov.framework.demoiselle.core.layer.IViewController+.new(..));
	
	pointcut injectionIFacade(): execution(public br.gov.framework.demoiselle.core.layer.IFacade+.new(..));

	pointcut injectionIPersistenceController(): execution(public br.gov.framework.demoiselle.core.layer.IPersistenceController+.new(..));

	pointcut injectionIDAO(): execution(public br.gov.framework.demoiselle.core.layer.IDAO+.new(..));

	/**
	 * Additional code that must be executed before specified pointcuts.
	 */
	before(): injectionIBusinessController() || injectionIViewController() || injectionIFacade() || injectionIPersistenceController() || injectionIDAO() {
		InjectionManager manager = new InjectionManager();
		manager.execute(thisJoinPoint.getTarget());
	}

}
