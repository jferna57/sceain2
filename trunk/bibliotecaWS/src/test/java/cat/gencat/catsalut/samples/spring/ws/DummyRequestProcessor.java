package cat.gencat.catsalut.samples.spring.ws;

import net.juancarlosfernandez.samples.spring.ws.entity.BooksInfoRequest;
import net.juancarlosfernandez.samples.spring.ws.entity.BooksInfoResponse;
import net.juancarlosfernandez.samples.spring.ws.entity.Libro;

/**
 * Implementacion dummy de IRequestProcesor
 * @author Juan C. Fern�ndez
 * @see http://www.juancarlosfernandez.net
 */
public class DummyRequestProcessor implements IRequestProcessor {

	/* 
	 * @see com.autentia.tutoriales.spring.ws.IRequestProcessor#process(com.autentia.tutoriales.spring.ws.entity.BooksInfoRequest)
	 */
	public BooksInfoResponse process(BooksInfoRequest request) {
		BooksInfoResponse response = new BooksInfoResponse();
		Libro			  libro    = null;
		
		for (int i = 0; i < 5; i++){
			libro = new Libro();
			
			libro.setTitulo("Titulo libro " + i);
			libro.setEditorial("Editorial libro " + i);
			libro.setPaginas(100 + i);
			libro.setPrecio(50 + i);
			
			response.addLibro(libro);			
		}
		
		return response;
	}
}
