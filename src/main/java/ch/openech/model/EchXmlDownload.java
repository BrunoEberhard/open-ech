package ch.openech.model;

import static ch.openech.xml.read.StaxEch.skip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.util.StringUtils;

import ch.openech.xml.write.EchNamespaceUtil;

public class EchXmlDownload {

	private final String schemaLocation;

	private EchXmlDownload() {
		this.schemaLocation = null;
	}

	private EchXmlDownload(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	private void download() {
		System.out.println("Download: " + schemaLocation);
		try {
			URL url = new URL(schemaLocation);
			String fileName = convert(url);
			File file = new File(fileName);
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				try (InputStream stream = url.openStream()) {
					try (ReadableByteChannel rbc = Channels.newChannel(stream)) {
						try (FileOutputStream fos = new FileOutputStream(file)) {
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
						}
					}
				} catch (FileNotFoundException f) {
					System.out.println("Not available: " + schemaLocation);
					return;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			try {
				process(file);
			} catch (XMLStreamException | IOException e) {
				throw new RuntimeException(e);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private String convert(URL url) {
		StringBuilder s = new StringBuilder("src/main/xml");

		String host = url.getHost();
		String[] hostParts = host.split("\\.");
		for (int i = hostParts.length - 1; i >= 0; i--) {
			if (i == 0 && hostParts[i].equals("www")) {
				continue;
			}
			s.append("/").append(hostParts[i]);
		}

		s.append(url.getPath());

		return s.toString();
	}

	private void process(File file) throws XMLStreamException, IOException {
		try (InputStream inputStream = new FileInputStream(file)) {
			process_(inputStream);
		}
	}

	private void process_(InputStream inputStream) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		process(xml);
	}

	private void process(XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("schema")) {
					schema(xml);
				} else
					skip(xml);
			} else if (event.isEndElement() || event.isEndDocument()) {
				return;
			} // else skip
		}
	}

	private void schema(XMLEventReader xml) throws XMLStreamException {
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

	private void imprt(StartElement startElement) throws XMLStreamException {
		// <xs:import namespace="http://www.ech.ch/xmlns/eCH-0011/8"
		// schemaLocation="http://www.ech.ch/xmlns/eCH-0011/8/eCH-0011-8-1.xsd"/>
		String schemaLocation = startElement.getAttributeByName(new QName("schemaLocation")).getValue();

		if (!StringUtils.isEmpty(schemaLocation)) {
			if (!schemaLocation.contains("/")) {
				schemaLocation = this.schemaLocation.substring(0, this.schemaLocation.lastIndexOf("/") + 1)
						+ schemaLocation;
			}
			new EchXmlDownload(schemaLocation).download();
		}

	}

	private static void download(int rootNumber, int major, int minor) {
		String rootNamespaceURI = EchNamespaceUtil.schemaURI(rootNumber, "" + major);
		EchXmlDownload download = new EchXmlDownload(EchNamespaceUtil.schemaLocation(rootNamespaceURI, "" + minor));
		download.download();
	}

	private static void process(InputStream inputStream) throws XMLStreamException {
		EchXmlDownload download = new EchXmlDownload();
		download.process_(inputStream);
	}

	public static void main(String... args) throws Exception {
		// download.download(20, 2, 3);
//		download(20, 3, 0);
//		download(116, 3, 0);
//		download(71, 1, 1);
//		download(72, 1, 0);
//		download(129, 4, 0); // Objektwesen
		download(132, 2, 0); // Objektwesen Steuern
		// download.download(147);
		// download.download(211, 1, 0);
		// download.download(21);
		// download.download(78);
		// download.download(93);
		// download.download(101);
		// download(108);
		// download.download(129);
		// download.download(148);
		// download.download(173);
		// download.download(196);
		// download.download(201);
		process(EchXmlDownload.class.getResourceAsStream("/ch/ech/xmlns/eCH-0211-1-0.xsd"));
	}
}
