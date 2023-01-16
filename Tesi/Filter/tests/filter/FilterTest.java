package filter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import injector.InjectionHandler;

public class FilterTest {

	private Filter filter;
	private InjectionHandler handler;

	@Before
	public void setup() {
		filter = new Filter("input", "output", "<script>(1)</script>");
		handler = new InjectionHandler();
	}

	@Test
	public void testContructor() {
		assertEquals("input", filter.getInputPath());
		assertEquals("output", filter.getOutputPath());
		assertEquals("<script>(1)</script>", filter.getPayload());
	}

	@Test
	public void testFilterNullString() {
		boolean thrown = false;
		try {
			filter.canBeFiltered(null, handler);
		} catch (IllegalArgumentException e) {
			thrown = true;
			assertTrue(thrown);
			assertEquals("Cannot filter a null string.", e.getMessage());
		}
		assertThrows(IllegalArgumentException.class, () -> { 
			filter.canBeFiltered(null, handler);});
	}

	@Test
	public void testForbiddenNoBackslashPaerenthesis() {
		String backslashParenthesis = "220 ([-/.+\\w]+) FTP server \\(SecureTransport (\\d[-.\\w]+)\\) ready\\.\\r\\n";
		assertTrue(filter.includesForbiddenCharacters(backslashParenthesis));
		String noBackslashParenthesis = "220 ([-/.+\\w]+) FTP server SecureTransport (\\d[-.\\w]+)\\) ready\\.\\r\\n";
		assertFalse(filter.includesForbiddenCharacters(noBackslashParenthesis));
	}

	@Test
	public void testFobiddenDPR() {
		String DPR = "HTTP/1\\.0 401 Authorization Required\\r\\nWWW-Authenticate: BASIC realm=\\\"(DPR?-\\d[^)]+)\\\"\\r\\n\\r\\nPassword Error\\.";
		assertTrue(filter.includesForbiddenCharacters(DPR));
		String NoDPR = "HTTP/1\\.0 401 Authorization Required\\r\\nWWW-Authenticate: BASIC realm=\\\"(?-\\d[^)]+)\\\"\\r\\n\\r\\nPassword Error\\.";
		assertFalse(filter.includesForbiddenCharacters(NoDPR));
	}

	@Test
	public void testFobiddenLookahead() {
		String forbidden = "?:";
		assertTrue(filter.includesForbiddenCharacters(forbidden));
		String Notforbidden = "(200-.*\\r\\n)";
		assertFalse(filter.includesForbiddenCharacters(Notforbidden));
	}
	
	@Test
	public void testFobiddenBackslashX() {
		String forbidden = "\\x";
		assertTrue(filter.includesForbiddenCharacters(forbidden));
	}

	@Test
	public void testFobiddenLookAheadAndBackslashX() {
		String forbidden = "\\x ?:";
		assertTrue(filter.includesForbiddenCharacters(forbidden));
	}

	@Test
	public void testIncludesDollarNum() {
		String dollarNum = "match http m|^HTTP/1\\.[01] \\d\\d\\d .*\\r\\nServer: Ethernut ([^\\r\\n]+)\\r\\n| p/Ethernut demo httpd/ v/$1/ o|Nut/OS| cpe:/o:ethernut:nut_os/a";
		assertTrue(filter.includesDollarNum(dollarNum));
		String noDollarnum = "match ftp m|^220-.*\\r\\n220-\\r\\n220 using FileZilla FileZilla Server version ([^\\r\\n]+)\\r\\n|s p/FileZilla ftpd/ v/1/ o/Windows/ cpe:/a:filezilla-project:filezilla_server:1/ cpe:/o:microsoft:windows/a";
		assertFalse(filter.includesDollarNum(noDollarnum));
	}

	@Test
	public void testCorrectFilter() {
		String shouldFilter = "match http m|^HTTP/1\\.[01] \\d\\d\\d .*\\r\\nServer: Ethernut ([^\\r\\n]+)\\r\\n| p/Ethernut demo httpd/ v/$1/ o|Nut/OS| cpe:/o:ethernut:nut_os/a";
		assertTrue(filter.canBeFiltered(shouldFilter, handler));
	}

	@Test
	public void testFilterNoBackslashX() {
		String backslashX = "match ssh m|^\\0\\0\\0\\$\\0\\0\\0\\0\\x01\\0\\0\\0\\x1bNo host key is configured!\\n\\r!\\\"v| p/Foundry Networks switch sshd/ i/broken: No host key configured/";
		assertFalse(filter.canBeFiltered(backslashX, handler));
	}

	@Test
	public void testFilterNoLookahead() {
		String lookahead = "match ftp m|^(?:200-.*\\r\\n)?220 ([\\w._-]+) FTP Server \\(Oracle XML DB/\\) ready\\.\\r\\n|s p/Oracle XML DB ftpd/ h/$1/ cpe:/a:oracle:database_server/";
		assertFalse(filter.canBeFiltered(lookahead, handler));
	}

	@Test
	public void testFilterNoSoftmatch() {
		String softMatch = "softmatch ftp m|^220 ([-.\\w]+) [-.\\w ]+ftp.*\\r\\n$|i h/$1/";
		assertFalse(filter.canBeFiltered(softMatch, handler));
	}

	@Test
	public void testFilterCurly() {
		String curly = "match http m|^HTTP/1\\.0 \\d\\d\\d .*\\r\\nSet-Cookie:WhatsUp={[-\\w]+}; path=/\\r\\nContent-Type: text/html\\r\\nServer: Ipswitch ([\\d.]+)\\r\\n| p/Ipswitch WhatsUp httpd/ v/$1/ o/Windows/ cpe:/a:ipswitch:whatsup/ cpe:/o:microsoft:windows/a";
		assertTrue(filter.canBeFiltered(curly, handler));
	}

	@Test
	public void testFilterNoDollarNum() {
		String noDollarNum = "match ftp m|^220-.*\\r\\n220-\\r\\n220 using FileZilla FileZilla Server version ([^\\r\\n]+)\\r\\n|s p/FileZilla ftpd/ v/1/ o/Windows/ cpe:/a:filezilla-project:filezilla_server:1/ cpe:/o:microsoft:windows/a";
		assertFalse(filter.canBeFiltered(noDollarNum, handler));
	}

	@Test
	public void testCanBeFilteredDollarNumButIncludesForbidden() {
		String dollarNumAndForbidden = "match http m|^HTTP/1\\.0 \\d\\d\\d DPR .*\\r\\nSet-Cookie:WhatsUp={[-\\w]+}; path=/\\r\\nContent-Type: text/html\\r\\nServer: Ipswitch ([\\d.]+)\\r\\n| p/Ipswitch WhatsUp httpd/ v/$1/ o/Windows/ cpe:/a:ipswitch:whatsup/ cpe:/o:microsoft:windows/a";
		assertTrue(dollarNumAndForbidden.contains("$1"));
		assertFalse(filter.canBeFiltered(dollarNumAndForbidden, handler));
		
	}
	
	@Test
	public void testFilterNoDollarWord() {
		String dollarWord = "match directconnect m=^\\$MyNick ([-.\\w]+)|\\$Lock= p/Direct Connect P2P/ i/User: $1/ o/Windows/ cpe:/o:microsoft:windows/a";
		assertFalse(filter.canBeFiltered(dollarWord, handler));
	}
}