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

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;
import br.gov.framework.demoiselle.util.layer.integration.GenericInjectionManager;

/**
 * Class intended to manage field injections annotated with @Injection.
 * 
 * @author CETEC/CTJEE
 * @see Injection
 */
public class InjectionManager extends GenericInjectionManager {
	
	private static final Logger log = Logger.getLogger(InjectionManager.class);
 
	/**
	 * Delegates the injection function to its specific factory.
	 * 
	 * @param object	the object to search for injections
	 */
	public void execute(Object object) {
		log.debug("Performing field injections on " + object.getClass().getName());

		for (Field field : getFieldsToInject(object)) {
			
			Injection annotation = field.getAnnotation(Injection.class);
			InjectionContext ctx = new InjectionContext(annotation, field.getType(), object.getClass());

			IFactory<?> factory = getFactory(ctx);
			
			try {
				log.debug("Injection on field " + field.getName());
				field.setAccessible(true);
				Object instance = factory.create(ctx);
				field.set(object, field.getType().cast(instance));
			} catch (Exception e) {
				throw new WebLayerIntegrationException(
						"Could not initialize attribute \"" + field.getName() + "\"", e);
			}
		}
	}
	
}