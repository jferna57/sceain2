package br.gov.framework.demoiselle.web.layer.integration;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.layer.IPersistenceController;
import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.util.layer.integration.GenericFactory;

/**
 * Default framework PersistenceController Factory.
 * This factory use convention names to instantiate objects from class types that implements each IPersistenceController interface.
 * 
 * @author CETEC/CTJEE
 */
public class WebPersistenceControllerFactory extends GenericFactory implements IFactory<IPersistenceController> {

	private static Logger log = Logger.getLogger(WebPersistenceControllerFactory.class);
	
	/**
	 * <p>
	 * Creates and retrieve objects from class specified by name or field type values stored in InjectionContext.
	 * If the programmer has specified the name of a class to be injected it will be done, otherwise, by convention,
	 * this method will instantiate an object found in a sub-package called "implementation" with same name removing
	 * the prefix "I" for example:
	 * </p>
	 * <p>
	 *  field declaration:<br>
	 *  <code>
	 *  \@injection
	 *  private br.gov.app.persistence.IUseCasePersistenceController pc;
	 *  </code>
	 *  </p>
	 *  <p>
	 *  The result will be like:<br>
	 *  <code>
	 *  pc = new br.gov.app.persistence.implementation.UseCasePersistenceController();
	 *  </code>
	 *  </p>
	 */
	public IPersistenceController create(InjectionContext ctx) {
		WebFactoryConfig config = ConfigurationLoader.load(WebFactoryConfig.class);

		String className = null;
		if (config.hasPersistence()) {
			className = conventionForClassName(ctx, config.getRegexPersistence(), config.getReplacePersistence());
		} else {
			className = conventionForClassName(ctx, config.getRegex(), config.getReplace());
		}
		
		try {
			log.debug("Creating PersistenceController from class "+ className);
			Class<?> clazz = Class.forName(className);
			return (IPersistenceController) createWithLazyCreateProxy(clazz);
		} catch (Exception e) {
			throw new WebLayerIntegrationException("Could not instantiate class \""+ className +"\"", e);
		}
	}

}
