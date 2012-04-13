package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.LEGAL_FORM;
import static ch.openech.dm.XmlConstants.LOCAL_ORGANISATION_ID;
import static ch.openech.dm.XmlConstants.ORGANISATION_ADDITIONAL_NAME;
import static ch.openech.dm.XmlConstants.ORGANISATION_ID;
import static ch.openech.dm.XmlConstants.ORGANISATION_IDENTIFICATION;
import static ch.openech.dm.XmlConstants.ORGANISATION_ID_CATEGORY;
import static ch.openech.dm.XmlConstants.ORGANISATION_LEGAL_NAME;
import static ch.openech.dm.XmlConstants.ORGANISATION_NAME;
import static ch.openech.dm.XmlConstants.UID;
import static ch.openech.dm.XmlConstants.UID_ORGANISATION_ID;
import static ch.openech.dm.XmlConstants.UID_ORGANISATION_ID_CATEGORIE;
import static ch.openech.dm.XmlConstants._OTHER_ORGANISATION_ID;

import java.util.List;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.organisation.Organisation;

public class WriterEch0097 {

	public final String URI;
	
	public WriterEch0097(EchNamespaceContext context) {
		URI = context.getNamespaceURI(97);
	}
	public void organisationIdentification(WriterElement parent, Organisation values) throws Exception {
		organisationIdentification(parent, ORGANISATION_IDENTIFICATION, values);
	}
	
	public void organisationIdentification(WriterElement parent, String tagName, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, tagName);

		uidStructure(element, UID, values.uid);
		// localOrganisationId(element, values.technicalIds.id);
		namedId(element, values.technicalIds.localId, LOCAL_ORGANISATION_ID);
		namedId(element, values.technicalIds.otherId, _OTHER_ORGANISATION_ID); // VERSION
		element.values(values, ORGANISATION_NAME, ORGANISATION_LEGAL_NAME, ORGANISATION_ADDITIONAL_NAME, LEGAL_FORM);
    }
	
	public void uidStructure(WriterElement parent, String tagName, String uid) throws Exception {
		if (uid != null && uid.length() == 12) {
			WriterElement uidStructure = parent.create(URI, tagName);
			uidStructure.text(UID_ORGANISATION_ID_CATEGORIE, uid.substring(0, 3)); // TYPO by schema
			uidStructure.text(UID_ORGANISATION_ID, uid.substring(3));
		}
    }
	
	private void namedId(WriterElement parent, List<NamedId> namedIds, String name) throws Exception {
		for (NamedId namedPersonId : namedIds) {
			namedId(parent, namedPersonId, name);
		}
	}
	
	private void namedId(WriterElement parent, NamedId namedId, String name) throws Exception {
		WriterElement element = parent.create(URI, name);
		
		element.text(ORGANISATION_ID_CATEGORY, namedId.personIdCategory);
		element.text(ORGANISATION_ID, namedId.personId);
	}

}
