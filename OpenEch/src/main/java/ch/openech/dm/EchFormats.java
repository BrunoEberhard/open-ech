package ch.openech.dm;

public class EchFormats {
	
	public static final int addressLine = 60;
	public static final int baseName = 100;
	public static final int businessReferenceId = 50;
	public static final int cantonAbbreviation = 2;
	public static final int cantonFlAbbreviation = 2;
	public static final int comment = 250;
	public static final int country = 2;
	public static final int countryIdISO2 = 2;
	public static final int countryNameShort = 50;
	public static final int decisionAuthority = 100;
	public static final int declarationLocalReference = 100;
	public static final int dwellingNumber = 10;
	public static final int emailAddress = 100;
	public static final int firstName = 30;
	public static final int foreignZipCode = 15;
	public static final int freeKategoryText = 100;
	public static final int houseNumber = 12;
	public static final int householdNumber = 9;
	public static final int internetAddress = 100;
	public static final int language = 2;
	public static final int lastName = 30;
	public static final int legalForm = 4;
	public static final int locality = 40;
	public static final int messageId = 36;
	public static final int municipalityName = 40;
	public static final int nogaCode = 6;
	public static final int officialFirstName = 100;
//	public static final int officialFirstName = 50;
	public static final int organisationIdCategory = 20;
	public static final int organisationName = 255;
//	public static final int organisationName = 60;
	public static final int participantId = 50;
	public static final int pathFileName = 250;
	public static final int personIdCategory = 20;
	public static final int phoneNumber = 20;
	public static final int postOfficeBoxText = 15;
	public static final int street = 60;
	public static final int subMessageType = 36;
	public static final int subject = 100;
	public static final int swissZipCodeAddOn = 2;
	public static final int testData = 250;
	public static final int title = 20;
	public static final int town = 40;
	public static final int uidBrancheText = 500;
	public static final int uidOrganisationIdCategorie = 3;
	public static final int uniqueIDBusinessCase = 50;
	public static final int vn = 13;

			
	// public static final String date = "http://www.w3.org/2001/XMLSchema:date";
	// public static final String datePartiallyKnown = "http://www.ech.ch/xmlns/eCH-0044/2:datePartiallyKnown";

//	public static final int baseName = 30;
//	
//	public static final int addressLine = 60;
//	
//	// eCH 11
//	public static final int originName = 50;
//	
//	// eCH 20/21
//	public static final String jobTitle = XmlConstants.JOB_TITLE;
//	public static final String employer = XmlConstants.EMPLOYER;
//
//	public static final String typeOfContact = "typeOfContact";
//
//	public static final int cantonAbbreviation = 2;
//	
//	// ech 97
//	public static final int organisationName = 255;
//	public static final String uidStructure = "uidStructure";

	
//	static {
//		Formats formats = Formats.getInstance();
//		
//		registerXmlSimpleTypes(20, "2.2");
//		registerXmlSimpleTypes(148, "1.0");
//		
//		// Von den Schemas nicht abgedeckte Formate
//		
//		Formats.register(jobTitle, new Size.SizeImpl(100));
//		Formats.register(employer, new Size.SizeImpl(100));
//		Formats.register(originName, new Size.SizeImpl(50));
//		
//		// codes
//
//		Formats.register(typeOfContact, new Size.SizeImpl(1));
//		Formats.register("category", new Size.SizeImpl(1));
//		
//		Formats.register(cantonAbbreviation, new Size.SizeImpl(2));
//		
//		Formats.register(uidStructure, new Size.SizeImpl(2));
//	}
//	
//	static void registerXmlSimpleTypes(int root, String version) {
//		EchSchema echNamespaceContext = EchSchema.getNamespaceContext(root, version);
//		for (Entry<String, Annotation> type : echNamespaceContext.getSimpleTypes().entrySet()) {
//			Formats.register(type.getKey(), type.getValue());
//			System.out.println(type.getKey());
//		}
//	}

}
