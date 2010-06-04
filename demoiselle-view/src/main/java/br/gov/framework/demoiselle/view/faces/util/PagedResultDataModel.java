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
package br.gov.framework.demoiselle.view.faces.util;

import java.util.Collection;
import java.util.List;

import javax.faces.model.DataModel;

import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

/** 
 * Class responsible for pagination in the view layer.
 * 
 * @param <T> object type that is contained in the collection
 * @author CETEC/CTJEE 
 * @see DataModel 
 */
public class PagedResultDataModel<T> extends DataModel {

	private int rowIndex = -1;	//current row index	
	private int totalNumRows;	//total number of records of the query
	private Page page;			//current page
	private List<T> list;		//object list from the current page

	/**
	 * Default constructor
	 */
	public PagedResultDataModel() {
		super();
	}
	
	/**
	 * Constructor with all fields  
	 * @param page Page requested 
	 * @param totalNumRows Total number of records of the query
	 * @param list List of objects of the current page
	 */
	public PagedResultDataModel(Page page, int totalNumRows, Collection<T> list) {
		super();
		setWrappedData(list);
		this.page = page;
		this.totalNumRows = totalNumRows;
	}

	/**
	 * Constructor receiving a pageResult
	 * @param pageResult
	 */
	public PagedResultDataModel(PagedResult<T> pageResult) {
		this(pageResult.getPage(), pageResult.getTotalResults().intValue(), pageResult.getResults());
	}	

	/**
	 * 	Informs that the line current is accessible
	 * @return true if accessible, false if not accessible
	 */
	public boolean isRowAvailable() {
		if (list == null)
			return false;
		int rowIndex = getRowIndex();
		if (rowIndex >= 0 && rowIndex < list.size())
			return true;
		else
			return false;
	}
	
	/**
	 * It binds to the PagedResult to Date Model
	 * @param pageResult
	 */
	public void bind(PagedResult<T> pageResult){
		setWrappedData(pageResult.getResults());
		this.page = pageResult.getPage();
		this.totalNumRows = pageResult.getTotalResults().intValue();
	}
	
	/**
	 * It binds to the List to Date Model
	 * @param list
	 */
	public void bind(Collection<T> list, Page page){
		setWrappedData(list);
		this.page = page;
		this.totalNumRows = list.size();
	}

	/**
	 * 	Returns the total number of records of the query
	 * @return 	Total number of records
	 */
	@Override
	public int getRowCount() {
		return totalNumRows;
	}

	/**
	 * Returns the current line of the page
	 * @return current line of the page
	 */
	public Object getRowData() {
		if (list == null)
			return null;
		else if (!isRowAvailable())
			throw new IllegalArgumentException();
		else {
			int dataIndex = getRowIndex();
			return list.get(dataIndex);
		}
	}

	/**
	 * Returns the index of current row
	 * @return index of current row
	 */
	public int getRowIndex() {
		return (rowIndex % page.getMaxResults());
	}

	/**
	 * Set the index of current row
	 * @param rowIndex index of current row
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * Returns a list of objects from the current page
	 * @return collection of objects
	 */
	public Object getWrappedData() {
		return list;
	}

	/**
	 * Set a list of objects from the current page
	 * @param list of objects
	 */
	@SuppressWarnings("unchecked")
	public void setWrappedData(Object list) {
		this.list = (List<T>) list;
	}

	/**
	 * Determine if the list is empty
	 * @return true if empty our false if not empty
	 */
	public boolean isEmpty() {
		return (list == null || list.isEmpty());
	}

	/**
	 * Returns the current page
	 * @return current page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Set the current page
	 * @param page current page
	 */
	public void setPage(Page page) {
		this.page = page;
	}

}
