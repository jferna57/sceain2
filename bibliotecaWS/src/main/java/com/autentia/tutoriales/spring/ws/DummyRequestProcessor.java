package com.autentia.tutoriales.spring.ws;

import com.autentia.tutoriales.spring.ws.entity.BooksInfoRequest;
import com.autentia.tutoriales.spring.ws.entity.BooksInfoResponse;
import com.autentia.tutoriales.spring.ws.entity.Libro;

/**
 * Implementación dummy de IRequestProcesor
 * @author Carlos García. Autentia.
 * @see http://www.mobiletest.es 
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
