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
 * <b>Transactional control mechanism specification</b>.
 * <p>
 * This package defines a transaction context that acts
 * from the start to finish of each action executed
 * by each layer artifacts. Its working depends on
 * a defined type, whether it is local or JTA.
 * </p>
 * <p>
 * Local type indicates that implementation
 * is responsible to manage the transaction.
 * </p>
 * <p> 
 * For JTA type, specification will need a
 * available JTA implementation.
 * </p>
 * 
 * @author CETEC/CTJEE
 */
package es.in2.framework.demo.core.transaction;