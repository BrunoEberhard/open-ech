package ch.openech.xml.read;

import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class StaxEchSchema {
	private static Logger logger = Logger.getLogger(StaxEchSchema.class.getName());
	private static final int MAX_DATE_LENGHT = "01-02-1934".length();
	
	public static String date(XMLEventReader xml) throws XMLStreamException {
		String date = token(xml);
		if (date.length() > MAX_DATE_LENGHT) {
			// Einige Schlaumeier hängen ein "+01:00" oder eine Zeitzone an
			date = date.substring(0, MAX_DATE_LENGHT);
		}
		return date;
	}
	
	public static String token(XMLEventReader xml) throws XMLStreamException {
		String token = null;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				token = event.asCharacters().getData().trim();
			} else if (event.isEndElement()) {
				return token;
			} // else skip
		}
	}
	
	public static int integer(XMLEventReader xml) throws XMLStreamException {
		int i = 0;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				String token = event.asCharacters().getData().trim();
				i = Integer.parseInt(token);
			} else if (event.isEndElement()) {
				return i;
			} // else skip
		}
	}
	
	public static int bulean(XMLEventReader xml) throws XMLStreamException {
		int i = -1;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				String token = event.asCharacters().getData().trim();
				i = Boolean.parseBoolean(token) || "1".equals(token) ? 1 : 0;
			} else if (event.isEndElement()) {
				if (i == -1) throw new IllegalArgumentException();
				return i;
			} // else skip
		}
	}
	
	public static void skip(XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				logger.fine("Skipping XML Element: " + event.asStartElement().getName().getLocalPart());
				skip(xml);
			} else if (event.isEndElement()) break;
			// else ignore
		}
	}
	
}
