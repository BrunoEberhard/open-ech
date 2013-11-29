package ch.openech.xml.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.openech.dm.Envelope;

public class StaxEch0090 {

	public static Envelope process(File file) throws FileNotFoundException {
		return process(new FileInputStream(file));
	}
	
	public static Envelope process(InputStream inputStream)  {
		//XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		//XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		
		return process();
	}

	private static Envelope process() {
//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ENVELOPE)) {
//					return envelope(xml);
//				}
//				else skip(xml);
//			} 
//		}
		return null;
	}
	
	private static Envelope envelope()  {
		Envelope envelope = new Envelope();
		
//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.endsWith("Date")) {
//					FlatProperties.set(envelope, startName, dateTime(xml));
//				} else {
//					FlatProperties.set(envelope, startName, token(xml));
//				}
//			} 
//		}
		return envelope;
	}

}
