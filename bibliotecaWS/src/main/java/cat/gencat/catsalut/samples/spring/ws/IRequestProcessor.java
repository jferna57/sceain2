package cat.gencat.catsalut.samples.spring.ws;

import net.juancarlosfernandez.samples.spring.ws.entity.BooksInfoRequest;
import net.juancarlosfernandez.samples.spring.ws.entity.BooksInfoResponse;

/**
 * Tratamiento de una petición de búsqueda de libros
 * @author Juan C. Fern�ndez
 * @see http://www.juancarlosfernandez.net
 */
public interface IRequestProcessor {
	/**
	 * Procesa la petición de consulta
	 * @param request Datos de la consulta
	 * @return Devuelve la respuesta
	 */
	public BooksInfoResponse process(BooksInfoRequest request);
}
