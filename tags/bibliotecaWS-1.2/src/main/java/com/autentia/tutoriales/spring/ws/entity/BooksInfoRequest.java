package com.autentia.tutoriales.spring.ws.entity;

/**
 * Representación de una petición.
 * @author Carlos García. Autentia
 * @see http://www.mobiletest.es
 */
public class BooksInfoRequest {
	private String categoria;
	private String nivel;
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
}
