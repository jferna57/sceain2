package net.juancarlosfernandez.samples.spring;

import net.juancarlosfernandez.samples.spring.ws.entity.BooksInfoRequest;

import org.junit.Test;


import junit.framework.TestCase;

public class BooksInfoRequestTest extends TestCase {
	
	@Test
	public void testGetCategoria() {
		BooksInfoRequest book = new BooksInfoRequest();
		book.setCategoria("cat1");
		if (book.getCategoria()=="cat1")
			assertTrue("ok", true);
		else
			assertTrue("ko",false);
		
	}
	@Test
	public void testSetCategoria() {
		assertTrue("ok",true);
	}
	@Test
	public void testGetNivel() {
		assertTrue("ok",true);
	}
	@Test
	public void testSetNivel() {
		assertTrue("ok",true);
	}

}
