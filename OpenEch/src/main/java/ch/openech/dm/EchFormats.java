package ch.openech.dm;

import java.util.Map.Entry;

import ch.openech.dm.code.EchCodes;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.db.model.Format;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.db.model.PlainFormat;
import ch.openech.xml.write.EchNamespaceContext;


public class EchFormats {
	
	// public static final String date = "http://www.w3.org/2001/XMLSchema:date";
	// public static final String datePartiallyKnown = "http://www.ech.ch/xmlns/eCH-0044/2:datePartiallyKnown";

	public static final String baseName = "baseName";
	public static final String addressLine = "addressLine";
	public static final String yesNo = "yesNo";
	
	public static final String residencePermit = "residencePermit";
	public static final String basedOnLaw = "basedOnLaw";

	// eCH 11
	public static final String originName = "originName";
	
	// eCH 20/21
	public static final String jobTitle = "jobTitle";
	public static final String employer = "employer";

	public static final String typeOfContact = "typeOfContact";

	public static final String cantonAbbreviation = "cantonAbbreviation";
	public static final Format cantonAbbreviationFormat = new PlainFormat(2);
	
	// ech 97
	public static final String organisationName = "organisationName";
	public static final String uidStructure = "uidStructure";

	
	static {
		Formats formats = Formats.getInstance();
		
		registerXmlSimpleTypes(20, "2.2");
		registerXmlSimpleTypes(148, "1.0");
		
		// Von den Schemas nicht abgedeckte Formate
		
		formats.register(jobTitle, new PlainFormat(100));
		formats.register(employer, new PlainFormat(100));
		formats.register(originName, new PlainFormat(50));
		
		// codes

		for (Entry<String, Code> type : EchCodes.getCodes().entrySet()) {
			formats.register(type.getKey(), type.getValue());
		}

		formats.register(typeOfContact, new PlainFormat(1));
		formats.register("category", new PlainFormat(1));
		
		formats.register(cantonAbbreviation, cantonAbbreviationFormat);
		
		formats.register(uidStructure, new PlainFormat(12));
	}
	
	static void registerXmlSimpleTypes(int root, String version) {
		EchNamespaceContext echNamespaceContext = EchNamespaceContext.getNamespaceContext(root, version);
		for (Entry<String, Format> type : echNamespaceContext.getSimpleTypes().entrySet()) {
			Formats.getInstance().register(type.getKey(), type.getValue());
		}
	}

	public static void initialize() {
		// nothing to do, everything done in static initializer
	}
}