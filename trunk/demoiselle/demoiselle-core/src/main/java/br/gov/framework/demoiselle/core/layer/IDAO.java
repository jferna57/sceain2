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
package br.gov.framework.demoiselle.core.layer;

/**
 * Represents the persistence layer object responsible for access to data. IDAO classes don't access other layers of
 * framework, only the persistence layer.
 * 
 * @author CETEC/CTJEE
 */
public interface IDAO<E> {

	/**
	 * Verifies that there is an element in the storage.
	 * 
	 * @param pojo specific implementation of IDAO.
	 * @return true if pojo exists
	 */
	public boolean exists(E pojo);

	/**
	 * Creates a new element in the storage. May be, for example, a record in a database table.
	 * 
	 * @param pojo specific implementation of IDAO.
	 * @return pojo
	 */
	public Object insert(E pojo);

	/**
	 * Removes a element of the storage. The element may be a record in a database table.
	 * 
	 * @param pojo
	 */
	public void remove(E pojo);

	/**
	 * Brings up to date a element of the storage. The element may be a record in a database table.
	 * 
	 * @param pojo
	 *            specific implementation of IDAO.
	 */
	public void update(E pojo);

}
