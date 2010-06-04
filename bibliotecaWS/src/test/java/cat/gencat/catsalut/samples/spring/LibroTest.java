package cat.gencat.catsalut.samples.spring;

import static org.junit.Assert.*;

import net.juancarlosfernandez.samples.spring.ws.entity.Libro;

import org.junit.Test;

/**
 * asdfadfasf
 * @author juancarlosf
 *
 */
public class LibroTest {
	
	Libro libro = new Libro();
	
	/**
	 * return : void
	 */
	@Test
	public void testSetGetEditorial() {
		libro.setEditorial("editorial");
		if (libro.getEditorial().equalsIgnoreCase("editorial"))
			assertTrue("Not yet implemented",true);
		else
			fail("Error!!!");
	}

	/**
	 * return : void
	 */
	@Test
	public void testSetGetTitulo() {
		libro.setTitulo("titulo");
		if (libro.getTitulo().equalsIgnoreCase("titulo"))
			assertTrue("OK",true);
		else
			fail("Error!!!");
	}

	@Test
	public void testSetGetPaginas() {
		libro.setPaginas(1);
		if (libro.getPaginas()==1)
			assertTrue("OK",true);
		else
			fail("Error!!!");
	}

	@Test
	public void testSetGetPrecio() {
		libro.setPrecio(100);
		if (libro.getPrecio()==100)
			assertTrue("OK",true);
		else
			fail("Error!!!");
	}
}
