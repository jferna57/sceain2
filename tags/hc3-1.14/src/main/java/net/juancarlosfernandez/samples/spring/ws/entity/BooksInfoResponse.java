package net.juancarlosfernandez.samples.spring.ws.entity;

/**
 * Representacion la respuesta a una peticion BooksInforRequest.
 * @author Juan C. Fernández
 * @see http://www.juancarlosfernandez.net
 */
public class BooksInfoResponse {
	private java.util.ArrayList<Libro> libros;
	
	public BooksInfoResponse(){
		this.libros = new java.util.ArrayList<Libro>();
	}

	public void addLibro(Libro libro){
		this.libros.add(libro);
	}
	
	public java.util.List<Libro> getLibros() {
		return this.libros;
	}
}
