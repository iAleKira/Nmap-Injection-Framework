package injector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements the injection utilities. Here are handful methods to
 * build strings that will be returned by the server. If the string has a
 * capture group that matches the payload, then it can be injected with the
 * payload. If it doesn't match then it is left as it is to be later parsed by a
 * parser.
 *
 */
public class InjectionHandler {

	/**
	 * This method determines the capture group that can be injected with the
	 * payload.
	 * 
	 * @param injectable - string to inject.
	 * @param payload    - payload to inject.
	 * @return captureGroup - capture group in injectable that can be injected by
	 *         payload.
	 * 
	 */

	public String extractInjectionGroup(String injectable, String payload) {
		String backup = injectable;
		String captureGroup;
		String captureGroup2;

		do {
			captureGroup = backup.substring(backup.indexOf("("), backup.indexOf(")") + 1); // extract first captureGroup
			long numOfCaptureGroups = captureGroup.chars().filter(ch -> ch == '(').count(); // count for nested groups
			if (numOfCaptureGroups > 1) { // if there's more than one in the same group
				int n = backup.lastIndexOf(captureGroup); // calculate where captureGroup starts
				captureGroup2 = backup.substring(n + captureGroup.length(), // capture the other one
						(backup.indexOf((")"), n + captureGroup.length()) + 1));
				captureGroup = captureGroup + captureGroup2; // make one capture group
				backup = backup.replace(captureGroup, ""); // delete it from backup
				captureGroup = ""; // update deletion
			}
			if (payload.matches(captureGroup)) { // if we do have a match with the payload
				return captureGroup;
			} else {
				backup = backup.replace(captureGroup, ("")); // otherwise just delete it
			}

		} while (backup.contains("("));
		return "";
	}

	/**
	 * This method determines the first capture group encountered in a string.
	 * If capture group is not found then empty string is returned.
	 * 
	 * @param captureSource - source string to extract capture group from.
	 * @return captureGroup - first capture group found.
	 * 
	 */

	public String extractCaptureGroup(String captureSource) {
		String captureGroup;
		String captureGroup2;
		if (!(captureSource.contains("(") && captureSource.contains(")"))) {
			return "";
		} else {
			captureGroup = captureSource.substring(captureSource.indexOf("("), captureSource.indexOf(")") + 1);
			long numOfCaptureGroups = captureGroup.chars().filter(ch -> ch == '(').count(); // count for nested groups
			if (numOfCaptureGroups > 1) { // if there's more than one in the same group
				int n = captureSource.lastIndexOf(captureGroup); // calculate where captureGroup starts
				captureGroup2 = captureSource.substring(n + captureGroup.length(), // capture the other one
						(captureSource.indexOf((")"), n + captureGroup.length()) + 1));
				captureGroup = captureGroup + captureGroup2; // make one capture group
			}
		}
		return captureGroup;
	}

	/**
	 * This method compiles a pattern so that it can get the regular expression in
	 * between its starting symbols "m|^" and ending symbol "|" of the input string.
	 * It then substring it to delete the string delimiters and the starting symbol
	 * mentioned earlier, and it returns the result, which is the shortened or
	 * restricted string.
	 * 
	 * @param toRestict - string to restrict.
	 * 
	 * @return restrictedString - string in between |^ |.
	 * 
	 */
	public String restrict(String toRestict) {
		Pattern p = Pattern.compile("m\\|[\\W](.*?)\\|"); // restrict the string to m|^...|
		Matcher m = p.matcher(toRestict);
		m.find();
		String fullString = m.group();
		String restrictedString = fullString.substring(3, fullString.length() - 1);
		return restrictedString;
	}
}