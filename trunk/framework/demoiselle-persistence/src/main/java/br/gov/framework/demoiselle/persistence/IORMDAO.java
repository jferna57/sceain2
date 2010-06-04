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
package br.gov.framework.demoiselle.persistence;

import java.io.Serializable;
import java.util.Collection;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.layer.IDAO;

/**
 * Interface that defines the methods to be implemented
 * by DAO specializations for ORM solutions.
 * 
 * @author CETEC/CTJEE
 *
 * @param <A>	a POJO class type
 * @see IDAO
 * @see IPojo
 */
public interface IORMDAO<A extends IPojo> extends IDAO<A> {
 
	/**
	 * The goal of this method is getting a collection of  
	 * objects that meet the search criteria established by
	 * attributes of A class instance.
	 * Assuming, for example, pojo is an instance of class
	 * Student, and its attribute birthDate is equal to 
	 * 11/17/1999, the findByExample implementation should
	 * be retrieve all objects that have the same value for 
	 * the same attribute.    
	 * @param pojo
	 * @return Collection of pojo
	 */
	public abstract Collection<A> findByExample(A pojo);
	
	/**
	 * <p>Searchs the POJO object with the key value.</p>
	 * <p>Consider using {@link #findById(Serializable)} method instead.</p>
	 * 
	 * @param pojoClass Class of pojo representation
	 * @param id Key of pojo object persistent
	 * @return Pojo persist
	 */
	public abstract A findById(Class<A> pojoClass, Serializable id);

	/**
	 * Searches an object through its key value.
	 * 
	 * @param id	the key to be used in the search
	 * @return	the actual object
	 */
	public abstract A findById(Serializable id);

}