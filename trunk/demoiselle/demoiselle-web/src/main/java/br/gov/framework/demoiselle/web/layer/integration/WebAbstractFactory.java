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

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;

/**
 * Abstract class to put together commons implementations for web factories 
 * @author CETEC/CTJEE
 *
 */
@Deprecated
public abstract class WebAbstractFactory {
	
	private static final Logger log = Logger.getLogger(WebAbstractFactory.class);

	public WebAbstractFactory() {
		super();
	}
	
	/**
	 * Extract from InjectionContext the class name to be instantiate by web factories
	 * @param ctx
	 * @param regex
	 * @param repalce
	 * @return
	 */
	protected String getClassNameToInject(InjectionContext ctx, String[] regex, String[] replace) {
		String className = null;
		if (ctx.getInjection() != null && ctx.getInjection().name() != null &&  !ctx.getInjection().name().trim().equals("")) {
			className = ctx.getInjection().name();
		} else {
			if((regex.length != replace.length) || (regex.length == 0)){
				throw new WebLayerIntegrationException("Error: regex has " + regex.length + " and replace has " + replace.length + ". It's incompatible.");
			} else {
				className = ctx.getFieldType().getName();
				for (int i = 0; i < regex.length; i++) {
					className = className.replaceFirst(regex[i], replace[i]);
				}
			}
		}
		log.debug("Class to inject " + className);
		return className;
	}
}