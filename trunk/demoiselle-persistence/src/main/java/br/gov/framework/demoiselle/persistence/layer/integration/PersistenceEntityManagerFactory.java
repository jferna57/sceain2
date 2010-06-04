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
package br.gov.framework.demoiselle.persistence.layer.integration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;
import br.gov.framework.demoiselle.persistence.EntityManagerProxy;
import br.gov.framework.demoiselle.persistence.PersistenceException;
import br.gov.framework.demoiselle.persistence.PersistenceHandler;
import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.util.layer.integration.GenericFactory;

/**
 * A factory intended to produce entity managers from the available JPA
 * persistence units.
 * 
 * @author CETEC/CTJEE
 * @see GenericFactory
 * @see IEntityManagerFactory
 */
public class PersistenceEntityManagerFactory
	extends GenericFactory implements IEntityManagerFactory {

	private static Logger log = Logger.getLogger(PersistenceEntityManagerFactory.class);

	/**
	 * This factory verifies if there is a JPATransactionResource for this
	 * persistence unit and then creates a respective entity manager. If there
	 * is no resource, the entity manager is created and stored into a
	 * JPATransactionResource and added to the transaction context.
	 */
	public EntityManager create(InjectionContext ctx) {

		Injection injection = ctx.getInjection();

		String persistenceUnit;
		if (!injection.name().equals("")) {
			persistenceUnit = injection.name();
			
		} else {
			if (ctx.getClassType().isAnnotationPresent(DefaultPersistenceUnit.class)) {
				
				DefaultPersistenceUnit pu = ctx.getClassType().getAnnotation(DefaultPersistenceUnit.class);
				persistenceUnit = pu.name();
				
			} else if (ctx.getClassType().getPackage().isAnnotationPresent(DefaultPersistenceUnit.class)) {
				
				DefaultPersistenceUnit pu = ctx.getClassType().getPackage().getAnnotation(DefaultPersistenceUnit.class);
				persistenceUnit = pu.name();
				
			} else {
				PersistenceEntityManagerFactoryConfig config = ConfigurationLoader.load(PersistenceEntityManagerFactoryConfig.class);
				if (config.getPersistenceUnitName() == null) {
					throw new PersistenceException("Default persistence unit name for JPA must be defined in " + Constant.FRAMEWORK_CONFIGURATOR_FILE);
				}
				persistenceUnit = config.getPersistenceUnitName();
			}
		}

		EntityManagerFactory emf = PersistenceHandler.getInstance().getEntityManagerFactory(persistenceUnit);

		log.debug("Creating " + EntityManager.class.getName() + " for persistence unit " + persistenceUnit);
		
		return createWithLazyCreateProxy(EntityManagerProxy.class, new Class[] {
				EntityManagerFactory.class, String.class }, new Object[] { emf, persistenceUnit });
	}

}
