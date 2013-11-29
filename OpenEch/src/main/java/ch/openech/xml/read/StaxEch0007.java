package ch.openech.xml.read;

import ch.openech.dm.common.MunicipalityIdentification;

public class StaxEch0007 {

	public static MunicipalityIdentification municipality()  {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		//municipality(xml, municipalityIdentification);
		return municipalityIdentification;
	}
		
	public static void municipality(MunicipalityIdentification municipalityIdentification) {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(MUNICIPALITY_ID)) municipalityIdentification.municipalityId = StaxEch.integer(xml);
//				else if (startName.equals(MUNICIPALITY_NAME)) municipalityIdentification.municipalityName = token(xml);
//				else if (startName.equals(CANTON_ABBREVIATION)) municipalityIdentification.cantonAbbreviation.canton = token(xml);
//				else if (startName.equals(HISTORY_MUNICIPIALITY_ID) || startName.equals(HISTORY_MUNICIPALITY_ID)) municipalityIdentification.historyMunicipalityId = StaxEch.integer(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
}
