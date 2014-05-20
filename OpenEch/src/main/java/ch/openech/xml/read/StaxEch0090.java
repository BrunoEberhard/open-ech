package ch.openech.xml.read;

import static  ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.properties.FlatProperties;

import  ch.openech.model.Envelope;

public class StaxEch0090 {

	public static Envelope process(File file) throws XMLStreamException, FileNotFoundException {
		return process(new FileInputStream(file));
	}
	
	public static Envelope process(InputStream inputStream) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		
		return process(xml);
	}

	private static Envelope process(XMLEventReader xml) throws XMLStreamException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ENVELOPE)) {
					return envelope(xml);
				}
				else skip(xml);
			} 
		}
		return null;
	}
	
	private static Envelope envelope(XMLEventReader xml) throws XMLStreamException {
		Envelope envelope = new Envelope();
		
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.endsWith("Date")) {
					FlatProperties.set(envelope, startName, dateTime(xml));
				} else {
					FlatProperties.set(envelope, startName, token(xml));
				}
			} 
		}
		return envelope;
	}

}
