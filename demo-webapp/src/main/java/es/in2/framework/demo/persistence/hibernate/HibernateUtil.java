package es.in2.framework.demo.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import es.in2.framework.demo.core.context.ContextLocator;
import es.in2.framework.demo.core.transaction.ITransactionContext;
import es.in2.framework.demo.core.transaction.ITransactionResource;

/**
 * <p>
 * Helper class to handle Hibernate resources.
 * </p>
 * <p>
 * The implementation of ITransactionContext is necessary for perfect runs of
 * this class.
 * </p>
 * 
 * @author CETEC/CTJEE
 */
public class HibernateUtil {

	private SessionFactory sessionFactory = null;
	private ITransactionContext transactionContext = ContextLocator
			.getInstance().getTransactionContext();
	private Configuration configuration = null;
	private boolean configured = false;

	private static final HibernateUtil instance = new HibernateUtil();

	private static Logger log = Logger.getLogger(HibernateUtil.class);

	public void setTransactionContext(ITransactionContext transactionContext) {
		this.transactionContext = transactionContext;
	}

	private HibernateUtil() {
		configured = false;
	}

	public static HibernateUtil getInstance() {
		return instance;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * Initialize hibernate configuration. Loader the config files for choose
	 * the type of map class (hbm or annotations)
	 */
	public void configure() {
		log.debug("Hibernate starting configuration...");

		if (configuration == null) {
			log.debug("Hibernate mapping...");
			configuration = new AnnotationConfiguration();
		}

		if (!configured) {
			try {
				configuration.configure();
				log.debug("Hibernate configured.");
			} catch (HibernateException e) {
				throw new PersistenceHibernateException(
						"Error on configure hibernate.", e);
			}
			try {
				sessionFactory = configuration.buildSessionFactory();
				log.debug("Session Factory created.");
			} catch (HibernateException e) {
				throw new PersistenceHibernateException(
						"Error on build hibernate session factory.", e);
			}
			configured = true;
		}

	}

	public Session getSession() throws PersistenceHibernateException {
		Session result = getSession(true);
		return result;
	}

	/**
	 * Seek hibernate session on the ITransactionContext resource. If any access
	 * to repository before are invoked, the hibernate session is on
	 * ITransactionContext implementation.
	 * 
	 * @return Hibernate session from transaction context
	 */
	private Session getSessionFromTransactionContext() {
		ITransactionResource resource = null;
		HibernateTransactionResource hibernateResource = null;

		Session result = null;

		if (transactionContext == null)
			throw new PersistenceHibernateException(
					"Without Transaction Context.");

		if (transactionContext.hasResource()) {
			resource = transactionContext.getResource();

			if (resource instanceof HibernateTransactionResource) {
				hibernateResource = (HibernateTransactionResource) resource;

				result = hibernateResource.getSession();
			} else {
				throw new PersistenceHibernateException(
						"Transaction context NOT contain a HibernateTransactionResource resource.");
			}
		}

		return result;

	}

	/**
	 * Primeiro verifica se existe alguma sessao iniciada anteriormente no
	 * contexto de transacao. Caso positivo, verifica-se se a sessao encontrada
	 * está fechada ou não devidamente conectada, só então é devolvida a sessão.
	 * Caso negativo, a tentativa de criacao de uma nova sessao será balizada
	 * pelo parâmetro create.
	 * 
	 * @param create
	 * @return Hibernate session
	 * @throws PersistenceHibernateException
	 */
	public Session getSession(boolean create)
			throws PersistenceHibernateException {
		Session result = getSessionFromTransactionContext();

		if (result == null && create) {
			result = createSession();
		}

		return result;
	}

	private Transaction getTransaction() throws PersistenceHibernateException {
		Transaction result = null;
		Session session = getSession(false);
		if (session != null)
			result = session.getTransaction();
		return result;
	}

	/**
	 * Commit all modify on persistent objects. Close the session is invoked
	 * after commit.
	 * 
	 * @throws PersistenceHibernateException
	 */
	public void commit() throws PersistenceHibernateException {
		log.debug("Hibernate commit invoked...");
		Session session = getSession(false);
		if (session != null) {
			Transaction tx = getTransaction();
			try {
				if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
					tx.commit();
					log.debug("Hibernate transaction commited - OK!");
				}
			} catch (HibernateException e) {
				rollback();
				throw new PersistenceHibernateException(
						"Error on hibernate commit database.", e);
			} finally {
				closeSession();
			}
		}
	}

	/**
	 * Close all connections with repository and clean hibernate session.
	 * 
	 * @throws PersistenceHibernateException
	 */
	private void closeSession() throws PersistenceHibernateException {
		Session session = getSession(false);
		if (session != null) {
			session.close();
			log.debug("Hibernate Session Closed.");
		}
	}

	/**
	 * Rollback all modify on persistent objects. Close the session is invoked
	 * after commit.
	 * 
	 * @throws PersistenceHibernateException
	 */
	public void rollback() throws PersistenceHibernateException {
		log.debug("Hibernate rollback invoked...");
		if (getSession(false) != null) {
			Transaction tx = getTransaction();
			try {
				if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
					tx.rollback();
					log.debug("Hibernate rollback - OK!");
				}
			} catch (HibernateException e) {
				throw new PersistenceHibernateException(
						"Error on hibernate rollback.", e);
			} finally {
				closeSession();
			}
		}
	}

	/**
	 * Configure hibernate, make a session, put hibernate resource on
	 * ITransacationContext, define the flush mode for COMMIT and initialize
	 * transaction.
	 * 
	 * @return
	 * @throws PersistenceHibernateException
	 */
	private Session createSession() throws PersistenceHibernateException {
		Session result = getSessionFromTransactionContext();
		if (result == null || !result.isOpen() || !result.isConnected()) {
			try {

				configure();

				result = sessionFactory.openSession();

				if (transactionContext != null) {
					transactionContext
							.setResource(new HibernateTransactionResource(
									result));
					log.debug("Hibernate resource transaction on context.");
				} else {
					throw new PersistenceHibernateException(
							"Initialize transaction context.");
				}

			} catch (HibernateException e) {
				throw new PersistenceHibernateException(
						"Error on open hibernate session.", e);
			}

			result.setFlushMode(FlushMode.COMMIT);

			beginTransaction();

		}
		return result;
	}

	private void beginTransaction() throws PersistenceHibernateException {
		log.debug("Starting hibernate transaction...");
		Transaction tx = getTransaction();

		if (tx != null) {
			Session session = getSession(false);
			try {
				tx = session.beginTransaction();
				log.debug("Hibernate transaction start ok.");
			} catch (HibernateException e) {
				throw new PersistenceHibernateException(
						"Error on starting hibernate transaction.", e);
			}

		}
	}

	protected Object clone() throws PersistenceHibernateException {
		throw new PersistenceHibernateException("HibernateUtil not cloneable.");
	}

}
