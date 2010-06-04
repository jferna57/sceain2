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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;

/**
 * Class intended to initialize and call methods on lazy proxy classes.
 * 
 * @author CETEC/CTJEE
 */
public class LazyCreateProxy implements InvocationHandler {

	private static Logger log = Logger.getLogger(LazyCreateProxy.class);

	private Class<?> clazz;
	private Object object;
	private Class<?>[] parameterTypes;
	private Object[] initargs;

	/**
	 * Creates a proxy for the specified class type using its default constructor.
	 * 
	 * @param clazz	the class to create
	 */
	public LazyCreateProxy(Class<?> clazz) {
		log.debug("Creating proxy to class " + clazz.getName());
		this.clazz = clazz;
	}

	/**
	 * Creates a proxy for the specified class type using its a constructor with the given parameter types and argument values.
	 * 
	 * @param clazz	the class to create
	 * @param parameterTypes	the types on the constructor
	 * @param initargs			the argument values for the constructor
	 */
	public LazyCreateProxy(Class<?> clazz, Class<?>[] parameterTypes, Object[] initargs) {
		
		this(clazz);
		
		if (log.isDebugEnabled() || log.isTraceEnabled()) {
			String types = null;
			if (parameterTypes != null) {
				for (Object type : parameterTypes) {
					types = (types != null ? types + ", " : "") + type.toString();
				}
				log.debug("Selecting class constructor with parameter types: " + types);
			} else {
				log.debug("Selecting class default constructor");
			}
		}
		
		this.parameterTypes = parameterTypes;
		this.initargs = initargs;
	}

	/*
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.debug("Invoking method " + method.getName());

		if (object == null) {
			log.debug("Creating object of class " + clazz.getName());
			
			if (parameterTypes == null) {
				object = clazz.newInstance();
			} else {
				Constructor<?> ctr = clazz.getConstructor(parameterTypes);
				object = ctr.newInstance(initargs);
			}
		}

		try {
			return method.invoke(object, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Returns the proxy target class type.
	 * 
	 * @return	a Class
	 */
	public Class<?> getInternalClass() {
		return clazz;
	}

	/**
	 * Instanciates an object for the specified class using its default constructor method.
	 * 
	 * @param clazz	the class to create
	 * @return	an Object
	 */
	public static Object newInstance(Class<?> clazz) {
		return newInstance(clazz, null, null);
	}

	/**
	 * Instanciates an object for the specified class using an alternative constructor method using the given parameter types and values.
	 * 
	 * @param clazz	the class to create
	 * @param parameterTypes	the types on the constructor
	 * @param initargs			the argument values for the constructor
	 * @return	an Object
	 */
	public static Object newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object[] initargs) {
		return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new LazyCreateProxy(clazz, parameterTypes, initargs));
	}

}
