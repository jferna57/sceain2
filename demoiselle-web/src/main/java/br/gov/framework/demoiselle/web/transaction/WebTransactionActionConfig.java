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
package br.gov.framework.demoiselle.web.transaction;

import br.gov.framework.demoiselle.core.transaction.TransactionType;
import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigKey;
import br.gov.framework.demoiselle.util.config.ConfigType;
import br.gov.framework.demoiselle.util.config.IConfig;

/**
 * <p>
 * Class that represents required configurations to start 
 * transactional control.
 * Configurations are contained into a framework standard configuration file 
 * defined by constant {@link Constant#FRAMEWORK_CONFIGURATOR_FILE}.
 * Configuration examples:
 * <p>
 * <pre>
 * #Setting JTA mode of transactional control
 * demoiselle.web.transaction.type=JTA
 * </pre>
 * <pre>
 * #Defines the class that represents the JNDI name for each server.
 * demoiselle.web.transaction.manager_lookup_class=br.gov.app.transaction.jta.jboss.JNDIJboss
 * </pre>
 * 
 * @author CETEC/CTJEE
 * @see IConfig
 */
public class WebTransactionActionConfig implements IConfig {
	
	private static final long serialVersionUID = 1L;

	@ConfigKey(name="framework.demoiselle.web.transaction.type", type=ConfigType.PROPERTIES, resourceName=Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String type;
	
	@ConfigKey(name="framework.demoiselle.web.transaction.manager_lookup_class", type=ConfigType.PROPERTIES, resourceName=Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String managerLookupClass;

	public TransactionType getType() {
		if (type == null) {
			return null;
		} else {
			return TransactionType.valueOf(type.toUpperCase());
		}
	}
	
	public String getManagerLookupClass() {
		return managerLookupClass;
	}
	
	void setType(String type){
		this.type=type;
	}

}
