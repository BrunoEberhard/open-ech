package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;


import java.util.List;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.util.StringUtils;

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
		if (!StringUtils.isEmpty(namedId.personId)) {
			WriterElement element = parent.create(URI, name);
			
			element.text(ORGANISATION_ID_CATEGORY, namedId.personIdCategory);
			element.text(ORGANISATION_ID, namedId.personId);
		}
	}

}
