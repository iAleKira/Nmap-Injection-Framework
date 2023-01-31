package server;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.random.RandomBoundsGen;
import org.junit.Before;
import org.junit.Test;

import injector.InjectionHandler;

public class ResponseGeneratorTest {
	private ResponseGenerator generator;
	private InjectionHandler injector;
	private String payload;
	private String response;
	private RandomGen random;
	private String toInject;

	@Before
	public void setup() throws IOException{
		generator = new ResponseGenerator();
		injector = new InjectionHandler();
		payload = "<script>alert(1)</script>";
		random = new RandomBoundsGen();
		toInject = "match ssh m|^SSH--OpenSSH([^\\r\\n]+)\\n| p/OpenSSH/ v/$2 Debian $3/ i/protocol $1/ o/Linux/ cpe:/a:openbsd:openssh:$2/ cpe:/o:debian:debian_linux/ cpe:/o:linux:linux_kernel/a";
	}
	
	@Test
	public void testGeneratedResponseContainsPayload() throws IOException{
		response = generator.generateResponse(toInject, injector, payload,random);
		assertTrue(response.contains(payload));
		assertEquals("SSH--OpenSSH<script>alert(1)</script>\n", response);
	}	
	
	@Test
	public void testGeneratedResponseRegexIsSubstitued() throws IOException{
		String capture = injector.extractInjectionGroup(toInject, payload);
		response = generator.generateResponse(toInject, injector, payload,random);
		assertTrue(response.contains(payload));
		assertTrue(!response.contains(capture));
	}
	
	@Test
	public void testGeneratedResponseIsNotNull() throws IOException{
		response = generator.generateResponse(toInject, injector, payload,random);
		assertNotNull(response);
	}
	
	@Test
	public void testGeneratedResponseIsNotEmpty() throws IOException{
		response = generator.generateResponse(toInject, injector, payload,random);
		assertTrue(!response.isEmpty());
	}	
}