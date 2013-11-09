package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.CountryIdentification;

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

	private void country(XMLEventReader xml) throws XMLStreamException, SQLException {
		CountryIdentification countryIdentification = new CountryIdentification();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (ID.equals(startName)) countryIdentification.countryId = integer(xml);
				else if (ISO2_ID.equals(startName)) countryIdentification.countryIdISO2 = token(xml);
				else if (SHORT_NAME_DE.equals(startName)) countryIdentification.countryNameShort = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				countryIdentifications.add(countryIdentification);
				countryNames.add(countryIdentification.countryNameShort);
				if (countryIdentification.countryIdISO2 != null) {
					countryIdISO2s.add(countryIdentification.countryIdISO2);
				}
				break;
			}  // else skip
		}
		Collections.sort(countryIdISO2s);
	}
	
	private void countries(XMLEventReader xml) throws XMLStreamException, SQLException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(COUNTRY)) country(xml);
				else skip(xml);
			}  else if (event.isEndElement()) {
				break;
			}
		}
	}
	
	private void process(InputStream inputStream) throws XMLStreamException, SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);

		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(COUNTRIES))countries(xml);
				else skip(xml);
			}  else if (event.isEndElement()) {
				break;
			}
		}
	}
	
	
	public static void main(String... args){
		System.out.println(getInstance().countryIdentifications.size());
	}
}
