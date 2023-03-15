package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * This class setups the relationship between each port we want to listen to and
 * its relative service.
 */
public class Database {

	private Map<Integer, String> map;

	public Database(Map<Integer, String> map) {
		this.map = map;
	}

	/**
	 * This method fills an input Map with each port the server listens to and pairs
	 * the port to a string. Each string represents a service. 
	 * @param map - It's the map that needs to be filled.
	 * 
	 */
	public void fillMap() {
		map.put(21, "ftp");
		map.put(22, "ssh");
		map.put(25, "smtp");
		map.put(80, "http");
		map.put(23, "telnet");
		map.put(853, "dns");
		map.put(88, "kerberos");
		map.put(119, "nntp");
		map.put(137, "netbios");
		map.put(143, "imap");
		map.put(161, "snmp");
		map.put(389, "ldap");
		map.put(445, "microsoft-ds");
		map.put(554, "rtsp");
		map.put(119, "nntp");
		map.put(631, "ipp");
		map.put(110, "pop3");
	}	
	
	/**
	 * This method generates a random location and accesses a valid file from the
	 * line in that location. The accessed line is then returned.
	 * 
	 * @param buffer - It's the file to access.
	 * @param path   - The file's path.
	 * 
	 * @return randomLine: A random line obtained from the file buffer in path.
	 * 
	 */
	public final String getRandomLineFromFile(BufferedReader buffer, String path) throws IOException {
		if (buffer == null) {
			throw new IllegalArgumentException("Input file is not valid. File: " + buffer);
		}

		if (path == null) {
			throw new IllegalArgumentException("Input path is not valid. Path: " + path);
		}
		int count = 0;
		try {
			while (buffer.readLine() != null) {
				count++;
			}
		} catch (IOException e) {
			System.out.println("Error while reading from file " + path + ". Cannot read more from file.");
		}
		buffer.close();
		double randomLocation = (Math.random() * count);
		String randomLine = Files.readAllLines(Paths.get(path)).get((int) randomLocation);
		return randomLine;
	}
}