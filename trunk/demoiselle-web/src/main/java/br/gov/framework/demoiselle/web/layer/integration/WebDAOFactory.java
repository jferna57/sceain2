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

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.layer.IDAO;
import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.util.layer.integration.GenericFactory;

/**
 * Default framework DAO Factory.
 * This factory use convention names to instantiate objects from class types that implements each IDAO<? extends IPojo> interface.
 * 
 * @author CETEC/CTJEE
 */
public class WebDAOFactory extends GenericFactory implements IFactory<IDAO<? extends IPojo>> {
	
	private static Logger log = Logger.getLogger(WebDAOFactory.class);
	
	/**
	 * <p>
	 * Creates and retrieve objects from class specified by name or field type values stored in InjectionContext.
	 * If the programmer has specified the name of a class to be injected it will be done, otherwise, by convention,
	 * this method will instantiate an object found in a sub-package called "implementation" with same name removing
	 * the prefix "I" for example:
	 * </p>
	 * <p>
	 *  field declaration:<br>
	 *  <code>
	 *  \@injection
	 *  private br.gov.app.persistence.IUseCaseDAO dao;
	 *  </code>
	 *  </p>
	 *  <p>
	 *  The result will be like:<br>
	 *  <code>
	 *  dao = new br.gov.app.persistence.implementation.UseCaseDAO();
	 *  </code>
	 *  </p>
	 */
	@SuppressWarnings("unchecked")
	public IDAO<? extends IPojo> create(InjectionContext ctx) {
		WebFactoryConfig config = ConfigurationLoader.load(WebFactoryConfig.class);

		String className = null;
		if (config.hasDAO()) {
			className = conventionForClassName(ctx, config.getRegexDAO(), config.getReplaceDAO());
		} else {
			className = conventionForClassName(ctx, config.getRegex(), config.getReplace());
		}
		
		try {
			log.debug("Creating DAO from class "+ className);
			Class<?> clazz = Class.forName(className);
			return (IDAO<? extends IPojo>) createWithLazyCreateProxy(clazz);
		} catch (Exception e) {
			throw new WebLayerIntegrationException("Could not instantiate class \""+ className +"\"", e);
		}
	}
	
}
