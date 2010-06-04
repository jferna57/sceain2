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
package br.gov.framework.demoiselle.core.bean;

import java.io.Serializable;

/**
 * Entity abstraction used by applications developed with framework.
 * 
 * POJO is an acronym for Plain Old Java Object, but it embodies a concept 
 * that than simpler and less intrusive is the design of a given framework, it is better. 
 * The name is used to emphasize that a given object is not somehow special, 
 * but is an ordinary Java Object. Martin Fowler, Rebecca Parsons and Josh Mackenzie 
 * coined the term POJO in September 2000: "We wondered why people were so against using 
 * regular objects in their systems and concluded that it was because simple objects lacked 
 * a fancy name. So we gave them one, and it's caught on very nicely." 
 * 
 * @author CETEC/CTJEE
 */
public interface IPojo extends Serializable {
 
}
 