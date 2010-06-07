package es.in2.framework.demo.persistence.JDBC;

import java.util.Properties;

import es.in2.framework.demo.util.Constant;
import es.in2.framework.demo.util.config.ConfigKey;
import es.in2.framework.demo.util.config.ConfigType;
import es.in2.framework.demo.util.config.IConfig;

/**
 * <p>
 * Helper class to handle JDBC native configurations
 * </p>
 * 
 * Ex:<br>
 * <pre>
 * #Configurations for JDBC
 * framework.demoiselle.persistence.jdbc.driver=org.postgresql.Driver
 * framework.demoiselle.persistence.jdbc.url=jdbc:postgresql://host/appname
 * framework.demoiselle.persistence.jdbc.user=postgres
 * framework.demoiselle.persistence.jdbc.pass=postgres
 * </pre>
 * <p>
 * For Pool connections accross JNDI name <br>
 * <pre>
 * framework.persistence.jdbc.urlJNDI=testDatabase
 * </pre>
 * </p>
 * 
 * @author CETEC/CTJEE
 */
public class JDBCConfig implements IConfig {
	
	private static final long serialVersionUID = 1L;

	@ConfigKey(name="framework.demoiselle.persistence.jdbc", type=ConfigType.PROPERTIES, resourceName=Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
