package  ch.openech.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.util.StringUtils;

import ch.openech.xml.write.EchNamespaceUtil;

public class XmlConstantsGenerator {

	private SortedSet<String> names = new TreeSet<String>();
	
	public void read(int rootNumber, String version) throws XMLStreamException, IOException {
		int pos = version.indexOf('.');
		String major = version.substring(0, pos);
		String minor = version.substring(pos + 1);
		
		String rootNamespaceURI = EchNamespaceUtil.schemaURI(rootNumber, major);
		String rootNamespaceLocation = EchNamespaceUtil.schemaLocation(rootNamespaceURI, minor);
		
		process(rootNamespaceLocation);
	}
	
	public void print() {
		for (String name : names) {
			System.out.print("public static final String ");
			System.out.print(StringUtils.toConstant(name));
			System.out.print(" = \""); System.out.print(name); System.out.println("\";");
		}
	}
	
	public void process(String namespaceLocation) throws XMLStreamException, IOException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = null;
		try {
			xml = inputFactory.createXMLEventReader(new URL(namespaceLocation).openStream());
			process(xml);
		} catch (Exception e) {
			// this could happen in SBB ;)
			InputStream stream = EchNamespaceUtil.getLocalCopyOfSchema(namespaceLocation);
			xml = inputFactory.createXMLEventReader(stream);
			process(xml);
		}
	}

	private void process(XMLEventReader xml) throws XMLStreamException, IOException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("element")) {
					Attribute attribute = startElement.getAttributeByName(new QName("name"));
					names.add(attribute.getValue());
				} 
			} 
		}
	}
	
	public static void main(String... args) throws XMLStreamException, IOException {
		XmlConstantsGenerator generator = new XmlConstantsGenerator();
		generator.read(7, "4.0");
		generator.read(10, "4.0");
		generator.read(8, "2.0");
		generator.read(20, "2.3");
		generator.read(21, "4.0");
		generator.read(11, "5.0");
		generator.read(44, "2.0");
		generator.read(46, "2.0");
		generator.read(58, "3.0");
		generator.read(71, "1.1");
		generator.read(72, "1.0");
		generator.read(78, "3.0");
		generator.read(90, "1.0");
		generator.read(93, "1.0");
		generator.read(97, "1.0");
		generator.read(98, "1.0");
		generator.read(101, "1.0");
		generator.read(102, "1.0");
		generator.read(108, "1.0");
		generator.read(112, "1.0");
		generator.read(148, "1.0");
		generator.read(196, "1.0");
		generator.read(217, "1.0");
		generator.print();
	}
	
}
