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
package es.in2.framework.demo.util.page;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * Structure used to handle pagination of data results.
 * 
 * @author CETEC/CTJEE
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(Page.class);

	private Integer firstResult;
	private Integer maxResults;
	private Integer pageNumber;

	/**
	 * Class constructor passing number of maximum results.
	 * 
	 * @param maxResults	maximum number of results
	 */
	public Page(Integer maxResults) {
		this.maxResults = maxResults;
		this.pageNumber = 1;
		this.firstResult = 0;
		log.debug("Page initialized with maxResults=" + this.maxResults);
	}

	/**
	 * Class constructor passing number of maximum results and a current page number.
	 * 
	 * @param maxResults	maximum number of results
	 * @param pageNumber	number of page
	 */
	public Page(Integer maxResults, Integer pageNumber) {
		this(maxResults);
		this.pageNumber = pageNumber;
		calculateFirstResult();
		log.debug("Page initialized with maxResults=" + this.maxResults + " and pageNumber=" + pageNumber);
	}

	/**
	 * Retrieves the first result.
	 * 
	 * @return number of first result
	 */
	public Integer getFirstResult() {
		return firstResult;
	}

	/**
	 * Retrieves the maximum number of results.
	 * 
	 * @return maximum number of results
	 */
	public Integer getMaxResults() {
		return maxResults;
	}

	/**
	 * Retrieves the current page number.
	 * 
	 * @return page number
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * Calculates the first result.
	 */
	private void calculateFirstResult() {
		this.firstResult = (pageNumber - 1) * this.maxResults;
		log.debug("First result calculated = " + this.firstResult);
	}

	public String toString() {
		return "Page [pageNumber=" + pageNumber + ", maxResults=" + maxResults + "]";
	}
	
}
