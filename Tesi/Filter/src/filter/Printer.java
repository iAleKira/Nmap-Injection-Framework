package filter;

import java.io.*;

import injector.InjectionHandler;

/**
 * @author Alessandro Bonfiglio.
 * 
 *         This class is All_payloads_filter.jar main class. It filters an
 *         inputFile line by line, into an outputFile by a certain payload. User
 *         can check whether a file can be injected with a payload of choice or
 *         not.
 * 
 *         Number of input arguments. 0: uses default settings. 1: uses default paths and custom payload. 
 *         3: uses user input files and payload.
 * 
 *         It then implements the filtering algorithm. An ExploitBuilder is
 *         needed, therefore it uses ExploitBuilder.jar library. The input file
 *         is read line by line, and for each line the filter checks if it can
 *         be filtered, according to the implementation, by the method
 *         {@link canBeFiltered}. If the return value is true, it then proceeds
 *         restricting the string in between pipes symbols | | and then inject
 *         it with the payload. If the injection succeeds, then the original
 *         string from the file is saved in the output file. If the injection
 *         fails, resulting in an empty string, it discards it.
 * 
 * @throws IOException: All_payloads_filter.jar, as default, is meant to be
 *                      launched from a fixed directory together with the file
 *                      it filters. - If the file is not found in the same
 *                      directory as Filter.jar: a FileNotFoundException is
 *                      triggered and the executable exits with error: "File
 *                      top-services-probes.txt not found. File needs to be
 *                      included...". - If the input file, default:
 *                      top-services-probes.txt, is found in the same directory
 *                      as Filter.jar but there is no output file then a
 *                      FileNotFoundException is triggered and the executable
 *                      exits with error: "Output path needs to exist..".
 */
public class Printer {

	private static Filter filter;

	public static final void main(String[] args) throws IOException {

		switch (args.length) {
		case 0:
			filter = new Filter("top-services-probes.txt", "injectableProbes//injectable-service-probes.txt",
					"<>|ale=/'-()[] 5rt%\"’\\$&#x`*~+-_.,:;?!@OR^{}¼¾¢0£¹¬`§°ç€¶");
			break;	
		case 1:
			filter = new Filter("top-services-probes.txt", "injectableProbes//injectable-service-probes.txt",
					args[0]);
			break;
		case 3:
			filter = new Filter(args[0], args[1], args[2]);
			break;
		default:
			System.err.println(
					"Wrong number of arguments. Please insert three, one, or none.\n Usage: ./Filter.jar. inputFile outputFile \"payload\"");
			System.exit(1);
		}
		String payload = filter.getPayload();
		InjectionHandler handler = new InjectionHandler();
		try {
			BufferedReader readBuffer = new BufferedReader(new FileReader(filter.getInputPath()));
			String readLine;
			try {
				PrintStream outputFile = new PrintStream(new File(filter.getOutputPath()));
				System.setOut(outputFile);
				while (true) {
					readLine = readBuffer.readLine();
					if (readLine == null) {
						break;
					}
					if (filter.canBeFiltered(readLine, handler)) {
						String restricted = handler.restrict(readLine);
						String injectedGroup = handler.extractInjectionGroup(restricted, payload);
						if (!injectedGroup.isEmpty()) {
							System.out.println(readLine);
						}
					}
				}
				System.setOut(System.out);
				readBuffer.close();
			} catch (FileNotFoundException ex) {
				System.out.println("Output path needs to exist. Path: " + filter.getOutputPath() + ".");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + filter.getInputPath()
					+ " not found. File needs to be included in the same directory path as Filter.jar.");
		}
	}
}