package ch.openech.xml.write;

import static ch.openech.xml.read.StaxEch.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class EchSchema {
	private static Map<String, EchSchema> contexts = new HashMap<String, EchSchema>();

	private final Map<Integer, String> namespaceURIs = new HashMap<Integer, String>();
	private final Map<Integer, String> namespaceLocations = new HashMap<Integer, String>();
	private final Map<Integer, Integer> namespaceVersions = new HashMap<Integer, Integer>();
	private final Map<Integer, Integer> namespaceMinorVersions = new HashMap<Integer, Integer>();
	private final int rootNumber;
	private String version;
	private String openEchNamespaceLocation;
	private WriterEch0020 writerEch0020;
	private WriterEch0112 writerEch0112;
	private WriterEch0148 writerEch0148;

	public EchSchema() {
		this.rootNumber = 0;
		this.version = null;
	}
	
	private EchSchema(int rootNumber, String version) {
		read(rootNumber, version);
		this.rootNumber = rootNumber;
		this.version = version;
	}
	
	public static EchSchema getNamespaceContext(int rootNumber, String version) {
		String location = EchNamespaceUtil.schemaLocation(rootNumber, version);
		if (!contexts.containsKey(location)) {
			EchSchema namespaceContext = new EchSchema(rootNumber, version);
			try {
				namespaceContext.read(location);
				contexts.put(location, namespaceContext);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contexts.get(location);
	}

	public static EchSchema getNamespaceContext( ch.openech.model.EchSchema schema) {
		return getNamespaceContext(schema.getSchemaNumber(), schema.getVersion() + "." + schema.getMinorVersion());
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
			if (rootNumber == 20) {
				readOpenEch(WriterOpenEch.URI);
			}
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

	//
	
	public WriterEch0020 getWriterEch0020() {
		if (writerEch0020 == null) {
			writerEch0020 = new WriterEch0020(this); // 1100 - 1400ms
		}
		return writerEch0020;
	}

	public WriterEch0112 getWriterEch0112() {
		if (writerEch0112 == null) {
			writerEch0112 = new WriterEch0112(this);
		}
		return writerEch0112;
	}

	public WriterEch0148 getWriterEch0148() {
		if (writerEch0148 == null) {
			writerEch0148 = new WriterEch0148(this); // 1100 - 1400ms
		}
		return writerEch0148;
	}
	
	//
	
	/* In den alten Versionen war es möglich, dass der Geburtsort bei
	 * der Geburtsmeldung auf unknown gesetzt war. Das wurde ab 2.0 mit
	 * einem eigenen Type unterbunden
	 */
	public boolean birthPlaceMustNotBeUnknown() {
		return getNamespaceVersion(20) > 1; //  !StringUtils.equals(ewkVersion, "1.0", "1.1");
	}
	
	/* Ab Version 1.1 und 2.2 exisitiert bei den Dialog Änderung Zustelladresse und 
	 * Korrektur Zustelladresse ein Feld "Kontakt gültig bis"
	 */
	public boolean contactHasValidTill() {
		return getNamespaceVersion(20) > 1 || getNamespaceMinorVersion(20) > 1; //  !StringUtils.equals(ewkVersion, "1.0", "2.0", "2.1");
	}
	
	/* Ab Version 1.1 und 2.2 kann bei Vormundschaftliche Massnahme
	 * angegeben werden, ob das Sorgerecht beinhatlet ist
	 */
	public boolean gardianMeasureRelationshipHasCare() {
		return getNamespaceVersion(20) == 1 && getNamespaceMinorVersion(20) > 0 || //
				 getNamespaceVersion(20) == 2 && getNamespaceMinorVersion(20) > 1 || //
				 getNamespaceVersion(20) > 2;
	}

	/* Ab Version 1.1 und 2.2 
	 */
	public boolean renewPermitHasTill() {
		return getNamespaceVersion(20) == 1 && getNamespaceMinorVersion(20) > 0 || //
				 getNamespaceVersion(20) == 2 && getNamespaceMinorVersion(20) > 1 || //
				 getNamespaceVersion(20) > 2;
	}

	/* In den alten Versionen musste bei der Korrektur der Berufsdaten mindestens
	 * Ein Beruf angegeben werden. Das war wahrscheinlich falsch, aber das
	 * XML verlangt es so.
	 */
	public boolean correctOccupationMustHaveOccupation() {
		return getNamespaceVersion(20) > 1 || getNamespaceMinorVersion(20) > 1; //  !StringUtils.equals(ewkVersion, "1.0", "2.0", "2.1");
	}
	
	/* Ab Version 1.1 und 2.2 existieren etwa ein Dutzend Meldegründe mehr,
	 * hauptsächlich Korrekturen
	 */
	public boolean additionalCorrectEvents() {
		return getNamespaceVersion(20) > 1 || getNamespaceMinorVersion(20) > 1; //  !StringUtils.equals(ewkVersion, "1.0", "2.0", "2.1");
	}
	
	/* Ab Version 2.0 wird residencePermitDetailed von eCH 6 nicht mehr verwendet
	 * sondern nur noch residencePermit
	 */
	public boolean residencePermitDetailed() {
		return getNamespaceVersion(20) == 1;  // return StringUtils.equals(ewkVersion, "1.0", "1.1");
	}

	/* In den Versionen 2.0 und 2.1 wurden bei eventCorrectRelationship die Namen
	 * der Eltern mitgeliefert.
	 */
	public boolean correctRelationshipPersonIncludesParents() {
		return getNamespaceVersion(20) == 1 || getNamespaceMinorVersion(20) == 2; // return StringUtils.equals(ewkVersion, "1.0", "1.1", "2.2");
	}
	
	/* Vor der Version 2.0 wurde bei einem "Schlüsseltausch" jeweils
	 * nur ein Schlüssel lieferbar, daher war die Funktionalität nicht brauchbar
	 */
	public boolean keyDeliveryPossible() {
		return getNamespaceVersion(20) > 1; // return !StringUtils.equals(ewkVersion, "1.0", "1.1");
	}
	
	/* Die Auswahl des Paragraphen wurde bei eCH0021 in
	 * der 3. Version reduziert auf ein paar wenige
	 */
	public boolean reducedBasedOnLawCode() {
		return getNamespaceVersion(20) > 1; // return ewkVersion.compareTo("2.0") >= 0;
	}
	
	/* basedOnLaw bei gardianMeasure wurde mit Version 2.1 nicht mehr zwingend
	 */
	public boolean basedOnLawCodeRequired() {
		return getNamespaceVersion(20) == 1 || getNamespaceVersion(20) == 2 && getNamespaceMinorVersion(20) == 0;
	}

	/* basedOnLawAddOn gibt es ab Version 2.3
	 */
	public boolean basedOnLawAddOn() {
		return getNamespaceVersion(20) >= 2 && getNamespaceMinorVersion(20) >= 3;
	}
	
	/* Ab 2.3 ist bei eventGardianMeasure und eventChangeGardian die
	 * relationship optional
	 */
	public boolean gardianRelationshipOptional() {
		return getNamespaceVersion(20) >= 2 && getNamespaceMinorVersion(20) >= 3;
	}

	/* Ab 2.3 gibt es einen 10. Typ von Beziehung
	 */
	public boolean typeOfRelationship10Exsists() {
		return getNamespaceVersion(20) >= 2 && getNamespaceMinorVersion(20) >= 3;
	}

	/*
	 * Beim Wechsel des Namens konnten bei den alten Versionen
	 * auch die Namen der Eltern gewechselt werden 
	 */
	public boolean changeNameWithParents() {
		return getNamespaceVersion(20) < 2; // return ewkVersion.compareTo("2.0") < 0;
	}

	/* 
	 * Die Extension in allen Meldegründen waren in 1.0 noch nicht verfügbar,
	 * jedoch ab 1.1 resp ab 2.0
	 */
	public boolean extensionAvailable() {
		return getNamespaceVersion(20) > 1 || getNamespaceMinorVersion(20) > 0;
	}

	/* 
	 * separationTill in maritalData existiert erst ab der 5. Version von eCH 11
	 */
	public boolean separationTillAvailable() {
		return getNamespaceVersion(11) >= 5;
	}
	
	/*
	 * Occupation enthält muss vor eCH 21 Version 4 das kindOfEmployment
	 * gesetzt haben. Bei späteren Versionen können alle Felder leer bleiben!
	 */
	public boolean kindOfEmploymentMandatory() {
		return getNamespaceVersion(21) < 4;
	}
	
	/*
	 * Occupation enthält aber eCH 21 Version 4 ein validTill 
	 */
	public boolean occupationValidTillAvailable() {
		return getNamespaceVersion(21) >= 4;
	}
	
	/*
	 * Geschäftsadressen haben keine "Haushaltsart" und "Haushaltsid"
	 */
	public boolean addressesAreBusiness() {
		return rootNumber == 148;
	}
	
}