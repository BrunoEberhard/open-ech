package ch.openech.xml.read;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.person.NameOfParent;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.util.StringUtils;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

public class StaxEch0021 {

	public static Occupation occupation(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Occupation occupation = new Occupation();
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(JOB_TITLE)) occupation.jobTitle = token(xmlParser);
				else if (startName.equals(KIND_OF_EMPLOYMENT)) occupation.kindOfEmployment = token(xmlParser);
				else if (startName.equals(EMPLOYER)) occupation.employer = token(xmlParser);
				else if (startName.equals(PLACE_OF_WORK)) occupation.placeOfWork = StaxEch0010.address(xmlParser);
				else if (startName.equals(PLACE_OF_EMPLOYER)) occupation.placeOfEmployer = StaxEch0010.address(xmlParser);
				else if (startName.equals(OCCUPATION_VALID_TILL)) occupation.occupationValidTill = date(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return occupation;
			} // else skip
		}
	}
	
	// partner meint immer "Identifikation der anderen Person einer Beziehung"
	public static PersonIdentification partner(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PersonIdentification personIdentification = null;
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_IDENTIFICATION) || //
						startName.equals(PERSON_IDENTIFICATION_PARTNER) ||
						startName.equals(PARTNER_ID_ORGANISATION)) personIdentification = StaxEch0044.personIdentification(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return personIdentification;
			} // else skip
		}
	}
	
	public static void relation(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException {
		Relation relation = null;
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(TYPE_OF_RELATIONSHIP) || startName.equals(PARTNERSHIP_TYPE_OF_RELATIONSHIP_TYPE)) {
					relation = new Relation();
					relation.typeOfRelationship = (TypeOfRelationship) StaxEch.enuum(TypeOfRelationship.class, token(xmlParser));
					if (relation.isParent()) relation.care = YesNo.Yes;
					person.relation.add(relation);
				}
				else if (startName.equals(BASED_ON_LAW)) enuum(xmlParser, relation, Relation.RELATION.basedOnLaw); 
				else if (startName.equals(BASED_ON_LAW_ADD_ON)) relation.basedOnLawAddOn = token(xmlParser);
				else if (startName.equals(CARE)) enuum(xmlParser, relation, Relation.RELATION.care);
				else if (startName.equals(PARTNER)) relation.partner = partner(xmlParser);
				else if (startName.equals(ADDRESS)) relation.address = StaxEch0010.address(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	// Das ist von der Signatur her zur Zeit eine grosse Ausnahme. Normalerweise würde einfach
	// ein PlaceOfOrigin zurückgeliefert. Hier wird jedoch zusätzlich überprüft, ob ein schon
	// bestehender Eintrag in der Liste ersetzt werden muss.
	public static void addPlaceOfOriginAddon(XmlPullParser xmlParser,  List<PlaceOfOrigin> placeOfOrigins) throws XmlPullParserException, IOException  {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(ORIGIN)) StaxEch0011.placeOfOrigin(xmlParser, placeOfOrigin);
				else if (startName.equals(REASON_OF_ACQUISITION)) enuum(xmlParser, placeOfOrigin, PlaceOfOrigin.PLACE_OF_ORIGIN.reasonOfAcquisition);
				else if (startName.equals(NATURALIZATION_DATE)) placeOfOrigin.naturalizationDate = StaxEch.date(xmlParser);
				else if (startName.equals(EXPATRIATION_DATE)) placeOfOrigin.expatriationDate = StaxEch.date(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				// eventuell wurden diese Heimatorte schon mit anyPerson von e11 geliefert.
				updatePlaceOfOrigin(placeOfOrigins, placeOfOrigin);
				return;
			} // else skip
		}
	}
	
	public static void updatePlaceOfOrigin(List<PlaceOfOrigin> placeOfOrigins, PlaceOfOrigin placeOfOrigin) {
		for (PlaceOfOrigin p : placeOfOrigins) {
			if (StringUtils.equals(placeOfOrigin.originName, p.originName) && StringUtils.equals(placeOfOrigin.cantonAbbreviation.canton, p.cantonAbbreviation.canton)) {
				placeOfOrigins.remove(p); break;
			}
		}
		placeOfOrigins.add(placeOfOrigin);
	}
	
	public static void nameOfParentAtBirth(XmlPullParser xmlParser, NameOfParent nameAtBirth) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String name = xmlParser.getName();
				if (name.equals(FIRST_NAME)) nameAtBirth.firstName = token(xmlParser);
				else if (name.equals(OFFICIAL_NAME)) nameAtBirth.officialName = token(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
}
