package es.in2.framework.demo.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import es.in2.framework.demo.core.transaction.ITransactionResource;

/**
 * <p>
 * Resource of framework transaction context.
 * See CORE Logical module for more details.
 * </p>
 * <p>  
 * HibernateUtil start the session with repository and put the 
 * resource on one ITransactionContext from ContextLocator object.
 * </p>
 * 
 * @author CETEC/CTJEE
 * @see ITransactionResource
 */
public class HibernateTransactionResource implements ITransactionResource {
	
	private Session session = null;
	
	private static Logger log = Logger.getLogger(HibernateTransactionResource.class);
	
	/**
	 * Commit hibernate session and close the connection with repository 
	 *@see br.gov.framework.demoiselle.core.transaction.ITransactionResource#commit()
	 */
	public void commit() {
		log.debug("Hibernate Transaction Resource - Commit.");
		HibernateUtil.getInstance().commit();
	}
	
	/**
	 * Rollback all modify on persist objects
	 *   
	 *@see br.gov.framework.demoiselle.core.transaction.ITransactionResource#rollback()
	 */
	public void rollback() {
		log.debug("Hibernate Transaction Resource - Rollback.");
		HibernateUtil.getInstance().rollback();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public HibernateTransactionResource(Session session) {
		this.session = session;
	}
	 
}