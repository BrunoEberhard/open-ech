package  ch.openech.model;

import static ch.openech.xml.read.StaxEch.skip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.xml.write.EchNamespaceUtil;


public class EchXmlDownload {
	
	private final Set<String> locations = new HashSet<>();
	
	private EchXmlDownload() {
	}
	
	//
	
	public void download(int rootNumber) {
		int major = 1;
		MAJOR: while (true) {
			int minor = 0;
			MINOR: while (true) {
				if (!download(rootNumber, major, minor)) {
					break MINOR;
				}
				minor++;
			}
			if (minor == 0) {
				break MAJOR;
			}
			major++;
		}
	}
	
	
	public boolean download(int rootNumber, int major, int minor) {
		String rootNamespaceURI = EchNamespaceUtil.schemaURI(rootNumber, "" + major);
		String rootNamespaceLocation = EchNamespaceUtil.schemaLocation(rootNamespaceURI, "" + minor);

		if (locations.contains(rootNamespaceLocation)) {
			return false;
		}
		locations.add(rootNamespaceLocation);
		
		try {
			return download(rootNamespaceLocation);
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}
	
	private boolean download(String namespaceLocation) throws XMLStreamException, IOException {
		String fileName = namespaceLocation.substring(namespaceLocation.lastIndexOf("/"));
		File file = new File("src/main/xml/ch/ech/xmlns" + fileName);
		if (!file.exists()) {
			URL url = new URL(namespaceLocation);
			try (ReadableByteChannel rbc = Channels.newChannel(url.openStream())) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
			} catch (FileNotFoundException f) {
				return false;
			}
		}
		
		process(file);
		return true;
	}
	
	private void process(File file) throws XMLStreamException, IOException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new FileInputStream(file));
		process(xml);
	}
	
	private void process(XMLEventReader xml) throws XMLStreamException, IOException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("schema")) {
					schema(xml);
				} else skip(xml);
			} else if (event.isEndElement() || event.isEndDocument()) {
				return;
			} // else skip
		}
	}
	
	private void schema(XMLEventReader xml) throws XMLStreamException, IOException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("import")) {
					imprt(startElement);
					skip(xml);
				} else {
					skip(xml);
				}
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private void imprt(StartElement startElement) throws XMLStreamException, IOException {
		// 	<xs:import namespace="http://www.ech.ch/xmlns/eCH-0011/8" schemaLocation="http://www.ech.ch/xmlns/eCH-0011/8/eCH-0011-8-1.xsd"/>
		String schemaLocation = startElement.getAttributeByName(new QName("schemaLocation")).getValue();
		int schema = EchNamespaceUtil.extractSchemaNumber(schemaLocation);
		download(schema);
	}

	public static void main(String... args) {
		EchXmlDownload download = new EchXmlDownload();
		download.download(20);
		download.download(46);
		download.download(21);
		download.download(78);
		download.download(108);
		download.download(173);
		download.download(196);
		download.download(201);
	}
}
