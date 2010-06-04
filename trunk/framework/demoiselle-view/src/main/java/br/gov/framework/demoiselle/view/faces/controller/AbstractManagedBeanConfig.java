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
package br.gov.framework.demoiselle.view.faces.controller;

import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigKey;
import br.gov.framework.demoiselle.util.config.ConfigType;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.util.config.IConfig;

/**
 * Represents configuration for AbstractManagedBean.
 * 
 * @author CETEC/CTJEE
 * @see IConfig
 * @see AbstractManagedBean
 */
public class AbstractManagedBeanConfig implements IConfig {

	private static final long serialVersionUID = 1L;

	private static AbstractManagedBeanConfig instance;

	@ConfigKey(name = "framework.demoiselle.view.faces.datascroller.maxPages", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String maxPages;

	@ConfigKey(name = "framework.demoiselle.view.faces.datascroller.rows", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String rows;

	/**
	 * Class constructor (private).
	 */
	private AbstractManagedBeanConfig() {
		super();
	}

	/**
	 * Returns the single instance of this configuration class.
	 * 
	 * @return	AbstractManagedBeanConfig
	 */
	public static synchronized AbstractManagedBeanConfig getInstance() {
		if (instance == null) {
			instance = new AbstractManagedBeanConfig();
			ConfigurationLoader.load(instance);
		}
		return instance;
	}
	
	/**
	 * Returns the maximum number of pages, whose default value is 10.
	 * 
	 * @return Number of pages
	 */
	public Integer getMaxPages() {
		if (maxPages == null || maxPages.equals(""))
			return 10;
		return Integer.valueOf(maxPages);
	}

	/**
	 * Returns the number of records per page, whose default value is 50.
	 * 
	 * @return Number of records
	 */
	public Integer getRows() {
		if (rows == null || rows.equals(""))
			return 50;
		return Integer.valueOf(rows);
	}

}
