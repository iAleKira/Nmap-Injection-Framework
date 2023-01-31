package server;

import java.io.IOException;

import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.js.Provider;

import injector.InjectionHandler;

/**
 * This class handles the generation of the response by the server.
 */
public class ResponseGenerator {
	
	/**
	 * This method generates the response from the server to the client.
	 * @param toInject - the string to inject
	 * @param injector - the InjectionHandler injector that will aid the injection.
	 * @param payload  - the payload to inject. 
	 * @param random - Random object to allow parsing.
	 * @return injected - the string injected with the payload
	 */
	
	public String generateResponse(String toInject, InjectionHandler injector, String payload, RandomGen random) throws IOException {
		String restricted = injector.restrict(toInject);
		String captureInjection = injector.extractInjectionGroup(restricted, payload);
		restricted = restricted.replace(captureInjection, "dontparsethis").replace("\\r", "\r").replace("\\n", "\n");
		RegExpGen generator = Provider.forEcmaScript().matchingExact(restricted);
		String injected = generator.generate(random);	
		injected = injected.replace("dontparsethis", payload);
		return injected;
	}
}