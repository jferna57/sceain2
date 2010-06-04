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
package br.gov.framework.demoiselle.util.layer.integration;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.layer.integration.Factory;
import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;

/**
 * Superclass intended to be inherited to manage field injections annotated with
 * {@link Injection}.
 *
 * @author CETEC/CTCTA
 */
public class GenericInjectionManager {

	private static Logger log = Logger.getLogger(GenericInjectionManager.class);
	private static Map<Class<?>, IFactory<?>> injectionMap = new LinkedHashMap<Class<?>, IFactory<?>>();

	private static final String INJECTION_MAP_FILE = "META-INF/demoiselle/injection-map.properties";

	static {

		log.debug("Loading injection map file \"" + INJECTION_MAP_FILE + "\"");

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> resources;
		try {
			resources = loader.getResources(INJECTION_MAP_FILE);
		} catch (Exception e) {
			throw new LayerIntegrationException(
					"Error loading resources " + INJECTION_MAP_FILE, e);
		}

		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();

			log.debug("Loading resource " + url);

			Properties props = new Properties();
			try {
				props.load(url.openStream());
			} catch (Exception e) {
				throw new LayerIntegrationException(
						"Error opening resource " + url, e);
			}

			Enumeration<?> fieldTypeNames = props.propertyNames();
			while (fieldTypeNames.hasMoreElements()) {
				String fieldTypeName = (String) fieldTypeNames.nextElement();
				String factoryName = props.getProperty(fieldTypeName);

				Class<?> iface;
				IFactory<?> factory;
				try {
					iface = Class.forName(fieldTypeName);
					factory = IFactory.class.cast(Class.forName(factoryName).newInstance());
				} catch (Exception e) {
					throw new LayerIntegrationException(
							"Invalid map " + fieldTypeName + "=" + factoryName, e);
				}

				if (!iface.isInterface()) {
					throw new LayerIntegrationException(
							iface.getName() + " is not an interface");
				}

				log.debug("Loading injection map " + iface.getName() + "=" +
						factory.getClass().getName());

				injectionMap.put(iface, factory);
			}
		}
	}

	protected GenericInjectionManager() {
	}

	/**
	 * Retrieves the list of attributes signed with @Injection in the hierarchy
	 * tree of the specified object.
	 *
	 * @param object	the object to search for
	 * @return a collection of fields
	 */
	protected Collection<Field> getFieldsToInject(Object object) {

		Collection<Field> fields = new ArrayList<Field>();
		for (Class<?> clazz = object.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(Injection.class)) {
					fields.add(field);
				}
			}
		}

		return fields;
	}

	/**
	 * Searches the factory specified to be directly used or to decide
	 * by convention the default factory.
	 *
	 * @param ctx				the injection context
	 * @return IFactory
	 */
	protected IFactory<?> getFactory(InjectionContext ctx) {
		try {
			if (ctx.getInjection().factory() != IFactory.class) {
				return IFactory.class.cast(ctx.getInjection().factory().newInstance());
			} else if (ctx.getClassType().isAnnotationPresent(Factory.class)) {
				return IFactory.class.cast(ctx.getClassType().
						getAnnotation(Factory.class).factory().newInstance());
			} else if (ctx.getClassType().getPackage().isAnnotationPresent(Factory.class)) {
				return IFactory.class.cast(ctx.getClassType().
						getPackage().getAnnotation(Factory.class).factory().newInstance());
			} else {
				return getDefaultFactory(ctx.getFieldType());
			}
		} catch (Exception e) {
			throw new LayerIntegrationException("Could not create factory", e);
		}
	}

	/**
	 * Returns the default factory according to field type.
	 *
	 * @param fieldType	field type
	 * @return default factory
	 */
	protected IFactory<?> getDefaultFactory(Class<?> fieldType) {
		for (Class<?> searchedInterface : injectionMap.keySet()) {
			if (containsInterface(fieldType, searchedInterface)) {
				return injectionMap.get(searchedInterface);
			}
		}

		throw new LayerIntegrationException(
				"Factory not found for field type " + fieldType.getName());
	}

	/**
	 * Returns whether a class implements a determined interface.
	 *
	 * @param sourceClass the class to check
	 * @param searchedInterface the interface to search for
	 * @return a boolean
	 */
	protected boolean containsInterface(Class<?> sourceClass, Class<?> searchedInterface) {
		return (sourceClass != null
				&& searchedInterface != null
				&& searchedInterface.isInterface()
				&& searchedInterface.isAssignableFrom(sourceClass)
		);
	}

}