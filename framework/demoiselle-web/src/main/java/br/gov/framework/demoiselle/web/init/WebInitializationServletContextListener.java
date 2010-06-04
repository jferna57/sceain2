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

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.action.IActionManager;
import br.gov.framework.demoiselle.core.action.ILoaderAction;

/**
 * <p>
 * A ServletContextListener that can be used to execute actions during inicialization.
 * </p>
 * 
 * <p>
 * in WEB-INF/web.xml file:
 * </p>  
 * <pre>
 *     &lt;listener&gt;
 *         &lt;listener-class&gt;
 *             br.gov.framework.demoiselle.web.init.WebInitializationServletListener
 *         &lt;/listener-class&gt;
 *     &lt;/listener&gt;
 * </pre>
 * 
 * <p>
 * To configure another loader action
 * </p>
 * 
 * <pre>
 *     &lt;context-param&gt;
 *         &lt;param-name&gt;INIT_LOADER_ACTION_CLASS&lt;/param-name&gt;
 *         &lt;param-value&gt;another.LoaderActionClass&lt;/param-value&gt;
 *     &lt;/context-param&gt;
 * </pre>
 * 
 * If INIT_LOADER_ACTION_CLASS not configured default loader is used {@linkplain WebInitializationLoaderAction}
 * 
 * @author CETEC/CTJEE
 */
public class WebInitializationServletContextListener implements IActionManager<IInitializationAction>, ServletContextListener {
 
	public static final String INIT_LOADER_ACTION_CLASS = "INIT_LOADER_ACTION_CLASS";
	
	private static Logger log = Logger.getLogger(WebInitializationServletContextListener.class);
	
	private ILoaderAction<IInitializationAction> loaderAction;
	private ServletContext context;
	
	public void setLoader(ILoaderAction<IInitializationAction> loader) {
		log.debug("Set Loader Action "+loader.getClass());
		this.loaderAction = loader;
	}

	public void execute() {
		Collection<IInitializationAction> actions = loaderAction.getActions();
		
		for (IInitializationAction action : actions) {
			log.debug("Execute Action "+action.getClass());
			action.setServletContext(context);
			action.execute();
		}
	}

	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent event) {
		log.debug("Context Initialized");
		
		context = event.getServletContext();
		
		String loaderActionClassParameter = event.getServletContext().getInitParameter(INIT_LOADER_ACTION_CLASS);
		String loaderActionClassName = null;
		if (loaderActionClassParameter != null && !loaderActionClassParameter.trim().equals("")){
			loaderActionClassName = loaderActionClassParameter.trim();
		} else{
			loaderActionClassName = WebInitializationLoaderAction.class.getName();
		}
		Class<?> actionLoaderClass;
		try {
			actionLoaderClass = Class.forName(loaderActionClassName);
			ILoaderAction<IInitializationAction> loader = (ILoaderAction<IInitializationAction>) actionLoaderClass.newInstance();
			setLoader(loader);
			execute();
		} catch (ClassNotFoundException e) {
			throw new WebInitializationException("Error: LoaderAction class \""+ loaderActionClassName +"\" not found!",e);
		} catch (Exception e) {
			throw new WebInitializationException("Error: Could not instantiate LoaderAction class \""+ loaderActionClassName +"\"",e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		log.debug("Context Destroyed");
	}

}