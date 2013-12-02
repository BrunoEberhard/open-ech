package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.CANTON;
import static ch.openech.dm.XmlConstants.CANTONS;
import static ch.openech.dm.XmlConstants.CANTON_ABBREVIATION;
import static ch.openech.dm.XmlConstants.CANTON_LONG_NAME;
import static ch.openech.dm.XmlConstants.HISTORY_MUNICIPALITY_ID;
import static ch.openech.dm.XmlConstants.MUNICIPALITIES;
import static ch.openech.dm.XmlConstants.MUNICIPALITY;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_ID;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_LONG_NAME;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_SHORT_NAME;
import static ch.openech.dm.XmlConstants.NOMENCLATURE;
import static ch.openech.xml.read.StaxEch.integer;
import static ch.openech.xml.read.StaxEch.isEndTag;
import static ch.openech.xml.read.StaxEch.isStartTag;
import static ch.openech.xml.read.StaxEch.token;
import static ch.openech.xml.read.StaxEch.isEndDocument;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ch.openech.dm.common.Canton;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.util.StringUtils;

public class StaxEch0071 {

	private static StaxEch0071 instance;
	private final List<MunicipalityIdentification> municipalityIdentifications = new ArrayList<MunicipalityIdentification>(10000);
	private final List<Canton> cantons = new ArrayList<Canton>(30);
	private final List<MunicipalityIdentification> municipalitiesUnmodifiable;
	private final List<Canton> cantonsUnmodifiable;
	
	private StaxEch0071() {
		InputStream inputStream = this.getClass().getResourceAsStream("eCH0071.xml");
		try {
			process(inputStream);
		} catch (Exception x) {
			throw new RuntimeException("Read of cantons and municipalities failed", x);
		}
		Collections.sort(cantons);
		Collections.sort(municipalityIdentifications);
		
		municipalitiesUnmodifiable = Collections.unmodifiableList(municipalityIdentifications);
		cantonsUnmodifiable = Collections.unmodifiableList(cantons);
	}

	public static synchronized StaxEch0071 getInstance() {
		if (instance == null) {
			instance = new StaxEch0071();
		}
		return instance;
	}
	
	public List<Canton> getCantons() {
		return cantonsUnmodifiable;
	}
	
	public List<MunicipalityIdentification> getMunicipalityIdentifications() {
		return municipalitiesUnmodifiable;
	}

	private void municipality(XmlPullParser xmlParser) throws  SQLException, XmlPullParserException, IOException {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		
		int event = xmlParser.next();
		while (!MUNICIPALITY.equals(xmlParser.getName())) {
			if (isStartTag(event)) {
				String tagName = xmlParser.getName();
				if (HISTORY_MUNICIPALITY_ID.equals(tagName)) {
					municipalityIdentification.historyMunicipalityId = integer(xmlParser);
				} else if (MUNICIPALITY_ID.equals(tagName)) {
					municipalityIdentification.municipalityId = integer(xmlParser);
				} else if (MUNICIPALITY_LONG_NAME.equals(tagName)) {
					municipalityIdentification.municipalityName = token(xmlParser);
				} else if (MUNICIPALITY_SHORT_NAME.equals(tagName)) {
					String shortName = token(xmlParser);
					if (StringUtils.isBlank(municipalityIdentification.municipalityName)) {
						municipalityIdentification.municipalityName = shortName;
					}
				} else if (CANTON_ABBREVIATION.equals(tagName)) {
					municipalityIdentification.cantonAbbreviation.canton = token(xmlParser);
				} 
			} 
			event = xmlParser.next();
		}
		municipalityIdentifications.add(municipalityIdentification);
	}

	private void canton(XmlPullParser xmlParser) throws SQLException, XmlPullParserException, IOException {
		Canton canton = new Canton();
		int event = xmlParser.next();
		while ( !CANTON.equals(xmlParser.getName())) {
			if (isStartTag(event)) {
				if (xmlParser.getName().equals(CANTON_ABBREVIATION)) {
					canton.cantonAbbreviation.canton = StaxEch.token(xmlParser);
				} else if (xmlParser.getName().equals(CANTON_LONG_NAME)) {
					canton.cantonLongName = StaxEch.token(xmlParser);
				}
			}
			event = xmlParser.next();
		}
		cantons.add(canton);
	}
	
	private void municipalities(XmlPullParser xmlParser) throws  SQLException, XmlPullParserException, IOException {
		int event = xmlParser.next();
		while (!isEndTag(event) && !MUNICIPALITIES.equals(xmlParser.getName())) {
			if (isStartTag(event) && MUNICIPALITY.equals(xmlParser.getName()))
			{
				municipality(xmlParser);
			}
			event = xmlParser.next();
		}
	}

	private void cantons(XmlPullParser xmlParser) throws  SQLException, XmlPullParserException, IOException {
		int event =  xmlParser.next();
		while (!isEndTag(event) && !CANTONS.equals(xmlParser.getName())) {
			if (isStartTag(event) && CANTON.equals(xmlParser.getName())) {
				canton(xmlParser);
			}
			event = xmlParser.next();
		}
	}
	
	private void nomenclature(XmlPullParser xmlParser) throws SQLException, XmlPullParserException, IOException {
		int event = xmlParser.next();
		while (event != XmlPullParser.END_DOCUMENT) {
			if (isStartTag(event)) {
				if (MUNICIPALITIES.equals(xmlParser.getName())) {
					municipalities(xmlParser);
				} else if (CANTONS.equals(xmlParser.getName())) {
					cantons(xmlParser);
				}
			}
			event = xmlParser.next();
		}
	}
	
	
	private void process(InputStream inputStream) throws SQLException, XmlPullParserException, IOException {
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		xmlParser.setInput(new InputStreamReader(inputStream));
		do {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				if (NOMENCLATURE.equals(xmlParser.getName())) {
					nomenclature(xmlParser);
				}
			} else if (isEndTag(event)) {
				return;
			}
		} while (!isEndDocument(xmlParser.getEventType()));
	}
	
}
