package ch.openech.xml.read;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.common.MunicipalityIdentification;
import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

public class StaxEch0007 {

	public static MunicipalityIdentification municipality(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		municipality(xmlParser, municipalityIdentification);
		return municipalityIdentification;
	}
		
	public static void municipality(XmlPullParser xmlParser, MunicipalityIdentification municipalityIdentification) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(MUNICIPALITY_ID)) municipalityIdentification.municipalityId = integer(xmlParser);
				else if (startName.equals(MUNICIPALITY_NAME)) municipalityIdentification.municipalityName = token(xmlParser);
				else if (startName.equals(CANTON_ABBREVIATION)) municipalityIdentification.cantonAbbreviation.canton = token(xmlParser);
				else if (startName.equals(HISTORY_MUNICIPIALITY_ID) || startName.equals(HISTORY_MUNICIPALITY_ID)) municipalityIdentification.historyMunicipalityId = integer(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
}
