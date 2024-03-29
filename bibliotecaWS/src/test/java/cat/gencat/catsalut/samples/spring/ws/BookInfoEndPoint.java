package cat.gencat.catsalut.samples.spring.ws;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import sun.rmi.runtime.Log;
import cat.gencat.catsalut.samples.spring.ws.entity.BooksInfoRequest;
import cat.gencat.catsalut.samples.spring.ws.entity.BooksInfoResponse;
import cat.gencat.catsalut.samples.spring.ws.entity.Libro;

/**
 * 
 * @author Juan C. Fern�ndez
 * @see http://www.juancarlosfernandez.net
 */
public class BookInfoEndPoint extends AbstractDomPayloadEndpoint {
	private Log logger = LogFactory.getLog(BookInfoEndPoint.class);

	private IRequestProcessor procesor;

	/**
	 * Sera inyectado por Spring
	 */
	public void setProcesor(IRequestProcessor procesor) {
		this.procesor = procesor;

	}

	/*
	 * @seeorg.springframework.ws.server.endpoint.AbstractDomPayloadEndpoint#
	 * invokeInternal(org.w3c.dom.Element, org.w3c.dom.Document)
	 */
	protected Element invokeInternal(Element domRequest, Document document)
			throws Exception {
		
		Element domResponse = null;

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Petición de consulta de libros");
			}

			BooksInfoRequest request = this.xmlToInfoRequest(domRequest);

			if (logger.isDebugEnabled()) {
				logger.debug("PETICION: categoria=" + request.getCategoria()
						+ ", nivel=" + request.getNivel());
			}

			BooksInfoResponse response = procesor.process(request);

			if (logger.isDebugEnabled()) {
				int numLibros = 0;
				if (response.getLibros() != null) {
					numLibros = response.getLibros().size();
				}
				logger.debug("RESPUESTA: Número de libros: " + numLibros);
			}

			domResponse = this.responseToXml(response, document);
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return domResponse;
	}

	/**
	 * @param request
	 *            Elemento XML <BooksInfoRequest ...>
	 * @return Genera una instancia de BooksInfoRequest a partir de un elemento
	 *         xml
	 */
	private BooksInfoRequest xmlToInfoRequest(Element request) {
		String categoria = request.getElementsByTagName("categoria").item(0)
				.getFirstChild().getNodeValue();
		String nivel = request.getElementsByTagName("nivel").item(0)
				.getFirstChild().getNodeValue();

		BooksInfoRequest bookInfoRequest = new BooksInfoRequest();

		bookInfoRequest.setCategoria(categoria);
		bookInfoRequest.setNivel(nivel);

		return bookInfoRequest;
	}

	/**
	 * @param request
	 *            Elemento XML <BooksInfoRequest ...>
	 * @return Genera una instancia de BooksInfoRequest a partir de un elemento
	 *         xml
	 */
	private Element responseToXml(BooksInfoResponse response, Document document) {
		Element root = document.createElementNS(
				"http://www.adictosaltrabajo.com/spring/ws/schemas",
				"BooksInfoResponse");
		List<Libro> libros = response.getLibros();

		if (libros != null) {
			Iterator<Libro> iteLibros = null;
			Libro libro = null;
			Element domLibro = null;

			iteLibros = libros.iterator();
			while (iteLibros.hasNext()) {
				libro = iteLibros.next();
				domLibro = this.bookToXml(document, libro);
				root.appendChild(domLibro);
			}
		}

		return root;
	}

	/**
	 * 
	 * @param document
	 *            Document para construir el DOM
	 * @param libro
	 *            Objeto a convertir a XML
	 * @return Devuelve el objeto libro en XML.
	 */
	private Element bookToXml(Document document, Libro libro) {
		Element domLibro = document.createElement("libro");

		this.addHijo(document, domLibro, "editorial", libro.getEditorial());
		this.addHijo(document, domLibro, "titulo", libro.getTitulo());
		this.addHijo(document, domLibro, "paginas", String.valueOf(libro
				.getPaginas()));
		this.addHijo(document, domLibro, "precio", String.valueOf(libro
				.getPrecio()));

		return domLibro;
	}

	/**
	 * Añade una nueva propiedad <tag>valor</tag> al elemento <padre>.
	 * 
	 * @param document
	 *            Para crear elementos
	 * @param padre
	 *            La nueva propiedad será agregada a este elemento
	 * @param tag
	 *            Nombre de la propiedad
	 * @param valor
	 *            Valor de la propiedad
	 */
	private void addHijo(Document document, Element padre, String tag,
			String valor) {
		Element domNombre = document.createElement(tag);
		Text domValor = document.createTextNode(valor);

		domNombre.appendChild(domValor);
		padre.appendChild(domNombre);
	}
}
