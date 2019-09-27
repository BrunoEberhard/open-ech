package ch.openech.model;

import static ch.openech.xml.EchReader.skip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Normalerweise können die catalog.txt entries beim Herunterladen erzeugt
 * werden. Bei Schemas im Review müssen die Schemas von Hand kopiert werden. Mit
 * dieser Klasse können dann aus den xsd die targetNamespace ausgelesen werden.
 * Diese Klasse wird somit nur sporadisch gebraucht.
 *
 */
public class EchCatalog {

	private final File file;
	
	private EchCatalog(File file) {
		this.file = file;
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
					String targetNamespace = startElement.getAttributeByName(new QName("targetNamespace")).getValue();
					System.out.println(targetNamespace + " = " + file.getName());
				}
				skip(xml);
			} else if (event.isEndElement() || event.isEndDocument()) {
				return;
			} // else skip
		}
	}


	public static void main(String... args) throws Exception {
		File dir = new File("src/main/xml");
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".xsd")) {
				EchCatalog instance = new EchCatalog(file);
				instance.process(file);
			}
		}
	}
}
