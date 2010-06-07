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
package es.in2.framework.demo.persistence.layer.integration;

import es.in2.framework.demo.util.Constant;
import es.in2.framework.demo.util.config.ConfigKey;
import es.in2.framework.demo.util.config.ConfigType;
import es.in2.framework.demo.util.config.IConfig;

/**
 * Represents configuration for PersistenceEntityManagerFactory.
 * 
 * @author CETEC/CTJEE
 * @see PersistenceEntityManagerFactory
 */
public class PersistenceEntityManagerFactoryConfig implements IConfig {

	private static final long serialVersionUID = 1L;

	@ConfigKey(name = "framework.demoiselle.persistence.default_persistence_unit", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String defaultPersistenceUnit;

	public String getPersistenceUnitName() {
		return defaultPersistenceUnit;
	}

}
