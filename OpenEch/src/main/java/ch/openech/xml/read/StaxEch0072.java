package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.COUNTRIES;
import static ch.openech.dm.XmlConstants.COUNTRY;
import static ch.openech.dm.XmlConstants.ID;
import static ch.openech.dm.XmlConstants.ISO2_ID;
import static ch.openech.dm.XmlConstants.SHORT_NAME_DE;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Vector;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.CountryIdentification;

public class StaxEch0072 {

	private static StaxEch0072 instance;
	private final Vector<CountryIdentification> countryIdentifications = new Vector<CountryIdentification>(300);
	private final Vector<String> countryIdISO2s = new Vector<String>(300);

	private StaxEch0072() {
		String path = this.getClass().getPackage().getName().replace('.', '/');
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path + "/eCH0072.xml");
		try {
			process(inputStream);
		} catch (Exception x) {
			throw new RuntimeException("Read of Countries failed", x);
		}
	}
	
	public static synchronized StaxEch0072 getInstance() {
		if (instance == null) {
			instance = new StaxEch0072();
		}
		return instance;
	}
	
	/**
	 * Diese Methode gibt einen Vector zurück, weil die JCombobox einen Constructor dazu
	 * anbietet. Vector implementiert auch List, das sollte also nirgends ein Problem sein.
	 * 
	 * @return Einen Vector der Länder. 
	 */
	public Vector<CountryIdentification> getCountryIdentifications() {
		return countryIdentifications;
	}

	public Vector<String> getCountryIdISO2s() {
		return countryIdISO2s;
	}

	private void country(XMLEventReader xml) throws XMLStreamException, SQLException {
		CountryIdentification countryIdentification = new CountryIdentification();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (ID.equals(startName)) countryIdentification.countryId = token(xml);
				else if (ISO2_ID.equals(startName)) countryIdentification.countryIdISO2 = token(xml);
				else if (SHORT_NAME_DE.equals(startName)) countryIdentification.countryNameShort = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				countryIdentifications.add(countryIdentification);
				countryIdISO2s.add(countryIdentification.countryIdISO2);
				break;
			}  // else skip
		}
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