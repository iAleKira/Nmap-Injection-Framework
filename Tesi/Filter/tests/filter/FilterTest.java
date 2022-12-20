package filter;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class FilterTest {

	private Filter filter;
	private Pattern p;

	@Before
	public void setup() {
		filter = new Filter("input", "output", "<script>(1)</script>");
		p = Pattern.compile("match \\w* m\\|\\W*.*");
	}

	@Test
	public void testContructor() {
		assertEquals("input", filter.getInputPath());
		assertEquals("output", filter.getOutputPath());
		assertEquals("<script>(1)</script>", filter.getPayload());
	}

	@Test
	public void testFilterNullString() {
		try {
			filter.canBeFiltered(p, null);
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot filter a null string.", e.getMessage());
		}
	}

	@Test
	public void testFilterNullPattern() {
		try {
			filter.canBeFiltered(null, "string");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot filter with null pattern.", e.getMessage());
		}
	}

	@Test
	public void testForbiddenNoBackslashPaerenthesis() {
		String backslashParenthesis = "220 ([-/.+\\w]+) FTP server \\(SecureTransport (\\d[-.\\w]+)\\) ready\\.\\r\\n";
		assertTrue(filter.includesFobiddenCharacters(backslashParenthesis));
		String noBackslashParenthesis = "220 ([-/.+\\w]+) FTP server SecureTransport (\\d[-.\\w]+)\\) ready\\.\\r\\n";
		assertFalse(filter.includesFobiddenCharacters(noBackslashParenthesis));
	}

	@Test
	public void testFobiddenDPR() {
		String DPR = "HTTP/1\\.0 401 Authorization Required\\r\\nWWW-Authenticate: BASIC realm=\\\"(DPR?-\\d[^)]+)\\\"\\r\\n\\r\\nPassword Error\\.";
		assertTrue(filter.includesFobiddenCharacters(DPR));
		String NoDPR = "HTTP/1\\.0 401 Authorization Required\\r\\nWWW-Authenticate: BASIC realm=\\\"(?-\\d[^)]+)\\\"\\r\\n\\r\\nPassword Error\\.";
		assertFalse(filter.includesFobiddenCharacters(NoDPR));
	}

	@Test
	public void testFobiddenLookahead() {
		String forbidden = "?:";
		assertTrue(filter.includesFobiddenCharacters(forbidden));
		String Notforbidden = "(200-.*\\r\\n)";
		assertFalse(filter.includesFobiddenCharacters(Notforbidden));
	}
	
	@Test
	public void testFobiddenBackslashX() {
		String forbidden = "\\x";
		assertTrue(filter.includesFobiddenCharacters(forbidden));
	}

	@Test
	public void testFobiddenLookAheadAndBackslashX() {
		String forbidden = "\\x ?:";
		assertTrue(filter.includesFobiddenCharacters(forbidden));
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
		assertTrue(filter.canBeFiltered(p, shouldFilter));
	}

	@Test
	public void testFilterNoBackslashX() {
		String backslashX = "match ssh m|^\\0\\0\\0\\$\\0\\0\\0\\0\\x01\\0\\0\\0\\x1bNo host key is configured!\\n\\r!\\\"v| p/Foundry Networks switch sshd/ i/broken: No host key configured/";
		assertFalse(filter.canBeFiltered(p, backslashX));
	}

	@Test
	public void testFilterNoLookahead() {
		String lookahead = "match ftp m|^(?:200-.*\\r\\n)?220 ([\\w._-]+) FTP Server \\(Oracle XML DB/\\) ready\\.\\r\\n|s p/Oracle XML DB ftpd/ h/$1/ cpe:/a:oracle:database_server/";
		assertFalse(filter.canBeFiltered(p, lookahead));
	}

	@Test
	public void testFilterNoSoftmatch() {
		String softMatch = "softmatch ftp m|^220 ([-.\\w]+) [-.\\w ]+ftp.*\\r\\n$|i h/$1/";
		assertFalse(filter.canBeFiltered(p, softMatch));
	}

	@Test
	public void testFilterCurly() {
		String curly = "match http m|^HTTP/1\\.0 \\d\\d\\d .*\\r\\nSet-Cookie:WhatsUp={[-\\w]+}; path=/\\r\\nContent-Type: text/html\\r\\nServer: Ipswitch ([\\d.]+)\\r\\n| p/Ipswitch WhatsUp httpd/ v/$1/ o/Windows/ cpe:/a:ipswitch:whatsup/ cpe:/o:microsoft:windows/a";
		assertTrue(filter.canBeFiltered(p, curly));
	}

	@Test
	public void testFilterNoDollarNum() {
		String noDollarNum = "match ftp m|^220-.*\\r\\n220-\\r\\n220 using FileZilla FileZilla Server version ([^\\r\\n]+)\\r\\n|s p/FileZilla ftpd/ v/1/ o/Windows/ cpe:/a:filezilla-project:filezilla_server:1/ cpe:/o:microsoft:windows/a";
		assertFalse(filter.canBeFiltered(p, noDollarNum));
	}

	@Test
	public void testFilterNoDollarWord() {
		String dollarWord = "match directconnect m=^\\$MyNick ([-.\\w]+)|\\$Lock= p/Direct Connect P2P/ i/User: $1/ o/Windows/ cpe:/o:microsoft:windows/a";
		assertFalse(filter.canBeFiltered(p, dollarWord));
	}
}