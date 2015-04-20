package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.util.LoggingRuntimeException;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.Canton;
import ch.openech.model.common.MunicipalityIdentification;

public class StaxEch0071 {

	private static StaxEch0071 instance;
	private final List<MunicipalityIdentification> municipalities = new ArrayList<MunicipalityIdentification>(10000);
	private final List<Canton> cantons = new ArrayList<Canton>(30);
	
	private StaxEch0071() {
		InputStream inputStream = this.getClass().getResourceAsStream("eCH0071.xml");
		try {
			process(inputStream);
		} catch (XMLStreamException x) {
			throw new LoggingRuntimeException(x, Logger.getLogger(getClass().getName()), "Read of cantons and municipalities failed");
		}
		Collections.sort(cantons);
		Collections.sort(municipalities);
	}

	public static synchronized StaxEch0071 getInstance() {
		if (instance == null) {
			instance = new StaxEch0071();
		}
		return instance;
	}
	
	public List<Canton> getCantons() {
		return cantons;
	}
	
	public List<MunicipalityIdentification> getMunicipalityIdentifications() {
		return municipalities;
	}

	private void municipality(XMLEventReader xml) throws XMLStreamException {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		
		boolean abolition = false;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (HISTORY_MUNICIPALITY_ID.equals(startName)) municipalityIdentification.historyMunicipalityId = integer(xml);
				else if (MUNICIPALITY_ID.equals(startName)) municipalityIdentification.id = integer(xml);
				else if (MUNICIPALITY_LONG_NAME.equals(startName)) municipalityIdentification.municipalityName = token(xml);
				else if (MUNICIPALITY_SHORT_NAME.equals(startName)) {
					String shortName = token(xml);
					if (StringUtils.isBlank(municipalityIdentification.municipalityName)) municipalityIdentification.municipalityName = shortName;
				}
				else if (CANTON_ABBREVIATION.equals(startName)) {
					municipalityIdentification.canton = new Canton(token(xml));
				}
				else if (MUNICIPALITY_ABOLITION_DATE.equals(startName)) {
					String token = token(xml);
					if (!StringUtils.isBlank(token)) {
						abolition = true;
					}
				}
				else skip(xml);
			} else if (event.isEndElement()) {
				if (!abolition) {
					municipalities.add(municipalityIdentification);
				}
				break;
			}  // else skip
		}
	}

	private void canton(XMLEventReader xml) throws XMLStreamException {
		Canton canton = new Canton();
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (CANTON_ABBREVIATION.equals(startName)) canton.id = token(xml);
				else if (CANTON_LONG_NAME.equals(startName)) canton.cantonLongName = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				cantons.add(canton);
				break;
			}  // else skip
		}
	}
	
	private void municipalities(XMLEventReader xml) throws XMLStreamException {
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

	private void cantons(XMLEventReader xml) throws XMLStreamException {
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
	
	private void nomenclature(XMLEventReader xml) throws XMLStreamException {
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
	
	private void process(InputStream inputStream) throws XMLStreamException {
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