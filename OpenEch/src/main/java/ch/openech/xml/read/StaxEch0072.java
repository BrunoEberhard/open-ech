package ch.openech.xml.read;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import static ch.openech.dm.XmlConstants.*;
import ch.openech.dm.common.CountryIdentification;

import static ch.openech.xml.read.StaxEch.*;


public class StaxEch0072 {

	private static StaxEch0072 instance;
	private final List<CountryIdentification> countryIdentifications = new ArrayList<CountryIdentification>(300);
	private final List<String> countryNames = new ArrayList<String>(300);
	private final List<String> countryIdISO2s = new ArrayList<String>(300);

	private final List<CountryIdentification> countriesUnmodifiable;
	private final List<String> countryNamesUnmodifiable;
	private final List<String> countryIdISO2sUnmodifiable;
	
	private StaxEch0072() {
		InputStream inputStream = this.getClass().getResourceAsStream("eCH0072.xml");
		try {
			process(inputStream);
		} catch (Exception x) {
			throw new RuntimeException("Read of Countries failed", x);
		}
		
		countriesUnmodifiable = Collections.unmodifiableList(countryIdentifications);
		countryNamesUnmodifiable = Collections.unmodifiableList(countryNames);
		countryIdISO2sUnmodifiable = Collections.unmodifiableList(countryIdISO2s);
	}
	
	public static synchronized StaxEch0072 getInstance() {
		if (instance == null) {
			instance = new StaxEch0072();
		}
		return instance;
	}
	
	public List<CountryIdentification> getCountryIdentifications() {
		return countriesUnmodifiable;
	}

	public List<String> getCountryNames() {
		return countryNamesUnmodifiable;
	}
	
	public List<String> getCountryIdISO2s() {
		return countryIdISO2sUnmodifiable;
	}

	private void country(XmlPullParser xmlParser) throws SQLException, XmlPullParserException, IOException {
		CountryIdentification countryIdentification = new CountryIdentification();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String tagName = xmlParser.getName();
				if (ID.equals(tagName)) {
					countryIdentification.countryId = integer(xmlParser);
				} else if (ISO2_ID.equals(tagName)) {
					countryIdentification.countryIdISO2 = token(xmlParser);
				} else if (SHORT_NAME_DE.equals(tagName)) {
					countryIdentification.countryNameShort = token(xmlParser);
				}
				
				
			} else if (isEndTag(event) && COUNTRY.equals(xmlParser.getName())) {
				countryIdentifications.add(countryIdentification);
				countryNames.add(countryIdentification.countryNameShort);
				if (countryIdentification.countryIdISO2 != null) {
					countryIdISO2s.add(countryIdentification.countryIdISO2);
				}
				Collections.sort(countryIdISO2s);
				break;
			}
		}
	}
	
	private void countries(XmlPullParser xmlParser) throws  SQLException, XmlPullParserException, IOException {
		
		int event = xmlParser.next();
		while (!COUNTRIES.equals(xmlParser.getName())) {
		    if (isStartTag(event)) {
		    	if (COUNTRY.equals(xmlParser.getName())) {
		    		country(xmlParser);
		    	}
		    }
			event = xmlParser.next();
		}
	}
	
	private void process(InputStream inputStream) throws SQLException, XmlPullParserException, IOException {
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		xmlParser.setInput(new InputStreamReader(inputStream));
		
		do {
			int event = xmlParser.next();
		    if (isStartTag(event)) {
		    	if (COUNTRIES.equals(xmlParser.getName())) {
		    		countries(xmlParser);
		    	}
		    }
		} while (!isEndDocument(xmlParser.getEventType()));
	}
	
	
	public static void main(String... args){
		System.out.println(getInstance().countryIdentifications.size());
	}
}
