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
package br.gov.framework.demoiselle.view.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Utility class intended to manage cookies in Web applications.
 * 
 * @author CETEC/CTJEE 
 */
public class CookieManager {
	
	private static final Logger log = Logger.getLogger(CookieManager.class);

	/**
	 * Retrieves a cookie by its name.
	 * 
	 * @param request - Web request
	 * @param cookieName - cookie name to be retrieve
	 * @return retrives a Cookie object corresponding to cookie name passed in as parameter
	 */
	public static final Cookie getCookie(HttpServletRequest request, String cookieName) {
		
		Cookie result = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					result = cookie;
				}
			}
		}
		
		if (result != null)
			log.debug("Get cookie " + result.getName() + " with value " + result.getValue());
		else
			log.debug("Get cookie return null");
		
		return result;
	}

	/**
	 * Saves a cookie with path = '/' and records its maximum life time.
	 * 
	 * @param response HttpServletResponse
	 * @param cookieName cookie name to be recorded by browser
	 * @param cookieValue cookie value
	 * @param maxAge lifetime.
	 */
	public static final void saveCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
		String path = "/";		
		saveCookie(response, cookieName, cookieValue, path, maxAge);
	}

	/**
	 * Saves a cookie with path = '/' and sets the maximum time to (10000 + 3600) * 24 * 30
	 * 
	 * @param response HttpServletResponse
	 * @param cookieName cookie name to be recorded by browser
	 * @param cookieValue cookie value
	 */
	public static final void saveCookie(HttpServletResponse response, String cookieName, String cookieValue) {
		String path = "/";
		int maxAge = (10000 + 3600) * 24 * 30;
		saveCookie(response, cookieName, cookieValue, path, maxAge);		
	}

	/**
	 * Saves a cookie setting the maximum time to (10000 + 3600) * 24 * 30
	 * 
	 * @param response HttpServletResponse
	 * @param cookieName cookie name to be recorded by browser
	 * @param cookieValue cookie value
	 * @param path path where cookie will be stored by browser
	 */
	public static final void saveCookie(HttpServletResponse response, String cookieName, String cookieValue, String path) {
		int maxAge = (10000 + 3600) * 24 * 30;
		saveCookie(response, cookieName, cookieValue, path, maxAge);
	}

	/**
	 * Saves a cookie
	 * 
	 * @param response HttpServletResponse
	 * @param cookieName cookie name to be recorded by browser
	 * @param cookieValue cookie value
	 * @param path path where cookie will be stored by browser
	 * @param maxAge lifetime
	 */
	public static final void saveCookie(HttpServletResponse response, String cookieName, String cookieValue, String path, int maxAge) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
		log.debug("Saved cookie " + cookieName + " with value " + cookieValue + " and path "+ path +" sucessfull");
	}
	
	/**
	 * Removes the specified cookie from the request.
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 */
	public static final void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie != null){			
			cookie.setComment("deleted");
			cookie.setPath("/");		
			response.addCookie(cookie);
			log.debug("Cookie " + cookieName + " removed");
		}		
	}
	
	/**
	 * Removes the specified cookie from the request.
	 * 
	 * @param request
	 * @param response
	 * @param cookie
	 */
	public static final void removeCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
		Cookie cookieLocal = getCookie(request, cookie.getName());
		removeCookie(request, response, cookieLocal.getName());
	}
	
}
