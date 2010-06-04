package cat.gencat.catsalut.samples.spring.ws.entity;

/**
 * Representacion de un libro
 * @author Juan C. Fernandez
 * @see http://www.juancarlosfernandez.net
 */
public class Libro {
	private String editorial;
	private String titulo;
	private int	   paginas;
	private int	   precio;
		
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getPaginas() {
		return paginas;
	}
	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
}
