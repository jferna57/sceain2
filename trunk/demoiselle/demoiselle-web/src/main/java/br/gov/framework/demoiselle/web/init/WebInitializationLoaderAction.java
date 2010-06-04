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
package br.gov.framework.demoiselle.web.init;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.action.ILoaderAction;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;

/**
 * <p>
 * This is a Default Loader of initialization action. Load all actions in  
 * {@linkplain WebInitializationLoaderActionConfig#getActionList()} 
 * </p>
 * @author CETEC/CTJEE
 */
public class WebInitializationLoaderAction implements ILoaderAction<IInitializationAction> {
	
	private static Logger log = Logger.getLogger(WebInitializationLoaderAction.class);
	
	public Collection<IInitializationAction> getActions() {
		log.debug("Loading Initialization Actions");
		
		Collection<IInitializationAction> result = new ArrayList<IInitializationAction>();
		WebInitializationLoaderActionConfig config = ConfigurationLoader.load(WebInitializationLoaderActionConfig.class);
		String[] actionList = config.getActionList();

		if (actionList != null && actionList.length > 0){
			for (String actionClassName : actionList) {
				Class<?> actionClass;
				try {
					actionClass = Class.forName(actionClassName);
					IInitializationAction action = (IInitializationAction)actionClass.newInstance();
					result.add(action);
				} catch (ClassNotFoundException e) {
					throw new WebInitializationException("Error: Action class \""+ actionClassName +"\" not found!",e);
				} catch (Exception e) {
					throw new WebInitializationException("Error: Could not instantiate Action class \""+ actionClassName +"\"",e);
				}
			}
		}
		
		log.debug("("+result.size()+") Initialization Actions");		
		
		return result;		
	}
	 
}