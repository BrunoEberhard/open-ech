package ch.openech.xml.read;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.common.Address;
import static ch.openech.dm.XmlConstants.*;

public class StaxEch0010 {

	public static Address address(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Address address = new Address();
		address(address, xmlParser);
		return address;
	}
	
	public static void address(Address address, XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (StaxEch.isStartTag(event)) {
				String startName = xmlParser.getName();
				// Die in der Java - Klasse alle Attribute flach drin sind,
				// kann hier einfach "rekursiv" die Methode wieder aufgerufen werden
				if (startName.equals(ORGANISATION)) address(address, xmlParser);
				else if (startName.equals(PERSON)) address(address, xmlParser);
				else if (startName.equals(ADDRESS_INFORMATION)) address(address, xmlParser);
				else if (startName.equals(SWISS_ZIP_CODE)) address.zip = StaxEch.token(xmlParser);
				else if (startName.equals(FOREIGN_ZIP_CODE)) address.zip = StaxEch.token(xmlParser);
				else if (startName.equals(SWISS_ZIP_CODE_ID)) address.swissZipCodeId = StaxEch.integer(xmlParser);
				else if (startName.equals(SWISS_ZIP_CODE_ADD_ON)) address.zip += " " + StaxEch.token(xmlParser);
				else StaxEch.simpleValue(xmlParser, address, startName);
			} else if (StaxEch.isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
}
