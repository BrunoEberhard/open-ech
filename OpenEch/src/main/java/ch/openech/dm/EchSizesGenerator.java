package ch.openech.dm;

import static ch.openech.xml.read.StaxEch.skip;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.xml.write.EchNamespaceUtil;


public class EchSizesGenerator {
	
	private final Map<Integer, String> namespaceURIs = new HashMap<Integer, String>();
	private final Map<Integer, String> namespaceLocations = new HashMap<Integer, String>();
	private final Map<Integer, Integer> namespaceVersions = new HashMap<Integer, Integer>();
	private final Map<Integer, Integer> namespaceMinorVersions = new HashMap<Integer, Integer>();
	private final int rootNumber;
	private String version;
	private String openEchNamespaceLocation;
	private static Set<String> lines = new TreeSet<>();
	
	private EchSizesGenerator(int rootNumber, String version) {
		read(rootNumber, version);
		this.rootNumber = rootNumber;
		this.version = version;
	}
	
	//
	
	private void read(int rootNumber, String version) {
		int pos = version.indexOf('.');
		String major = version.substring(0, pos);
		String minor = version.substring(pos + 1);
		
		String rootNamespaceURI = EchNamespaceUtil.schemaURI(rootNumber, major);
		String rootNamespaceLocation = EchNamespaceUtil.schemaLocation(rootNamespaceURI, minor);

		try {
			read(rootNamespaceLocation);
//			if (rootNumber == 20) {
//				readOpenEch(WriterOpenEch.URI);
//			}
		} catch (Exception x) {
			// TODO
			x.printStackTrace();
		}
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getNamespaceURI(int namespaceNumber) {
		return namespaceURIs.get(namespaceNumber);
	}
	
	public String getNamespaceLocation(int namespaceNumber) {
		return namespaceLocations.get(namespaceNumber);
	}
	
	public int getNamespaceVersion(int namespaceNumber) {
		return namespaceVersions.get(namespaceNumber);
	}
	
	public int getNamespaceMinorVersion(int namespaceNumber) {
		return namespaceMinorVersions.get(namespaceNumber);
	}
	
	public Set<Integer> getNamespaceNumbers() {
		return namespaceURIs.keySet();
	}
	
	public String getOpenEchNamespaceLocation() {
		return openEchNamespaceLocation;
	}
	
	private void read(String namespaceLocation) throws XMLStreamException, IOException {
		if (namespaceLocations.containsValue(namespaceLocation)) return;
		registerLocation(namespaceLocation);
		process(namespaceLocation);
	}
	
	private void readOpenEch(String openEchNamespaceLocation) throws XMLStreamException, IOException {
		this.openEchNamespaceLocation = openEchNamespaceLocation;
		process(openEchNamespaceLocation);
	}
	
	private void process(String namespaceLocation) throws XMLStreamException, IOException {
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
	
	private void registerLocation(String namespaceLocation) {
		int namespaceNumber = EchNamespaceUtil.extractSchemaNumber(namespaceLocation);
		String namespaceURI = EchNamespaceUtil.schemaURI(namespaceLocation);
		int namespaceVersion = EchNamespaceUtil.extractSchemaMajorVersion(namespaceLocation);
		int namespaceMinorVersion = EchNamespaceUtil.extractSchemaMinorVersion(namespaceLocation);
		namespaceURIs.put(namespaceNumber, namespaceURI);
		namespaceLocations.put(namespaceNumber, namespaceLocation);
		namespaceVersions.put(namespaceNumber, namespaceVersion);
		namespaceMinorVersions.put(namespaceNumber, namespaceMinorVersion);
	}
	
	private void process(XMLEventReader xml) throws XMLStreamException, IOException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("schema")) {
					imports(xml);
				} else skip(xml);
			} else if (event.isEndElement() || event.isEndDocument()) {
				return;
			} // else skip
		}
	}
	
	private void imports(XMLEventReader xml) throws XMLStreamException, IOException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("import")) {
					imprt(startElement);
					skip(xml);
				} else if (startName.equals("simpleType")) {
					String name = startElement.getAttributeByName(new QName("name")).getValue();
					if (name.endsWith("Type")) {
						name = name.substring(0, name.length()-4);
						simpleType(name, xml);				
					}
				} else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private void imprt(StartElement startElement) throws XMLStreamException, IOException {
		String schemaLocation = startElement.getAttributeByName(new QName("schemaLocation")).getValue();
		read(schemaLocation);
	}
	
	private void simpleType(String name, XMLEventReader xml) throws XMLStreamException, IOException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("restriction")) {
					Attribute base = startElement.getAttributeByName(new QName("base"));
					boolean intBase = base != null && base.getValue().contains("int");
					restriction(name, xml, intBase);
				}
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private void restriction(String name, XMLEventReader xml, boolean intBase) throws XMLStreamException, IOException {
		int size = -1;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals("maxLength")) {
					String value = startElement.getAttributeByName(new QName("value")).getValue();
					size = Integer.parseInt(value);
				} 
//				else if (startName.equals("enumeration")) {
//					String value = startElement.getAttributeByName(new QName(VALUE)).getValue();
//					size = Math.max(size, value.length());
//				} else if (startName.equals("maxInclusive")) {
//					String value = startElement.getAttributeByName(new QName(VALUE)).getValue();
//					size = Math.max(size, value.length());
//				}
				skip(xml);
			} else if (event.isEndElement()) {
				if (size > 0) {
					lines.add("public static final int " + name + " = " + size + ";");
				}
				return;
			} // else skip
		}
	}

	public static void main(String... args) {
		new EchSizesGenerator(108, "1.0");
		new EchSizesGenerator(46, "2.0");
		new EchSizesGenerator(20, "2.2");
		new EchSizesGenerator(21, "4.0");
		
		for (String line : lines) {
			System.out.println(line);
		}
	}
}
