package net.juancarlosfernandez.samples.spring.ws.entity;

/**
 * Representacion de una peticion.
 * @author Juan C. Fernández
 * @see http://www.juancarlosfernandez.net
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
