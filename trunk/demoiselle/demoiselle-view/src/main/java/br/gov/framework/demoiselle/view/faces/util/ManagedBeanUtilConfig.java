package br.gov.framework.demoiselle.view.faces.util;

import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigKey;
import br.gov.framework.demoiselle.util.config.ConfigType;
import br.gov.framework.demoiselle.util.config.IConfig;

public class ManagedBeanUtilConfig implements IConfig {

	private static final long serialVersionUID = 1L;
	
	@ConfigKey(name = "framework.demoiselle.view.faces.message.priority", type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String messagePriority;
	
	public MessagePriority getMessagePriority() {
		if (messagePriority == null) {
			//Default Value
			return MessagePriority.MESSAGE_BUNDLE;
		} else {
			return MessagePriority.valueOf(messagePriority.toUpperCase());
		}
	}

}
