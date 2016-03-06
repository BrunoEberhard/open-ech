package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.model.common.CountryIdentification;

public class StaxEch0072 {

	private final List<CountryIdentification> countryIdentifications = new ArrayList<CountryIdentification>(300);
	private final List<CountryIdentification> countriesUnmodifiable;
	
	public StaxEch0072() {
		this(StaxEch0072.class.getClassLoader().getResourceAsStream("ch/ech/data/eCH0072.xml"));
	}
	
	public StaxEch0072(InputStream inputStream) {
		try {
			process(inputStream);
		} catch (Exception x) {
			throw new RuntimeException("Read of Countries failed", x);
		}
		
		countriesUnmodifiable = Collections.unmodifiableList(countryIdentifications);
	}
	
	public List<CountryIdentification> getCountryIdentifications() {
		return countriesUnmodifiable;
	}

	private void country(XMLEventReader xml) throws XMLStreamException {
		CountryIdentification countryIdentification = new CountryIdentification();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (ID.equals(startName)) countryIdentification.id = integer(xml);
				else if (ISO2_ID.equals(startName)) countryIdentification.countryIdISO2 = token(xml);
				else if (SHORT_NAME_DE.equals(startName)) countryIdentification.countryNameShort = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				countryIdentifications.add(countryIdentification);
				break;
			}  // else skip
		}
	}
	
	private void countries(XMLEventReader xml) throws XMLStreamException {
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
	
	private void process(InputStream inputStream) throws XMLStreamException {
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

}
