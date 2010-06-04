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

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.framework.demoiselle.util.config.ConfigurationLoader;

/**
 * @author CETEC/CTJEE
 */
public class WebFactoryConfigTest {

	private static WebFactoryConfig config;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		config = ConfigurationLoader.load(WebFactoryConfig.class);		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		config = null;
	}

	@Test
	public void testGetRegex() {
		String[] values = config.getRegex();
		Assert.assertNotNull(values);
		Assert.assertEquals(1, values.length);
		Assert.assertEquals("\\.I", values[0]);
	}

	@Test
	public void testGetReplace() {
		String[] values = config.getReplace();
		Assert.assertNotNull(values);
		Assert.assertEquals(1, values.length);
		Assert.assertEquals(".implementation.", values[0]);
	}

	@Test
	public void testGetRegexBusiness() {
		String[] values = config.getRegexBusiness();
		Assert.assertNotNull(values);
		Assert.assertEquals(0, values.length);
	}

	@Test
	public void testGetReplaceBusiness() {
		String[] values = config.getReplaceBusiness();
		Assert.assertNotNull(values);
		Assert.assertEquals(0, values.length);
	}

	@Test
	public void testGetRegexDAO() {
		String[] values = config.getRegexDAO();
		Assert.assertNotNull(values);
		Assert.assertEquals(0, values.length);
	}

	@Test
	public void testGetReplaceDAO() {
		String[] values = config.getReplaceDAO();
		Assert.assertNotNull(values);
		Assert.assertEquals(0, values.length);
	}

	@Test
	public void testHasDefault() {
		Assert.assertTrue(true);
	}

	@Test
	public void testHasBusiness() {
		Assert.assertTrue(true);
	}

	@Test
	public void testHasDAO() {
		Assert.assertTrue(true);
	}

}
