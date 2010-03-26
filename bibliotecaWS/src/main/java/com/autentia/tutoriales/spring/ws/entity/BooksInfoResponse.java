package com.autentia.tutoriales.spring.ws.entity;

/**
 * Representación la respuesta a una petición BooksInforRequest.
 * @author Carlos García. Autentia
 * @see http://www.mobiletest.es
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
