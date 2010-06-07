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
package es.in2.framework.demo.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

/**
 * Class intended to handle JPA persistence unit factories.
 * 
 * @author CETEC/CTJEE
 * @see EntityManagerFactory
 */
public class PersistenceHandler {

	private static Logger log = Logger.getLogger(PersistenceHandler.class);

	/**
	 * The single instance for the class (i.e. singleton pattern).
	 */
	private static final PersistenceHandler instance = new PersistenceHandler();

	/**
	 * The map of existing entity manager factories, using persistence unit names as keys.
	 */
	Map<String, EntityManagerFactory> emfs = new HashMap<String, EntityManagerFactory>();

	/**
	 * Class constructor (private).
	 */
	private PersistenceHandler() {
		log.debug("Persistence handler created");
	}

	/**
	 * Returns an entity manager factory for the specified persistence unit name.
	 * 
	 * @param persistenceUnit	the persistence unit unique name
	 * @return	EntityManagerFactory
	 */
	public EntityManagerFactory getEntityManagerFactory(String persistenceUnit) {
		
		if (emfs.containsKey(persistenceUnit)) {
			return emfs.get(persistenceUnit);
			
		} else {
			log.debug("Creating entity manager factory for persistence unit \"" + persistenceUnit + "\"");

			EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
			emfs.put(persistenceUnit, emf);
			
			return emf;
		}
	}

	/**
	 * Returns the single instance for the handler (singleton approach).
	 * 
	 * @return	PersistenceHandler
	 */
	public static PersistenceHandler getInstance() {
		return instance;
	}

}
