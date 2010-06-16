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
package app.message;

import java.util.Locale;

import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.core.message.Severity;

/**
 * @author CETEC/CTJEE
 */
public enum ResourceBundleMessage implements IMessage {
	
	SIMPLE_MESSAGE("mensagem.simples", ""),
	PARAMETERIZED_MESSAGE("mensagem.parametrizada", "Mensagem parametrizada: {0} e {1}");

	private String key;
	private String label;
			
	private ResourceBundleMessage(String key) {
		this.key = key;
	}
	
	private ResourceBundleMessage(String key, String defaultMessage) {
		this.key = key;
		this.label = defaultMessage;
	}
	
	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public Locale getLocale() {
		return new Locale("pt", "BR");
	}

	public Severity getSeverity() {
		return Severity.FATAL;
	}
	
	public String getResourceName() {
		return "messages";
	}	

}