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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.action.ILoaderAction;

/**
 * <p>
 * A Servlet that can be used to execute actions to request redirect.
 * </p>
 * 
 * <p>
 * in WEB-INF/web.xml file:
 * </p>  
 * <pre>
 *     &lt;servlet&gt;
 *         &lt;servlet-name&gt;WebRedirectServlet&lt;/servlet-name&gt;
 *         &lt;servlet-class&gt;br.gov.framework.web.redirect.WebRedirectServlet&lt;/servlet-class&gt;
 *         &lt;init-param&gt;
 *             &lt;param-name&gt;REDIRECT_LOADER_ACTION_CLASS&lt;/param-name&gt;
 *             &lt;param-value&gt;another.LoaderActionClass&lt;/param-value&gt;
 *         &lt;/init-param&gt;
 *     &lt;/servlet&gt;
 *     
 *     &lt;servlet-mapping&gt;
 *         &lt;servlet-name&gt;WebRedirectServlet&lt;/servlet-name&gt;
 *         &lt;url-pattern&gt;/redirect&lt;/url-pattern&gt;
 *     &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * If REDIRECT_LOADER_ACTION_CLASS not configured default loader is used {@linkplain WebRedirectLoaderAction}
 * @author CETEC/CTJEE
 * 
 */
public class WebRedirectServlet extends HttpServlet {
 
	private static final long serialVersionUID = 1L;
	
	public static final String REDIRECT_LOADER_ACTION_CLASS = "REDIRECT_LOADER_ACTION_CLASS";

	public static final String WEB_REDIRECT_SERVLET_URL_PATTERN = "/redirect";
	
	private static final Logger log = Logger.getLogger(WebRedirectServlet.class);
	
	private ILoaderAction<IRedirectAction> loader;

	public void setLoader(ILoaderAction<IRedirectAction> loader) {
		this.loader = loader;
	}	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebRedirectActionManager redirectActionManager = new WebRedirectActionManager(req, resp);

		redirectActionManager.setLoader(this.loader);
		redirectActionManager.execute();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		log.debug("Init WebRedirectServlet");
		String loaderActionClassParameter = config.getInitParameter(REDIRECT_LOADER_ACTION_CLASS);
		String loaderActionClassName = null;
		if (loaderActionClassParameter != null && !loaderActionClassParameter.trim().equals("")){
			loaderActionClassName = loaderActionClassParameter.trim();
		} else {
			loaderActionClassName = WebRedirectLoaderAction.class.getName();
		}
		
		try {
			Class<?> actionLoaderClass = Class.forName(loaderActionClassName);
			ILoaderAction<IRedirectAction> loader = (ILoaderAction) actionLoaderClass.newInstance();
			setLoader(loader);
			log.debug("Set LoaderAction OK!");
		} catch (ClassNotFoundException e) {
			throw new WebRedirectException("Error: LoaderAction class \""+ loaderActionClassName +"\" not found!",e);
		} catch (Exception e) {
			throw new WebRedirectException("Error: Could not instantiate LoaderAction class \""+ loaderActionClassName +"\"",e);
		}
	}
	 
}