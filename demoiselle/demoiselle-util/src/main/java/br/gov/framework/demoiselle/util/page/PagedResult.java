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
package br.gov.framework.demoiselle.util.page;

import java.io.Serializable;
import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * Structure intended to hold a collection of data results in a pagination manner.
 * 
 * @author CETEC/CTJEE
 * @see Page
 */
public class PagedResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Page page;
	private Long totalResults;
	private Collection<T> results;
	
	private static final Logger log = Logger.getLogger(PagedResult.class);
	
	/**
	 * Constructor for paged result.
	 * 
	 * @param page page configuration
	 * @param totalResults total of results per page
	 * @param results to be paginated
	 */
	public PagedResult(Page page, Long totalResults, Collection<T> results) {
		
		if (results != null && page != null) {
			if (results.size() > page.getMaxResults()) {
				throw new PageException("Results size (" + results.size()
						+ ") is greater than maximum results for page ("
						+ page.getMaxResults() + ").");
			}
		}
		
		this.page = page;
		this.totalResults = totalResults;
		this.results = results;
		
		log.debug("PagedResult created with total results " + this.totalResults +
				" and paginated with " + (results != null ? results.size() : 0) + " items.");
	}

	/**
	 * Returns the page structure.
	 * 
	 * @return Page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Returns the quantity of results.
	 * 
	 * @return number total of results
	 */
	public Long getTotalResults() {
		return totalResults;
	}

	/**
	 * Retrieves the collection results.
	 * 
	 * @return a collection of paged results
	 */
	public Collection<T> getResults() {
		return results;
	}

}
 
