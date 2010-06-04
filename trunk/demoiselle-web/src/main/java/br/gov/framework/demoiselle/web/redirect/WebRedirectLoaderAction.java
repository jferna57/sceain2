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
package br.gov.framework.demoiselle.web.redirect;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.action.ILoaderAction;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;

/**
 * <p>
 * This is a Default Loader of redirect action. Load all actions in  
 * {@linkplain WebRedirectLoaderActionConfig#getActionList()} 
 * </p>
 * 
 * @author CETEC/CTJEE
 */
public class WebRedirectLoaderAction implements ILoaderAction<IRedirectAction> {
	
	private static Logger log = Logger.getLogger(WebRedirectLoaderAction.class);
	private Collection<IRedirectAction> actions = null;
	
	public WebRedirectLoaderAction(){
		log.debug("Executing WebRedirectLoaderAction constructor.");
		setActions();
	}
	
	public void setActions() {
		log.debug("Loading Redirect Actions");
		
		actions = new ArrayList<IRedirectAction>();
		WebRedirectLoaderActionConfig config = ConfigurationLoader.load(WebRedirectLoaderActionConfig.class);
		String[] actionList = config.getActionList();

		if (actionList != null && actionList.length > 0){
			for (String actionClassName : actionList) {
				Class<?> actionClass;
				try {
					actionClass = Class.forName(actionClassName);
					IRedirectAction action = (IRedirectAction) actionClass.newInstance();
					actions.add(action);
				} catch (ClassNotFoundException e) {
					throw new WebRedirectException("Error: Action class \""+ actionClassName +"\" not found!",e);
				} catch (Exception e) {
					throw new WebRedirectException("Error: Could not instantiate Action class \""+ actionClassName +"\"",e);
				}
			}
		}
		
		log.debug("("+actions.size()+") Redirect Actions");
	}

	public Collection<IRedirectAction> getActions() {
		return this.actions;
	}
	 
}