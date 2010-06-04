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

import static org.easymock.EasyMock.cmp;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unitary tests concerning cookie management.
 * 
 * @author CETEC/CTJEE
 * @see Cookie
 */
public class CookieManagerTest {

	private Comparator<Cookie> comparatorCookie;

	@Before
	public void setUp() throws Exception {
		comparatorCookie = new Comparator<Cookie>() {
			public int compare(Cookie c1, Cookie c2) {
				if (!c1.getName().equals(c2.getName()))
					return -1;
				if (!c1.getValue().equals(c2.getValue()))
					return -1;
				if (!c1.getPath().equals(c2.getPath()))
					return -1;
				if (c1.getMaxAge() != c2.getMaxAge())
					return -1;
				return 0;
			}
		};
	}

	@Test
	public void testGetCookie() {

		HttpServletRequest request = createMock(HttpServletRequest.class);

		Cookie cookie1 = new Cookie("key1", "value1");
		Cookie cookie2 = new Cookie("key2", "value2");
		Cookie cookie3 = new Cookie("key3", "value3");
		Cookie cookie4 = new Cookie("key4", "value4");
		Cookie cookie5 = new Cookie("key5", "value5");
		Cookie[] cookies = new Cookie[] { cookie1, cookie2, cookie3, cookie4, cookie5 };

		expect(request.getCookies()).andReturn(cookies);
		replay(request);

		Cookie cookie = CookieManager.getCookie(request, "key4");

		verify(request);

		assertEquals(cookie4, cookie);
	}

	@Test
	public void testGetCookieNull() {

		HttpServletRequest request = createMock(HttpServletRequest.class);

		expect(request.getCookies()).andReturn(null);
		replay(request);

		Cookie cookie = CookieManager.getCookie(request, "key4");

		verify(request);

		Assert.assertNull(cookie);
	}

	@Test
	public void testSaveCookieNameValue() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/");
		cookie.setMaxAge((10000 + 3600) * 24 * 30);

		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.addCookie(cmp(cookie, comparatorCookie, LogicalOperator.EQUAL));
		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue());

		verify(response);
	}

	@Test
	public void testSaveCookieNameValuePath() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/teste");
		cookie.setMaxAge((10000 + 3600) * 24 * 30);

		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.addCookie(cmp(cookie, comparatorCookie, LogicalOperator.EQUAL));
		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue(), cookie.getPath());

		verify(response);
	}

	@Test
	public void testSaveCookieNameValuePathMaxage() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/teste");
		cookie.setMaxAge(10000);

		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.addCookie(cmp(cookie, comparatorCookie, LogicalOperator.EQUAL));
		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue(),
				cookie.getPath(), cookie.getMaxAge());

		verify(response);
	}

	@Test
	public void testSaveCookieNameValueMaxage() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/teste");
		cookie.setMaxAge(10000);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);

		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue(),
				cookie.getMaxAge());

		verify(response);
	}

	@Test
	public void testRemoveCookieName() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/teste");
		cookie.setMaxAge(10000);

		HttpServletRequest request = createMock(HttpServletRequest.class);

		Cookie cookie1 = new Cookie("key1", "value1");
		Cookie cookie2 = new Cookie("key2", "value2");
		Cookie cookie3 = new Cookie("key3", "value3");
		Cookie cookie4 = new Cookie("key4", "value4");
		Cookie cookie5 = new Cookie("key5", "value5");
		Cookie[] cookies = new Cookie[] { cookie1, cookie2, cookie3, cookie4, cookie5 };

		expect(request.getCookies()).andReturn(cookies);

		replay(request);

		HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
		response.addCookie(cmp(cookie, comparatorCookie, LogicalOperator.EQUAL));
		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue(),
				cookie.getPath(), cookie.getMaxAge());

		verify(response);

		CookieManager.removeCookie(request, response, "key1");
	}

	@Test
	public void testRemoveCookie() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/teste");
		cookie.setMaxAge(10000);

		HttpServletRequest request = createMock(HttpServletRequest.class);

		Cookie cookie1 = new Cookie("key1", "value1");
		Cookie cookie2 = new Cookie("key2", "value2");
		Cookie cookie3 = new Cookie("key3", "value3");
		Cookie cookie4 = new Cookie("key4", "value4");
		Cookie cookie5 = new Cookie("key5", "value5");
		Cookie[] cookies = new Cookie[] { cookie1, cookie2, cookie3, cookie4, cookie5 };

		expect(request.getCookies()).andReturn(cookies).anyTimes();

		replay(request);

		HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
		response.addCookie(cmp(cookie, comparatorCookie, LogicalOperator.EQUAL));
		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue(),
				cookie.getPath(), cookie.getMaxAge());

		verify(response);

		CookieManager.removeCookie(request, response, cookie1);
	}

	@Test
	public void testRemoveCookieNull() {
		
		Cookie cookie = new Cookie("key", "value");
		cookie.setPath("/teste");
		cookie.setMaxAge(10000);

		HttpServletRequest request = createMock(HttpServletRequest.class);

		String cookie1 = null;
		Cookie cookie2 = new Cookie("key2", "value2");
		Cookie cookie3 = new Cookie("key3", "value3");
		Cookie cookie4 = new Cookie("key4", "value4");
		Cookie cookie5 = new Cookie("key5", "value5");
		Cookie[] cookies = new Cookie[] { cookie2, cookie3, cookie4, cookie5 };

		expect(request.getCookies()).andReturn(cookies).anyTimes();

		replay(request);

		HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
		response.addCookie(cmp(cookie, comparatorCookie, LogicalOperator.EQUAL));
		replay(response);

		CookieManager.saveCookie(response, cookie.getName(), cookie.getValue(),
				cookie.getPath(), cookie.getMaxAge());

		verify(response);

		CookieManager.removeCookie(request, response, cookie1);
	}

}