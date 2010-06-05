package br.gov.framework.demoiselle.core.layer;

/**
 * Represents the persistence layer object responsible for access to data. IDAO classes don't access other layers of
 * framework, only the persistence layer.
 * 
 * @author CETEC/CTJEE
 */
public interface IDAO<E> {

	/**
	 * Verifies that there is an element in the storage.
	 * 
	 * @param pojo specific implementation of IDAO.
	 * @return true if pojo exists
	 */
	public boolean exists(E pojo);

	/**
	 * Creates a new element in the storage. May be, for example, a record in a database table.
	 * 
	 * @param pojo specific implementation of IDAO.
	 * @return pojo
	 */
	public Object insert(E pojo);

	/**
	 * Removes a element of the storage. The element may be a record in a database table.
	 * 
	 * @param pojo
	 */
	public void remove(E pojo);

	/**
	 * Brings up to date a element of the storage. The element may be a record in a database table.
	 * 
	 * @param pojo
	 *            specific implementation of IDAO.
	 */
	public void update(E pojo);

}
