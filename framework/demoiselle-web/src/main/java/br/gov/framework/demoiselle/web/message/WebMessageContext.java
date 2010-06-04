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
package br.gov.framework.demoiselle.web.message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.core.message.IMessageContext;
import br.gov.framework.demoiselle.core.message.Severity;

/**
 * Structure that implements a message context to be used by Web applications.
 * 
 * @author CETEC/CTJEE
 * @see IMessage
 * @see IMessageContext
 */
public class WebMessageContext implements IMessageContext {

	private static Logger log = Logger.getLogger(WebMessageContext.class);

	/**
	 * A singleton instance of WebMessageContext.
	 */
	private static final WebMessageContext instance = new WebMessageContext();

	/**
	 * TreadLocal to manage the collection of messages.
	 */
	private ThreadLocal<Collection<MessageRecord>> messages = new ThreadLocal<Collection<MessageRecord>>();

	/**
	 * This constructor adds this class to the message context.
	 */
	private WebMessageContext() {
		ContextLocator.getInstance().setMessageContext(this);
		log.debug("Message context created");
	}

	public void addMessage(IMessage message, Object... args) {
		
		log.debug("Adding message " + message + " to the context");
		
		if (messages.get() == null) {
			messages.set(new ArrayList<MessageRecord>());
		}
		
		messages.get().add(new MessageRecord(message, args));
	}

	public Collection<IMessage> getMessages() {
		
		if (messages.get() == null) {
			messages.set(new ArrayList<MessageRecord>());
		}

		Collection<IMessage> result = new MessageArrayList<IMessage>();

		for (MessageRecord record : messages.get()) {
			result.add(new MessageLabelArgumentDecorator(record.getMessage(), record.getArgs()));
		}

		log.debug("Retrieving collection of messages (" + result.size() + ")");

		return result;
	}

	public Collection<IMessage> getMessages(Locale locale) {
		
		if (messages.get() == null) {
			messages.set(new ArrayList<MessageRecord>());
		}

		Collection<IMessage> result = new MessageArrayList<IMessage>();

		for (MessageRecord record : messages.get()) {
			result.add(new MessageLabelArgumentDecorator(
					new MessageLabelInternationalizeDecorator(record
							.getMessage(), locale), record.getArgs()));
		}

		log.debug("Retrieving collection of messages (" + result.size() + ")");

		return result;
	}

	public void clear() {
		messages.set(null);
		log.debug("Cleaning up message context");
	}

	/**
	 * Returns a single instance (singleton pattern).
	 * 
	 * @return WebMessageContext
	 */
	public static WebMessageContext getInstance() {
		return instance;
	}

	/**
	 * An inner class intended to decorate messages with argument values.
	 * 
	 * @see IMessage
	 */
	private class MessageLabelArgumentDecorator implements IMessage {

		private IMessage message;
		private Object[] args;

		public MessageLabelArgumentDecorator(IMessage message, Object... args) {
			this.message = message;
			this.args = args;
		}

		public String getKey() {
			return message.getKey();
		}

		public String getLabel() {
			String label = message.getLabel();

			if (args != null) {
				MessageFormat formatter = new MessageFormat("");
				formatter.applyPattern(message.getLabel());
				label = formatter.format(args);
			}

			return label;
		}

		public Locale getLocale() {
			return message.getLocale();
		}

		public String getResourceName() {
			return message.getResourceName();
		}

		public Severity getSeverity() {
			return message.getSeverity();
		}

		@Override
		public String toString() {
			return message.toString();
		}

		@Override
		public boolean equals(Object obj) {
			return message.equals(obj);
		}

		@Override
		public int hashCode() {
			return message.hashCode();
		}

	}

	/**
	 * An inner class intended to decorate messages with localization.
	 * 
	 * @see IMessage
	 */
	private class MessageLabelInternationalizeDecorator implements IMessage {

		private IMessage message;
		private Locale newLocale;

		public MessageLabelInternationalizeDecorator(IMessage message, Locale newLocale) {
			this.message = message;
			this.newLocale = newLocale;
		}

		public String getKey() {
			return message.getKey();
		}

		public String getLabel() {
			String label = message.getLabel();

			if (!newLocale.equals(message.getLocale())) {
				ResourceBundle bundle = ResourceBundle.getBundle(message.getResourceName(), newLocale);
				if (bundle.getString(message.getKey()) != null) {
					label = bundle.getString(message.getKey());
				}
			}

			return label;
		}

		public Locale getLocale() {
			return message.getLocale();
		}

		public String getResourceName() {
			return message.getResourceName();
		}

		public Severity getSeverity() {
			return message.getSeverity();
		}

		@Override
		public String toString() {
			return message.toString();
		}

		@Override
		public boolean equals(Object obj) {
			return message.equals(obj);
		}

		@Override
		public int hashCode() {
			return message.hashCode();
		}

	}

	/**
	 * An inner class intended to hold an array of messages.
	 */
	private class MessageArrayList<E> extends ArrayList<E> {

		private static final long serialVersionUID = 1L;

		@Override
		public int indexOf(Object o) {
			if (o == null) {
				for (int i = 0; i < this.size(); i++)
					if (this.get(i) == null)
						return i;
			} else {
				for (int i = 0; i < this.size(); i++)
					if (this.get(i).equals(o))
						return i;
			}
			return -1;
		}

	}

	/**
	 * An inner class intended to hold a single message instance.
	 * 
	 * @see IMessage
	 */
	private class MessageRecord {

		private IMessage message;
		private Object[] args;

		public MessageRecord(IMessage message, Object... args) {
			this.message = message;
			this.args = args;
		}

		public IMessage getMessage() {
			return message;
		}

		public Object[] getArgs() {
			return args;
		}

	}

}
