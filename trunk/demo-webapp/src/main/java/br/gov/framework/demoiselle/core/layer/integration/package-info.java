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

/**
 * <p>
 * This package makes use of design patterns such as 
 * <b>Factory</b>, <b>Proxy</b>, <b>IoC</b> 
 * and dependency injection to
 * maintain layer integration at low coupling level
 * to ensure a better maintenance and writing/readability
 * of classes that represent each layer.
 * </p>
 * <p>
 * Integration mechanism between layers will act in 
 * view layer injecting business objects through a
 * factory of framework or another factory defined
 * by application. This factory may make use a
 * a proxy, of framework or application, to instance
 * the business object.
 * </p>
 * <p>
 * Integration mechanism between layers will act also
 * on business rule layer, injecting persistence objects
 * through a factory of framework or another defined
 * by application. This factory may make use a
 * proxy, of framework or application, to instance
 * the persistence object.
 * </p> 
 * 
 * @author CETEC/CTJEE
 */
package br.gov.framework.demoiselle.core.layer.integration;