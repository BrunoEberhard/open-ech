package ch.openech.model;

import static ch.openech.xml.EchReader.skip;

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
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.util.StringUtils;

import ch.openech.xml.write.EchNamespaceUtil;

// Ausgabe kann in eclipse user catalog kopiert werden in
// workspace/.metadata/.plugins/org.eclipse.wst.xml.core/usser_catalog.xml
// C:\projects\workspaces\minimalj\.metadata\.plugins\org.eclipse.wst.xml.core

public class EchXmlDownload {

	private String schemaLocation;
	private String targetNamespace;
	private static Map<String, String> fileByNamespace = new HashMap<>();
	
	private EchXmlDownload() {
		this.schemaLocation = null;
	}

	private EchXmlDownload(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	private void download() {
		try {
			URL url = new URL(schemaLocation);
			String fileName = convert(url);

			if ("http://www.ech.ch/xmlns/eCH-0084/1/eCH-0084-commons-1-4.xsd".equals(schemaLocation)) {
				schemaLocation = "http://www.ech.ch/xmlns/eCH-0084/1/eCH-0084-commons-1-3.xsd";
			}
			if ("http://www.ech.ch/xmlns/eCH-0147/1/eCH-0147T0.xsd".equals(schemaLocation)) {
				schemaLocation = "http://share.ech.ch/xmlns/eCH-0147/1.0/eCH-0147T0.xsd";
			}
			if ("http://www.ech.ch/xmlns/eCH-0147/1/eCH-0147T1.xsd".equals(schemaLocation)) {
				schemaLocation = "http://share.ech.ch/xmlns/eCH-0147/1.0/eCH-0147T1.xsd";
			}
			if ("http://www.ech.ch/xmlns/eCH-0147/1/eCH-0147T2.xsd".equals(schemaLocation)) {
				schemaLocation = "http://share.ech.ch/xmlns/eCH-0147/1.0/eCH-0147T2.xsd";
			}
			if ("http://www.ech.ch/xmlns/eCH-0148/1/eCH-0148-1-2.xsd".equals(schemaLocation)) {
				schemaLocation = "https://www.ech.ch/dokument/bcc30760-77bf-4ed9-ad64-89cb5c6603bb";
			}
			if ("http://www.ech.ch/xmlns/eCH-0213/1.0/eCH-0213-commons-1-0.xsd".equals(schemaLocation)) {
				schemaLocation = "https://www.ech.ch/dokument/2fc0d695-874c-45ab-b829-62df35b188a0";
			}
			if ("http://www.ech.ch/xmlns/eCH-0215/1.0/eCH-0215-1-0.xsd".equals(schemaLocation)) {
				schemaLocation = "http://www.ech.ch/xmlns/eCH-0215/1/eCH-0215-1-0.xsd";
			}
			if ("http://www.ech.ch/xmlns/eCH-0215/2.0/eCH-0215-2-0.xsd".equals(schemaLocation)) {
				schemaLocation = "http://www.ech.ch/xmlns/eCH-0215/2/eCH-0215-2-0.xsd";
			}

			url = new URL(schemaLocation);
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
					// System.out.println("Not available: " + schemaLocation);
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
			
			System.out.println(" <uri name=\"" + targetNamespace + "\" + uri=\"platform:/resource/openech/" + fileName + "\"/>");
			System.out.println(" <system systemId=\"" + url.toExternalForm() + "\" + uri=\"platform:/resource/openech/" + fileName + "\"/>");

			fileByNamespace.put(targetNamespace, fileName.substring(fileName.lastIndexOf("/") + 1));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private String convert(URL url) {
		StringBuilder s = new StringBuilder("src/main/xml");

		String path = url.getPath();
		s.append(path.substring(path.lastIndexOf('/')));

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
					targetNamespace = startElement.getAttributeByName(new QName("targetNamespace")).getValue();
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
			if ("http://www.ech.ch/xmlns/eCH-0213-commons/1/eCH-0213-commons-1-0.xsd".equals(schemaLocation)) {
				schemaLocation = "http://www.ech.ch/xmlns/eCH-0213/1.0/eCH-0213-commons-1-0.xsd";
			}
			new EchXmlDownload(schemaLocation).download();
		}

	}

	private static void download(int rootNumber, int major, int minor) {
		String rootNamespaceURI = EchNamespaceUtil.schemaURI(rootNumber, "" + major);
		String location = EchNamespaceUtil.schemaLocation(rootNamespaceURI, "" + minor);
		download(location);
	}

	private static void download(String location) {
		EchXmlDownload download = new EchXmlDownload(location);
		download.download();
	}

	private static void process(InputStream inputStream) throws XMLStreamException {
		EchXmlDownload download = new EchXmlDownload();
		download.process_(inputStream);
	}

	public static void main(String... args) throws Exception {
		download(20, 3, 0); // Person mutation
		// download(11, 8, 1); // Person wird von 20 geholt
		download(71, 1, 1); // Orte
		download(72, 1, 0); // Länder

		download(215, 2, 0); // sektoriellen Personenidentifikator

//		download(20, 2, 3); // Person mutation, alte Version zu Zeit nicht mehr vorgesehen

//		download(129, 4, 0); // Objektwesen
//		download(132, 2, 0); // Objektwesen Steuern
		// download("http://www.ech.ch/xmlns/eCH-0147/1.0/eCH-0147T0.xsd");
		// download("http://www.ech.ch/xmlns/eCH-0147/1.0/eCH-0147T1.xsd");
		// download("http://www.ech.ch/xmlns/eCH-0147/1.0/eCH-0147T2.xsd");
		download(211, 1, 0);
		// download(21, 7, 0); // Personergänzung wird von 20 und anderen geholt

		// in ech 20, Version 3: "Der Meldungsrahmen wird neu durch den eCH-0058 statt
		// den eCH-0078 implementiert. Der eCH-0078 wird nicht mehr weiter gepflegt."
		download(78, 4, 0);

		// download(58, 5, 0);
		download(93, 3, 0);
//		download(101, 1, 0); Alle Daten von PersonExtendedInformation sind bereits in Person integriert
		download(108, 5, 0);
		// download(129, 4, 0); wird von 211 geholt
		download(148, 1, 2);
		download(173, 1, 0);
		download(196, 1, 0);
		download(201, 1, 0);
		download(116, 4, 0); // UID Meldegründe
		download(211, 1, 0);
		download(212, 1, 0);
		// für xsd die noch nicht fertig sind
		// process(EchXmlDownload.class.getResourceAsStream("/eCH-0211-1-0.xsd"));
		// process(EchXmlDownload.class.getResourceAsStream("/eCH-0212-1-0.xsd"));

		download(215, 1, 0);

		download(229, 1, 0);

		for (String namespace : fileByNamespace.keySet()) {
			System.out.println(namespace + " = " + fileByNamespace.get(namespace));
		}
	}
}
