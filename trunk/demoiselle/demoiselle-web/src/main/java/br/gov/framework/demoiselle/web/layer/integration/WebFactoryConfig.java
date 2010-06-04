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
package br.gov.framework.demoiselle.web.layer.integration;

import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigKey;
import br.gov.framework.demoiselle.util.config.ConfigType;
import br.gov.framework.demoiselle.util.config.IConfig;

/**
 * Represents configuration for WebBusinessControllerFactory and WebDAOFactory classes.
 * 
 * @author CETEC/CTJEE
 * @see WebBusinessControllerFactory
 * @see WebPersistenceControllerFactory
 * @see WebDAOFactory
 */
public class WebFactoryConfig implements IConfig {

	private static final long serialVersionUID = 1L;

	@ConfigKey(name = "framework.demoiselle.web.injection.regex", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] regex;

	@ConfigKey(name = "framework.demoiselle.web.injection.replace", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] replace;

	@ConfigKey(name = "framework.demoiselle.web.injection.business.regex", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] regexBusiness;

	@ConfigKey(name = "framework.demoiselle.web.injection.business.replace", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] replaceBusiness;

	@ConfigKey(name = "framework.demoiselle.web.injection.dao.regex", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] regexDAO;

	@ConfigKey(name = "framework.demoiselle.web.injection.dao.replace", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] replaceDAO;
	
	@ConfigKey(name = "framework.demoiselle.web.injection.persistence.regex", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] regexPersistence;

	@ConfigKey(name = "framework.demoiselle.web.injection.persistence.replace", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] replacePersistence;

	public String[] getRegex() {
		if (regex == null || regex.length == 0) {
			regex = new String[] { "\\.I" };
		}
		return regex;
	}

	public String[] getReplace() {
		if (replace == null || replace.length == 0) {
			replace = new String[] { ".implementation." };
		}
		return replace;
	}

	public String[] getRegexBusiness() {
		return regexBusiness;
	}

	public String[] getReplaceBusiness() {
		return replaceBusiness;
	}

	public String[] getRegexDAO() {
		return regexDAO;
	}

	public String[] getReplaceDAO() {
		return replaceDAO;
	}

	public String[] getRegexPersistence() {
		return regexPersistence;
	}

	public String[] getReplacePersistence() {
		return replacePersistence;
	}

	protected boolean hasBusiness() {
		return hasElement(this.regexBusiness) && hasElement(this.replaceBusiness);
	}

	protected boolean hasDAO() {
		return hasElement(this.regexDAO) && hasElement(this.replaceDAO);
	}
	
	protected boolean hasPersistence() {
		return hasElement(this.regexPersistence) && hasElement(this.replacePersistence);
	}
	
	protected boolean hasDefault() {
		return hasElement(this.regex) && hasElement(this.replace);
	}

	private boolean hasElement(String[] element) {
		return (element != null && element.length > 0);
	}

}
