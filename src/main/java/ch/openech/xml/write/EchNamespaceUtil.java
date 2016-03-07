package ch.openech.xml.write;

import java.io.InputStream;

/**
 * Some calculations between namespace URIs, Locations and Version. Ech convention specific.
 * Not pretty, but at least there are some JUnit-Tests
 * 
 */
public class EchNamespaceUtil {

	private static final String ECH_NS_BASE = "http://www.ech.ch/xmlns/eCH-";

	public static InputStream getLocalCopyOfSchema(String namespaceLocation) {
		String fileName;
		if (!WriterOpenEch.URI.equals(namespaceLocation)) {
			int pos = namespaceLocation.lastIndexOf("/");
			fileName = "/ch/ech/xmlns" + namespaceLocation.substring(pos);
		} else {
			fileName = "/ch/openech/xmlns/Open-eCH-1-0.xsd";
		}
		return EchNamespaceUtil.class.getResourceAsStream(fileName);
	}

	// Aufgrund von Konventionen für URI und Location verwendbar
	public static int extractSchemaNumber(String namespaceLocationOrURI) {
		if (namespaceLocationOrURI.startsWith(ECH_NS_BASE) && namespaceLocationOrURI.length() >= ECH_NS_BASE.length() + 4) {
			return  Integer.parseInt(namespaceLocationOrURI.substring(ECH_NS_BASE.length(), ECH_NS_BASE.length() + 4));
		} else {
			return -1;
		}
	}
	
	// http://www.ech.ch/xmlns/eCH-0044/2/eCH-0044-2-0.xsd -> 2.0
	public static String extractSchemaVersion(String namespaceLocation) {
		if (namespaceLocation.startsWith(ECH_NS_BASE) && namespaceLocation.length() >= ECH_NS_BASE.length() + 6) {
			int versionStart = ECH_NS_BASE.length() + 5;
			int versionEnd = namespaceLocation.indexOf('/', versionStart);
			
			// now skip 3 '-'
			int minorVersionStart = namespaceLocation.indexOf('-', versionEnd);
			minorVersionStart = namespaceLocation.indexOf('-', minorVersionStart + 1);
			minorVersionStart = namespaceLocation.indexOf('-', minorVersionStart + 1) + 1;

			int minorVersionEnd = namespaceLocation.indexOf(".xsd", minorVersionStart);
			return  namespaceLocation.substring(versionStart, versionEnd) + "." + namespaceLocation.substring(minorVersionStart, minorVersionEnd);
		} else {
			return null;
		}
	}

	// Aufgrund von Konventionen für URI und Location verwendbar
	public static int extractSchemaMajorVersion(String namespaceLocationOrURI) {
		if (namespaceLocationOrURI.startsWith(ECH_NS_BASE) && namespaceLocationOrURI.length() >= ECH_NS_BASE.length() + 6) {
			int versionStart = ECH_NS_BASE.length() + 5;
			int versionEnd = namespaceLocationOrURI.indexOf('/', versionStart);
			if (versionEnd < 0) versionEnd = namespaceLocationOrURI.length();
			return  Integer.parseInt(namespaceLocationOrURI.substring(versionStart, versionEnd));
		} else {
			return -1;
		}
	}

	// Da nur die Location die Minor Version enthält für URI nicht anwendbar
	public static int extractSchemaMinorVersion(String namespaceLocation) {
		if (namespaceLocation.startsWith(ECH_NS_BASE) && namespaceLocation.length() >= ECH_NS_BASE.length() + 6) {
			int versionStart = ECH_NS_BASE.length() + 5;
			int versionEnd = namespaceLocation.indexOf('/', versionStart);
			
			// now skip 3 '-'
			int minorVersionStart = namespaceLocation.indexOf('-', versionEnd);
			minorVersionStart = namespaceLocation.indexOf('-', minorVersionStart + 1);
			minorVersionStart = namespaceLocation.indexOf('-', minorVersionStart + 1) + 1;
			
			int minorVersionEnd = namespaceLocation.indexOf(".xsd", minorVersionStart);
			return  Integer.parseInt(namespaceLocation.substring(minorVersionStart, minorVersionEnd));
		} else {
			return -1;
		}
	}

	// http://www.ech.ch/xmlns/eCH-0044/2 -> http://www.ech.ch/xmlns/eCH-0044/2/eCH-0044-2-0.xsd
	public static String schemaLocation(String namespaceURI, String minor) {
		int major = extractSchemaMajorVersion(namespaceURI);
		String fileNamePart = namespaceURI.substring(ECH_NS_BASE.length() - 4, ECH_NS_BASE.length() + 4);
		if (major != 196) {
			return namespaceURI + "/" + fileNamePart + "-" + major + "-" + minor + ".xsd";
		} else {
			// bei 196 scheint das Verzeichnis auf dem eCH server einen anderen Namen bekommen zu haben
			return namespaceURI + "." + minor + "/" + fileNamePart + "-" + major + "-" + minor + ".xsd";
		}
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
		return ECH_NS_BASE + fourDigitSttring + "/" + major;
	}
	
	// http://www.ech.ch/xmlns/eCH-0044/2/eCH-0044-2-0.xsd - > http://www.ech.ch/xmlns/eCH-0044/2
	public static String schemaURI(String namespaceLocation) {
		int versionStart = ECH_NS_BASE.length() + 5;
		int versionEnd = namespaceLocation.indexOf('/', versionStart);
		return  namespaceLocation.substring(0, versionEnd);
	}
	
}
