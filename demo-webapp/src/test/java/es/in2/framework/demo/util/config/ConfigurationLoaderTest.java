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
package es.in2.framework.demo.util.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author CETEC/CTJEE
 */
public class ConfigurationLoaderTest {

	private SampleConfiguration cfg;
	
	/**
	 * {@ Before} annotation does the method below will be executed
	 * before executing each test case (methods with 
	 * annotation {@ Test}.
	 * @author CETEC/CTJEE
	 * @see ConfigurationLoader
	 * @see System
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.getProperties()
			.put("framework.stringValue", "value");
		System.getProperties()
			.put("framework.integerValue", "10");
		System.getProperties()
			.put("framework.intValue", "10");
		System.getProperties()
			.put("framework.listValue", "value1, value2, value3");
		System.getProperties()
			.put("framework.booleanValue", "true");
		System.getProperties()
			.put("framework.arrayStringValue", "value1, value2");
		System.getProperties()
			.put("framework.boolValue", "false");		
		System.getProperties()
		.put("framework.byteValue", "0");

		cfg = ConfigurationLoader.load(SampleConfiguration.class);		
	}

	@After
	public void tearDown() throws Exception {
		System.getProperties().remove("framework.stringValue");
		System.getProperties().remove("framework.integerValue");
		System.getProperties().remove("framework.intValue");
		System.getProperties().remove("framework.listValue");
		System.getProperties().remove("framework.booleanValue");
		System.getProperties().remove("framework.boolValue");
		System.getProperties().remove("framework.arrayStringValue");
		System.getProperties().remove("framework.byteValue");
	}
	
	@Test
	public void testLoad() {
		ConfigurationLoader.load(new Object());
	}

	@Test
	public void testLoadStringValueSystem() {
		assertEquals("value", cfg.getStringValueSystem());
	}

	@Test
	public void testLoadListValueSystem() {
		List<String> listValue = new ArrayList<String>(3);
		listValue.add("value1");
		listValue.add("value2");
		listValue.add("value3");
		assertEquals(listValue, cfg.getListValueSystem());
	}

	@Test
	public void testLoadIntegerValueSystem() {
		assertEquals(new Integer(10), cfg.getIntegerValueSystem());
	}

	@Test
	public void testLoadIntValueSystem() {
		assertEquals(10, cfg.getIntValueSystem());
	}

	@Test
	public void testLoadStringValueProperties() {
		assertEquals("value", cfg.getStringValueProperties());
	}
	
	@Test
	public void testLoadListValueProperties() {
		List<String> listValue = new ArrayList<String>(3);
		listValue.add("value1");
		listValue.add("value2");
		listValue.add("value3");
		assertEquals(listValue, cfg.getListValueProperties());
	}

	@Test
	public void testLoadIntegerValueProperties() {
		assertEquals(new Integer(10), cfg.getIntegerValueProperties());
	}

	@Test
	public void testLoadIntValueProperties() {
		assertEquals(10, cfg.getIntValueProperties());
	}

	@Test
	public void testLoadStringValueXML() {
		assertEquals("value", cfg.getStringValueXML());
	}
	
	@Test
	public void testLoadListValueXML() {
		List<String> listValue = new ArrayList<String>(3);
		listValue.add("value1");
		listValue.add("value2");
		listValue.add("value3");
		assertEquals(listValue, cfg.getListValueXML());
	}

	@Test
	public void testLoadIntegerValueXML() {
		assertEquals(new Integer(10), cfg.getIntegerValueXML());
	}

	@Test
	public void testLoadIntValueXML() {
		assertEquals(10, cfg.getIntValueXML());
	}

	@Test
	public void testLoadWithFakeProperties() {
		try {
			ConfigurationLoader.load(ConfigMock.class);
			fail();
		} catch (ConfigurationException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testLoadWithPrefix() {
		assertEquals(3, cfg.getPrefixProperties().size());
	}
	
	@Test
	public void testLoadWithInterpolation() {
		System.getProperties().put("interpolation-2", "interx");
		ConfigMock2 mock = ConfigurationLoader.load(ConfigMock2.class);
		assertEquals("value", mock.getKey());
	}
	
}


class SampleConfiguration {

	/**
	 * <p>
	 * Annotation {@link ConfigKey} will 
	 * load the attribute immediately below
	 * item value specified by name. The source 
	 * of item is defined by <b>type</b>, that may assume 
	 * the following values:
	 * </p>
	 * <ul>
	 * <li>SYSTEM: get the values from a class {@link System} of Java;
	 * <li>XML: get the values from a XML file;
	 * <li>PROPERTIES: get the values from a properties file.
	 * </ul> 
	 */
	@ConfigKey(name = "framework.stringValue",
			type=ConfigType.SYSTEM)
	private String stringValueSystem;
	@ConfigKey(name = "framework.integerValue",
			type=ConfigType.SYSTEM)
	private Integer integerValueSystem;
	@ConfigKey(name = "framework.intValue",
			type=ConfigType.SYSTEM)
	private int intValueSystem;
	@ConfigKey(name = "framework.listValue",
			type=ConfigType.SYSTEM)
	private List<String> listValueSystem;
	@ConfigKey(name = "framework.booleanValue",
			type=ConfigType.SYSTEM)
	private Boolean booleanValueSystem;
	@ConfigKey(name = "framework.boolValue",
			type=ConfigType.SYSTEM)
	private boolean boolValueSystem;
	@ConfigKey(name = "framework.arrayStringValue",
			type=ConfigType.SYSTEM)
	private String[] arrayStringSystem;
	
	@ConfigKey(name = "framework.stringValue",
			type=ConfigType.PROPERTIES,
			resourceName="configuration.properties")
	private String stringValueProperties;
	@ConfigKey(name = "framework.integerValue",
			type=ConfigType.PROPERTIES,
			resourceName="configuration.properties")
	private Integer integerValueProperties;
	@ConfigKey(name = "framework.byteValue",
			type=ConfigType.PROPERTIES,
			resourceName="configuration.properties")
	private Byte byteValueProperties;
	@ConfigKey(name = "framework.intValue",
			type=ConfigType.PROPERTIES,
			resourceName="configuration.properties")
	private int intValueProperties;
	@ConfigKey(name = "framework.listValue",
			type=ConfigType.PROPERTIES,
			resourceName="configuration.properties")
	private List<String> listValueProperties;
	@ConfigKey(name = "prefix",
			type = ConfigType.PROPERTIES,
			resourceName = "configuration.properties")
	private Properties prefixProperties;
	
	@ConfigKey(name = "framework.stringValue",
			type=ConfigType.XML,
			resourceName="configuration.xml")
	private String stringValueXML;
	@ConfigKey(name = "framework.integerValue",
			type=ConfigType.XML,
			resourceName="configuration.xml")
	private Integer integerValueXML;
	@ConfigKey(name = "framework.byteValue",
			type=ConfigType.XML,
			resourceName="configuration.xml")
	private Byte byteValueXML;
	@ConfigKey(name = "framework.intValue",
			type=ConfigType.XML,
			resourceName="configuration.xml")
	private int intValueXML;
	@ConfigKey(name = "framework.listValue",
			type=ConfigType.XML,
			resourceName="configuration.xml")
	private List<String> listValueXML;
	
	public SampleConfiguration() {
	}

	public String getStringValueSystem() {
		return stringValueSystem;
	}

	public Integer getIntegerValueSystem() {
		return integerValueSystem;
	}

	public int getIntValueSystem() {
		return intValueSystem;
	}

	public List<String> getListValueSystem() {
		return listValueSystem;
	}

	public Boolean getBooleanValueSystem() {
		return booleanValueSystem;
	}

	public boolean isBoolValueSystem() {
		return boolValueSystem;
	}

	public String getStringValueProperties() {
		return stringValueProperties;
	}

	public Integer getIntegerValueProperties() {
		return integerValueProperties;
	}
	
	public Byte getByteValueProperties(){
		return byteValueProperties;
	}

	public int getIntValueProperties() {
		return intValueProperties;
	}

	public List<String> getListValueProperties() {
		return listValueProperties;
	}

	public Properties getPrefixProperties() {
		return prefixProperties;
	}
	
	public String getStringValueXML() {
		return stringValueXML;
	}

	public Integer getIntegerValueXML() {
		return integerValueXML;
	}
	
	public Byte getByteValueXML() {
		return byteValueXML;
	}

	public int getIntValueXML() {
		return intValueXML;
	}

	public List<String> getListValueXML() {
		return listValueXML;
	}

	public String[] getArrayStringSystem() {
		return arrayStringSystem;
	}
	
}

class ConfigMock {
	
	@ConfigKey(name="config.key", type=ConfigType.PROPERTIES, resourceName="fake.properties")
	private String conf;

	public String getConf() {
		return conf;
	}
	
}

class ConfigMock2 {
	
	@ConfigKey(name="var.${interpolation}-${interpolation-2}.prop", type=ConfigType.PROPERTIES, resourceName="configuration.properties", defaultInSystem=true)
	private String key;

	public String getKey() {
		return key;
	}
	
}