package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import org.minimalj.util.StringUtils;

import ch.openech.model.person.Occupation;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.Relation;

public class WriterEch0021 {

	public final String URI;
	public final WriterEch0010 ech10;
	public final WriterEch0011 ech11;
	public final WriterEch0044 ech44;
	
	public WriterEch0021(EchSchema context) {
		URI = context.getNamespaceURI(21);
		ech10 = new WriterEch0010(context);
		ech11 = new WriterEch0011(context);
		ech44 = new WriterEch0044(context);
	}

	public void occupation(WriterElement parent, Occupation occupation) throws Exception {
		WriterElement occupationElement = parent.create(URI, OCCUPATION);

		occupationElement.values(occupation, JOB_TITLE, KIND_OF_EMPLOYMENT, EMPLOYER);

		if (occupation.placeOfWork != null) {
			ech10.addressInformation(occupationElement, PLACE_OF_WORK, occupation.placeOfWork);
		}

		if (occupation.placeOfEmployer != null) {
			ech10.addressInformation(occupationElement, PLACE_OF_EMPLOYER, occupation.placeOfEmployer);
		}
		
		occupationElement.values(occupation, OCCUPATION_VALID_TILL);
	}

	public void relation(WriterElement parent, Relation relation) throws Exception {
		relation(parent, _RELATIONSHIP, relation);
	}

	public void relation(WriterElement parent, String tagName, Relation relation) throws Exception {
		// in dieser Version 20-1-0 wird Relationship noch gross geschrieben (!)

		if (relation.partner != null) {
			WriterElement relationElement = parent.create(URI, tagName);

			relationElement.values(relation, TYPE_OF_RELATIONSHIP, BASED_ON_LAW, CARE);

			WriterElement partnerElement = relationElement.create(URI, PARTNER);

			if (relation.partner.person != null) {
				ech44.personIdentification(partnerElement, relation.partner.person.personIdentification());
			} else if (relation.partner.personIdentification != null) {
				ech44.personIdentificationPartner(partnerElement, relation.partner.personIdentification);
			} else if (relation.partner.organisation != null) {
				throw new RuntimeException("TODO");
			}

			if (relation.address != null) {
				ech10.address(partnerElement, ADDRESS, relation.address);
			}
		}
	}

	public void placeOfOriginAddon(WriterElement parent, String tagName, PlaceOfOrigin placeOfOrigin) throws Exception {
		WriterElement placeOfOriginElement = parent.create(URI, tagName);

		ech11.placeOfOrigin(placeOfOriginElement, ORIGIN, placeOfOrigin);
		placeOfOriginElement.values(placeOfOrigin, REASON_OF_ACQUISITION, NATURALIZATION_DATE, EXPATRIATION_DATE);
	}

	public void nameOfParentAtBirth(WriterElement parent, String name, String firstName, String officialName) throws Exception {
		if (!StringUtils.isBlank(firstName) || !StringUtils.isBlank(officialName)) {
			WriterElement element = parent.create(URI, name);
			element.text(FIRST_NAME, firstName);
			element.text(OFFICIAL_NAME, officialName);
		}
	}

}
