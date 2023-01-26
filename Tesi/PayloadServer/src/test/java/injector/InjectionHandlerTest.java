package injector;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InjectionHandlerTest {

	private String ssh;
	private String payload;
	private InjectionHandler builder;
	
	@Before
	public void setup() {
		ssh = "match ssh m|^SSH-([\\d.]+)-OpenSSH_([\\w._-]+)[ -]{1,2}Debian[ -_]([^\\r\\n]+)\\r?\\n| p/OpenSSH/ v/$2 Debian $3/ i/protocol $1/ o/Linux/ cpe:/a:openbsd:openssh:$2/ cpe:/o:debian:debian_linux/ cpe:/o:linux:linux_kernel/a\n";
		payload = "\"’/><img src=’x’ onerror=’alert(1)’";
		builder = new InjectionHandler();
	}
	
	@Test
	public void testRestrict(){
		String restricted = builder.restrict(ssh);
		assertTrue(ssh.contains(restricted));
		assertEquals("SSH-([\\d.]+)-OpenSSH_([\\w._-]+)[ -]{1,2}Debian[ -_]([^\\r\\n]+)\\r?\\n", restricted);
	}
		
	@Test
	public void testExtractInjectionGroupMatchFound(){
		String restricted = "SSH-([\\d.]+)-OpenSSH_([\\w._-]+)[ -]{1,2}Debian[ -_]([^\\r\\n]+)\\r?\\n";
		String captureGroup = builder.extractInjectionGroup(restricted, payload);
		assertEquals("([^\\r\\n]+)", captureGroup);
		assertTrue(payload.matches(captureGroup));
	}
	
	@Test
	public void testExtractInjectionGroupNoMatchFound(){
		String restricted = "SSH-([\\d.]+)-OpenSSH_([\\w._-]+)[ -]{1,2}Debian[ -_]\\r?\\n";
		String captureGroup = builder.extractInjectionGroup(restricted, payload);
		assertEquals("", captureGroup);
		assertTrue(captureGroup.isEmpty());
		assertTrue(!payload.matches(captureGroup));
	}
	
	@Test
	public void testExtractInjectionGroupNestedCapture(){
		String restricted = "SSH-([\\d.]+([\\w._-]+))-OpenSSH_[ -]{1,2}Debian[ -_]\\r?\\n";
		String captureGroup = builder.extractInjectionGroup(restricted, payload);
		assertEquals("", captureGroup);
		assertTrue(captureGroup.isEmpty());
		assertTrue(!payload.matches(captureGroup));
	}
}