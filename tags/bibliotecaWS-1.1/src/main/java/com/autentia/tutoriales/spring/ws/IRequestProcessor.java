package com.autentia.tutoriales.spring.ws;

import com.autentia.tutoriales.spring.ws.entity.BooksInfoRequest;
import com.autentia.tutoriales.spring.ws.entity.BooksInfoResponse;

/**
 * Tratamiento de una petición de búsqueda de libros
 * @author Carlos García. Autentia.
 * @see http://www.mobiletest.es 
 */
public interface IRequestProcessor {
	/**
	 * Procesa la petición de consulta
	 * @param request Datos de la consulta
	 * @return Devuelve la respuesta
	 */
	public BooksInfoResponse process(BooksInfoRequest request);
}
