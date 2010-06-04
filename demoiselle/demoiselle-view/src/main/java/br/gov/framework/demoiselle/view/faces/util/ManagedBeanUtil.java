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
package br.gov.framework.demoiselle.view.faces.util;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;

/**
 * Utility class intended to make easy operations with ManagedBean.
 * 
 * @author CETEC/CTJEE
 */
public abstract class ManagedBeanUtil {

	private static final Logger log = Logger.getLogger(ManagedBeanUtil.class);

	private static HttpServletRequest request;
	private static HttpServletResponse response;
	private static ExternalContext externalContext;

	/**
	 * @param valueBidingExpression
	 */
	public static String getBeanExpressionFromValueBidingExpression(String valueBidingExpression) {
		String result = null;
		if (valueBidingExpression != null) {
			if (valueBidingExpression.contains(".")) {
				StringBuffer sbOut = new StringBuffer();
				for (char c : valueBidingExpression.toCharArray()) {
					if (c == '.') {
						break;
					}
					sbOut.append(c);
				}
				result = sbOut.toString() + "}";
			} else {
				result = valueBidingExpression;
			}
		}
		return result;
	}

	/**
	 * Used to get the last attribute or method name from the expression.
	 * 
	 * @param valueBidingExpression {@linkplain Throwable}
	 * @return {@linkplain Throwable} Attribute Or Method name
	 */
	public static String getAttributeOrMethodNameFromValueBidingExpression(String valueBidingExpression) {
		String result = null;
		if (valueBidingExpression != null) {
			int lastDot = valueBidingExpression.lastIndexOf('.');
			if (lastDot < 0) {
				result = null;
			} else {
				int lastCharIndex = valueBidingExpression.length() - 1;
				char lastChar = valueBidingExpression.charAt(lastCharIndex);
				int endIndex = (lastChar == '}' ? lastCharIndex: lastCharIndex + 1);
				result = valueBidingExpression.substring(lastDot + 1, endIndex);
			}
		}
		return result;
	}

	/**
	 * @param severity
	 */
	public static FacesMessage.Severity getSeverity(
			br.gov.framework.demoiselle.core.message.Severity severity) {
		Severity sev = FacesMessage.SEVERITY_INFO;
		if (severity == br.gov.framework.demoiselle.core.message.Severity.INFO)
			sev = FacesMessage.SEVERITY_INFO;
		else if (severity == br.gov.framework.demoiselle.core.message.Severity.WARNING)
			sev = FacesMessage.SEVERITY_WARN;
		else if (severity == br.gov.framework.demoiselle.core.message.Severity.ERROR)
			sev = FacesMessage.SEVERITY_ERROR;
		else if (severity == br.gov.framework.demoiselle.core.message.Severity.FATAL)
			sev = FacesMessage.SEVERITY_FATAL;
		return sev;
	}

	/**
	 * @param msgs
	 */
	public static void addMessages(List<FacesMessage> msgs) {
		for (FacesMessage msg : msgs) {
			addMessage(msg);
		}
	}

	/**
	 * @param msgs
	 */
	@Deprecated
	public static void addIMessages(Collection<IMessage> msgs) {
		addMessages(msgs);
	}
	
	/**
	 * @param msgs
	 */
	public static void addMessages(Collection<IMessage> msgs) {
		for (IMessage msg : msgs) {
			addMessage(msg);
		}
	}

	/**
	 * Used to add a message to application context.
	 * 
	 * @param msg {@linkplain FacesMessage}
	 */
	public static void addMessage(FacesMessage msg) {
		log.debug("Message on FacesContext");
		addMessage(null, msg.getDetail(), msg.getSummary(), msg.getSeverity());
	}

	/**
	 * Used to add a message to application context and to add error to
	 * transaction context.
	 * 
	 * @param msg {@linkplain FacesMessage}
	 * @param error {@linkplain Throwable}
	 */
	public static void addMessage(FacesMessage msg, Throwable error) {
		log.debug("Message on FacesContext and Throwable");
		addMessage(msg);
		ContextLocator.getInstance().getTransactionContext().add(error);
	}

	/**
	 * Used to add a message to application context. It receives as parameter
	 * the component id to which it will be bound, detail and summary of message
	 * and its severity.
	 * 
	 * Severity must be Severity.INFO or Severity.WARNING or Severity.ERROR or
	 * Severity.FATAL.
	 * 
	 * @param uiId		component id to which the message will be bound
	 * @param detail	message detail
	 * @param summary	message summary
	 * @param severity	message severity
	 */
	public static void addMessage(String uiId, String detail, String summary,
			FacesMessage.Severity severity) {
		
		FacesMessage facesMessage = new FacesMessage();
		if (detail != null) {
			facesMessage.setDetail(detail);
		}
		if (severity == null) {
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		} else {
			facesMessage.setSeverity(severity);
		}
		if (summary != null)
			facesMessage.setSummary(summary);

		FacesContext.getCurrentInstance().addMessage(uiId, facesMessage);
	}

	/**
	 * @param uiId
	 * @param detail
	 * @param summary
	 * @param severity
	 * @param error
	 */
	public static void addMessage(String uiId, String detail, String summary, FacesMessage.Severity severity, Throwable error) {
		log.debug("Message on FacesContext and Throwable");
		addMessage(uiId, detail, summary, severity);
		ContextLocator.getInstance().getTransactionContext().add(error);
	}

	/**
	 * Used to add a message to application context. It receives as parameter
	 * only the message detail.
	 * 
	 * @param summary	message summary
	 */
	public static void addMessage(String summary) {
		addMessage(null, null, summary, null);
	}

	/**
	 * Used to add a message to application context and to add error to
	 * transaction context.
	 * 
	 * @param summary	message summary
	 * @param error		throwable
	 */
	public static void addMessage(String summary, Throwable error) {
		addMessage(summary);
		ContextLocator.getInstance().getTransactionContext().add(error);
	}

	/**
	 * Used to add a message to application context. It receives as parameter
	 * the {@linkplain IMessage} and the {@linkplain Throwable}.
	 * 
	 * @param message {@linkplain IMessage}
	 */
	public static void addMessage(IMessage message, Object...args) {
		FacesMessage fasesMsg = new FacesMessage();		
		String label = getMessageBundle(message);		
		fasesMsg.setSummary(label);
		fasesMsg.setDetail(formatMessage(label,args));
		fasesMsg.setSeverity(getSeverity(message.getSeverity()));
		addMessage(fasesMsg);
	}
	
	/**
	 * If the message locale is not equals the user locale and there is a key then
	 * this method look in the properties file with resourceName from message and
	 * the composition of the locale.
	 * 
	 * If there is no key, returns an key not found message.
	 * "?? key (" + message.getKey() + ") not found ??"
	 * 
	 * @param message {@link IMessage} Message
	 * @return {@link String} Label the message
	 */
	public static String getMessageBundle(IMessage message) {
		ManagedBeanUtilConfig config = ConfigurationLoader.load(ManagedBeanUtilConfig.class);
		
		String sMessage = "";
        ResourceBundle bundle = null;
		Locale usrLocale = getUserLocale();
		
		switch (config.getMessagePriority()) {
		case MESSAGE_BUNDLE:
            String messageBundle = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
            if (messageBundle != null) {
                bundle = ResourceBundle.getBundle(messageBundle, usrLocale);
            }
			
	        if (bundle != null){
	            try{
	                sMessage = bundle.getString(message.getKey());
	            }catch (MissingResourceException e) {
	                sMessage = "?? key (" + message.getKey() + ") not found ??";
	            }
	        }else{
	            sMessage = message.getLabel();
	        }
	        break;
		case MESSAGE_CONTEXT:
			if (usrLocale!=null && !usrLocale.equals(message.getLocale()) && message.getKey() != null && message.getResourceName() != null) {
				bundle = ResourceBundle.getBundle(message.getResourceName(), usrLocale);
				if (bundle != null) {
					try {
						sMessage = bundle.getString(message.getKey());
					} catch (MissingResourceException e) {
						sMessage = "?? key (" + message.getKey() + ") not found ??";
					}
				}
			} else {
				sMessage = message.getLabel();
			}
			break;
		default:
			sMessage = config.getMessagePriority()+" not found";
			break;
		}
		return sMessage;
	}

	/**
	 * Returns the locale for current user.
	 */
	private static Locale getUserLocale(){
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}
	
	/**
     * From a string message with {0} parameters, returns
     * a message replaced with de arguments.
     * 
     * Eg.: this is a message for {0} and {1}
     * When call this method and passing this message and 
     * arguments "param0" and "param1" results:
     *  this is a message for param0 and param1
     * 
     * @param message
     * @param arguments
     */
    public static String formatMessage(String message, Object... arguments){
    	MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(message);
		return formatter.format(arguments);
    }

	/**
	 * Used to add a message to application context and to add error to
	 * transaction context. It receives as parameter the {@linkplain IMessage}
	 * and the {@linkplain Throwable}.
	 * 
	 * @param message {@linkplain IMessage}
	 * @param error		{@linkplain Throwable}
	 */
	public static void addMessage(IMessage message, Throwable error) {
		addMessage(message);
		ContextLocator.getInstance().getTransactionContext().add(error);
	}

	/**
	 * Used to add a message to application context. It receives as parameter
	 * the message detail and the severity that it has.
	 * 
	 * Severity must be Severity.INFO or Severity.WARNING or Severity.ERROR or
	 * Severity.FATAL.
	 * 
	 * @param summary	message summary
	 * @param severity	message severity
	 */
	public static void addMessage(String summary, FacesMessage.Severity severity) {
		addMessage(null, null, summary, severity);
	}

	/**
	 * Used to add a message to application context and to add error to
	 * transaction context. It receives as parameter the message detail and the
	 * severity that it has.
	 * 
	 * Severity must be Severity.INFO or Severity.WARNING or Severity.ERROR or
	 * Severity.FATAL.
	 * 
	 * @param summary	message summary
	 * @param severity	message severity
	 * @param error
	 */
	public static void addMessage(String summary, FacesMessage.Severity severity, Throwable error) {
		addMessage(summary, severity);
		ContextLocator.getInstance().getTransactionContext().add(error);
	}

	/**
	 * Used to add a message to application context. It receives as parameter
	 * the component id to which it will be bound and the message detail.
	 * 
	 * @param uiId		component id to which the message will be bound.
	 * @param summary	message summary
	 */
	public static void addMessage(String uiId, String summary) {
		addMessage(uiId, null, summary, null);
	}

	/**
	 * Used to add a message to application context. It receives as parameter
	 * the component id to which it will be bound, the message detail and the
	 * severity that it has.
	 * 
	 * Severity must be Severity.INFO or Severity.WARNING or Severity.ERROR or
	 * Severity.FATAL.
	 * 
	 * @param uiId		component id to which the message will be bound.
	 * @param summary	message summary
	 * @param severity	message severity
	 */
	public static void addMessage(String uiId, String summary, FacesMessage.Severity severity) {
		addMessage(uiId, null, summary, severity);
	}

	/**
	 * Used to add a message to context application. It receives as parameter
	 * the component id to which it will be bound, the detail and summary of
	 * message.
	 * 
	 * @param uiId		component id to which the message will be bound.
	 * @param detail	message detail
	 * @param summary	message summary
	 */
	public static void addMessage(String uiId, String detail, String summary) {
		addMessage(uiId, detail, summary, null);
	}

	/**
	 * Returns the current HTTP session of user.
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	/**
	 * Returns the application context.
	 */
	public static ServletContext getServletContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (ServletContext) facesContext.getExternalContext().getContext();
	}

	/**
	 * Returns the request in processing.
	 */
	public static HttpServletRequest getRequest() {
		if (request == null) {
			return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		} else {
			return request;
		}
	}

	/**
	 * @param request
	 */
	static void setRequest(HttpServletRequest request) {
		ManagedBeanUtil.request = request;
	}

	/**
	 * Returns the Remote IP.
	 * 
	 * @return ip
	 */
	public static String getRemoteIP() {
		return getRequest().getRemoteAddr();
	}

	/**
	 * @param response
	 */
	static void setResponse(HttpServletResponse response) {
		ManagedBeanUtil.response = response;
	}

	/**
	 * Returns the response of request in processing.
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletResponse getResponse() {
		if (response == null) {
			return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		} else {
			return response;
		}
	}
	
	/**
	 * Returns the externalContext in processing.
	 * 
	 * @return HttpServletRequest
	 */
	public static ExternalContext getExternalContext() {
		if (externalContext == null) {
			return FacesContext.getCurrentInstance().getExternalContext();
		} else {
			return externalContext;
		}
	}
	
	/**
	 * @param externalContext
	 */
	static void setExternalContext(ExternalContext externalContext) {
		ManagedBeanUtil.externalContext = externalContext;
	}
	
	/**
	 * Returns a certain managed bean found in any application scope.
	 * 
	 * It must pass in as parameter the managedbean name expressed through JSF
	 * EL. For example, if you wish to recover the managedbean ExampleMB, you
	 * must pass in as parameter a String "#{exampleMB}", where "exampleMB" is
	 * the name corresponding to managedbean declared in the file
	 * faces-config.xml of application.
	 * 
	 * @param name managedbean name expressed in JSF EL.
	 * @return Object
	 */
	@SuppressWarnings("deprecation")
	public static Object getManagedBean(String name) {
		return FacesContext.getCurrentInstance().getApplication().createValueBinding(name).getValue(FacesContext.getCurrentInstance());
	}
	
	public static Object getManagedBean(String name, Class<?> managedBeanClass) {
		ValueExpression ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(
						FacesContext.getCurrentInstance().getELContext(),
						name , managedBeanClass);

		return ve.getValue(FacesContext.getCurrentInstance().getELContext());
	}

	/**
	 * @param url
	 * @param usePort
	 * @param verifyJs
	 */
	public static String getFullUrl(String url, boolean usePort, boolean verifyJs) {
		
		StringBuffer sbOut = new StringBuffer(getProtocol());

		sbOut.append(getRequest().getLocalName());
		if (usePort) {
			sbOut.append(":");
			sbOut.append(getRequest().getLocalPort());
		}
		sbOut.append(url);

		if (!verifyJs) {
			sbOut.append("?");
			sbOut.append("verifyJs");
			sbOut.append("=");
			sbOut.append("1");
		}

		return sbOut.toString();
	}

	/**
	 * return Protocol
	 */
	public static String getProtocol() {
		HttpServletRequest request = getRequest();
		if (request != null) {
			String protocol = request.getScheme();
			if (protocol != null)
				return protocol + "://";
		}
		return "http://";
	}

	/**
	 * @param url
	 * @param usePort
	 * @param verifyJs
	 * @return full url with context path
	 */
	public static String getFullUrlWithContextPath(String url, boolean usePort, boolean verifyJs) {
		
		StringBuffer sbOut = new StringBuffer(getProtocol());

		if (getRequest().getLocalName().equals("0.0.0.0"))
			sbOut.append("localhost");
		else
			sbOut.append(getRequest().getLocalName());

		if (usePort) {
			sbOut.append(":");
			sbOut.append(getRequest().getLocalPort());
		}
		sbOut.append(getRequest().getContextPath());
		sbOut.append(url);

		if (!verifyJs) {
			sbOut.append("?");
			sbOut.append("verifyJs");
			sbOut.append("=");
			sbOut.append("1");
		}

		return sbOut.toString();
	}
	
}
