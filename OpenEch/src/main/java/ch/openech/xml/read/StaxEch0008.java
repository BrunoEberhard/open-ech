package ch.openech.xml.read;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.common.CountryIdentification;

public class StaxEch0008 {

	/*
	 * Die Landinformation besteht nur aus drei einfachen Werten,
	 * die über das generische simpleValue eingelesen werden können. 
	 */
	public static void country(XmlPullParser xmlParser, CountryIdentification countryIdentification) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (StaxEch.isStartTag(event)) {
				String startName = xmlParser.getName();
				StaxEch.simpleValue(xmlParser, countryIdentification, startName);
			} else if (StaxEch.isEndTag(event)) return;
			// else skip
		}
	}
	
}
