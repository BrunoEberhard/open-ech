package ch.openech.xml.write;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Some calculations between namespace URIs, Locations and Version. Ech convention specific.
 * Not pretty, but at least there are some JUnit-Tests
 * 
 */
public class EchNamespaceUtil {

	private static final String ECH_NS_BASE = "http://www.ech.ch/xmlns/eCH-";

	private static Map<String, String> LOCAL_COPIES = new HashMap<>();
	
	static {
		LOCAL_COPIES.put("http://www.open-ech.ch/xmlns/open-eCH/1", "/ch/openech/xmlns/Open-eCH-1-0.xsd");
		LOCAL_COPIES.put("http://www.ech.ch/xmlns/eCH-0147/1/eCH-0147T0.xsd", "/ch/ech/xmlns/eCH-0147_V1.2_T0.xsd");
		LOCAL_COPIES.put("http://www.ech.ch/xmlns/eCH-0147/1/eCH-0147T1.xsd", "/ch/ech/xmlns/eCH-0147_V1.2_T1.xsd");
		LOCAL_COPIES.put("http://www.ech.ch/xmlns/eCH-0147/1/eCH-0147T2.xsd", "/ch/ech/xmlns/eCH-0147_V1.2_T2.xsd");
		LOCAL_COPIES.put("eCH-0147T0.xsd", "/ch/ech/xmlns/eCH-0147_V1.2_T0.xsd");
		LOCAL_COPIES.put("eCH-0147T1.xsd", "/ch/ech/xmlns/eCH-0147_V1.2_T1.xsd");
		LOCAL_COPIES.put("eCH-0147T2.xsd", "/ch/ech/xmlns/eCH-0147_V1.2_T2.xsd");
		LOCAL_COPIES.put("http://www.ech.ch/xmlns/eCH-0211/1/eCH-0211-1-0.xsd", "/ch/ech/xmlns/eCH-0211-1-0.xsd");
	}
	
	public static InputStream getLocalCopyOfSchema(String namespaceLocation) {
		String fileName;
		if (!LOCAL_COPIES.containsKey(namespaceLocation)) {
			int pos = namespaceLocation.lastIndexOf("/");
			fileName = "/ch/ech/xmlns" + namespaceLocation.substring(pos);

		} else {
			fileName = LOCAL_COPIES.get(namespaceLocation);
		}
		return EchNamespaceUtil.class.getResourceAsStream(fileName);
	}

	// Aufgrund von Konventionen für URI und Location verwendbar
	public static int extractSchemaNumber(String s) {
		if (s.contains("0147")) {
			return 147;
		}
		if (s.contains("eCH-")) {
			int pos = s.lastIndexOf("eCH-") + 4;
			int end = pos;
			while (end < s.length() && Character.isDigit(s.charAt(end))) end++;
			
			try {
				return Integer.parseInt(s.substring(pos, end));
			} catch (Exception x) {
				System.err.println("Error parsing schema number of " + s);
				return -1;
			}
		} else {
			return -1;
		}
	}
	
	public static int extractSchemaMajorVersion(String s) {
		if (s.contains("0147")) {
			return -1;
		}
		if (s.contains("eCH-")) {
			int pos = s.lastIndexOf("eCH-") + 4;
			// skip schemanumber
			while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
			// skip -
			pos++;
			// skip 'commons-' in 'eCH-0084-commons-1-4'
			if (!Character.isDigit(s.charAt(pos))) {
				// skip commons
				while (pos < s.length() && s.charAt(pos) != '-' && s.charAt(pos) != '/') pos++;
				// skip - or /
				pos++;
			}
						
			int end = pos;
			while (end < s.length() && Character.isDigit(s.charAt(end))) end++;
			
			try {
				return Integer.parseInt(s.substring(pos, end));
			} catch (Exception x) {
				System.err.println("Error parsing major version of " + s);
				return -1;
			}
		} else {
			return -1;
		}
	}

	// Da nur die Location die Minor Version enthält für URI nicht anwendbar
	public static int extractSchemaMinorVersion(String s) {
		if (s.contains("0147")) {
			return -1;
		}
		if (s.contains("eCH-")) {
			int pos = s.lastIndexOf("eCH-") + 4;
			// skip schemanumber
			while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
			// skip -
			pos++;
			// skip 'commons-' in 'eCH-0084-commons-1-4'
			if (!Character.isDigit(s.charAt(pos))) {
				// skip commons
				while (pos < s.length() && s.charAt(pos) != '-') pos++;
				// skip -
				pos++;
			}
			// skip version
			while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
			// skip -
			pos++;
			
			int end = pos;
			while (end < s.length() && Character.isDigit(s.charAt(end))) end++;
			
			try {
				return Integer.parseInt(s.substring(pos, end));
			} catch (Exception x) {
				System.err.println("Error parsing minor version of " + s);
				return -1;
			}
		} else {
			return -1;
		}
	}

	// http://www.ech.ch/xmlns/eCH-0044/2 -> http://www.ech.ch/xmlns/eCH-0044/2/eCH-0044-2-0.xsd
	public static String schemaLocation(String namespaceURI, String minor) {
		int major = extractSchemaMajorVersion(namespaceURI);
		String fileNamePart = namespaceURI.substring(ECH_NS_BASE.length() - 4, ECH_NS_BASE.length() + 4);
		return namespaceURI + "/" + fileNamePart + "-" + major + "-" + minor + ".xsd";
	}
	
	// 112, 1, 0 -> http://www.ech.ch/xmlns/eCH-0112/1/eCH-0112-1-0.xsd
	public static String schemaLocation(int number, String major, String minor) {
		return schemaLocation(schemaURI(number, major), minor);
	}

	// 112, 1.0 -> http://www.ech.ch/xmlns/eCH-0112/1/eCH-0112-1-0.xsd
	public static String schemaLocation(int number, String version) {
		int pos = version.indexOf(".");
		String major = version.substring(0, pos);
		String minor = version.substring(pos+1);
		return schemaLocation(number, major, minor);
	}

	// 20, 2 -> http://www.ech.ch/xmlns/eCH-0020/2
	public static String schemaURI(int number, String major) {
		String numberAsString = "0000" + Integer.toString(number);
		String fourDigitSttring = numberAsString.substring(numberAsString.length() - 4);
		String uri = ECH_NS_BASE + fourDigitSttring + "/" + major;
		if (number == 215 || number == 213) {
			uri += ".0";
		}
		return uri;
	}
	
	// http://www.ech.ch/xmlns/eCH-0044/2/eCH-0044-2-0.xsd - > http://www.ech.ch/xmlns/eCH-0044/2
	public static String schemaURI(String namespaceLocation) {
		int versionStart = ECH_NS_BASE.length() + 5;
		int versionEnd = namespaceLocation.indexOf('/', versionStart);
		return  namespaceLocation.substring(0, versionEnd);
	}
	
}
