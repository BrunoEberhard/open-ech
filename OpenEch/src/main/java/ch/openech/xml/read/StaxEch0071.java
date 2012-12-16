package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.CANTON;
import static ch.openech.dm.XmlConstants.CANTONS;
import static ch.openech.dm.XmlConstants.CANTON_ABBREVIATION;
import static ch.openech.dm.XmlConstants.CANTON_LONG_NAME;
import static ch.openech.dm.XmlConstants.HISTORY_MUNICIPALITY_ID;
import static ch.openech.dm.XmlConstants.MUNICIPALITIES;
import static ch.openech.dm.XmlConstants.MUNICIPALITY;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_ABOLITION_DATE;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_ID;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_LONG_NAME;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_SHORT_NAME;
import static ch.openech.dm.XmlConstants.NOMENCLATURE;
import static ch.openech.xml.read.StaxEch.integer;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

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

import ch.openech.dm.common.Canton;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.util.StringUtils;

public class StaxEch0071 {

	private static StaxEch0071 instance;
	private final List<MunicipalityIdentification> municipalityIdentifications = new ArrayList<MunicipalityIdentification>(10000);
	private final List<Canton> cantons = new ArrayList<Canton>(30);
	private final List<MunicipalityIdentification> municipalitiesUnmodifiable;
	private final List<Canton> cantonsUnmodifiable;
	
	private StaxEch0071() {
		String path = this.getClass().getPackage().getName().replace('.', '/');
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path + "/eCH0071.xml");
		try {
			process(inputStream);
		} catch (Exception x) {
			throw new RuntimeException("Read of cantons and municipalities failed", x);
		}
		Collections.sort(cantons);
		Collections.sort(municipalityIdentifications);
		
		municipalitiesUnmodifiable = Collections.unmodifiableList(municipalityIdentifications);
		cantonsUnmodifiable = Collections.unmodifiableList(cantons);
	}

	public static synchronized StaxEch0071 getInstance() {
		if (instance == null) {
			instance = new StaxEch0071();
		}
		return instance;
	}
	
	public List<Canton> getCantons() {
		return cantonsUnmodifiable;
	}
	
	public List<MunicipalityIdentification> getMunicipalityIdentifications() {
		return municipalitiesUnmodifiable;
	}

	private void municipality(XMLEventReader xml) throws XMLStreamException, SQLException {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		
		boolean abolition = false;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (HISTORY_MUNICIPALITY_ID.equals(startName)) municipalityIdentification.historyMunicipalityId = integer(xml);
				else if (MUNICIPALITY_ID.equals(startName)) municipalityIdentification.municipalityId = integer(xml);
				else if (MUNICIPALITY_LONG_NAME.equals(startName)) municipalityIdentification.municipalityName = token(xml);
				else if (MUNICIPALITY_SHORT_NAME.equals(startName)) {
					String shortName = token(xml);
					if (StringUtils.isBlank(municipalityIdentification.municipalityName)) municipalityIdentification.municipalityName = shortName;
				}
				else if (CANTON_ABBREVIATION.equals(startName)) municipalityIdentification.cantonAbbreviation.canton = token(xml);
				else if (MUNICIPALITY_ABOLITION_DATE.equals(startName)) {
					String token = token(xml);
					if (!StringUtils.isBlank(token)) {
						abolition = true;
					}
				}
				else skip(xml);
			} else if (event.isEndElement()) {
				if (!abolition) {
					municipalityIdentifications.add(municipalityIdentification);
				}
				break;
			}  // else skip
		}
	}

	private void canton(XMLEventReader xml) throws XMLStreamException, SQLException {
		Canton canton = new Canton();
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (CANTON_ABBREVIATION.equals(startName)) canton.cantonAbbreviation.canton = token(xml);
				else if (CANTON_LONG_NAME.equals(startName)) canton.cantonLongName = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				cantons.add(canton);
				break;
			}  // else skip
		}
	}
	
	private void municipalities(XMLEventReader xml) throws XMLStreamException, SQLException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MUNICIPALITY)) {
					municipality(xml);
				}
				else skip(xml);
			}  else if (event.isEndElement()) {
				break;
			}
		}
	}

	private void cantons(XMLEventReader xml) throws XMLStreamException, SQLException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(CANTON)) canton(xml);
				else skip(xml);
			}  else if (event.isEndElement()) {
				break;
			}
		}
	}
	
	private void nomenclature(XMLEventReader xml) throws XMLStreamException, SQLException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MUNICIPALITIES)) municipalities(xml);
				else if (startName.equals(CANTONS)) cantons(xml);
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
				if (startName.equals(NOMENCLATURE)) nomenclature(xml);
				else skip(xml);
			}  else if (event.isEndElement()) {
				break;
			}
		}
	}
	
}
