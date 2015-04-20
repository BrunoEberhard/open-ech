package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import java.util.List;

import org.minimalj.model.ViewUtil;

import ch.openech.model.common.NamedId;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.organisation.UidStructure;
import ch.openech.model.organisation.types.LegalForm;

public class WriterEch0097 {

	public final String URI;
	
	public WriterEch0097(EchSchema context) {
		URI = context.getNamespaceURI(97);
	}

	public void organisationIdentification(WriterElement parent, Organisation values) throws Exception {
		OrganisationIdentification organisationIdentification = ViewUtil.view(values, new OrganisationIdentification());
		organisationIdentification(parent, organisationIdentification);
	}

	public void organisationIdentification(WriterElement parent, OrganisationIdentification values) throws Exception {
		organisationIdentification(parent, ORGANISATION_IDENTIFICATION, values);
	}
	
	public void organisationIdentification(WriterElement parent, String tagName, OrganisationIdentification values) throws Exception {
		WriterElement element = parent.create(URI, tagName);

		uidStructure(element, UID, values.uid);
		// localOrganisationId(element, values.technicalIds.id);
		namedId(element, values.technicalIds.localId, LOCAL_ORGANISATION_ID);
		namedId(element, values.technicalIds.otherId, _OTHER_ORGANISATION_ID); // VERSION
		element.values(values, ORGANISATION_NAME, ORGANISATION_LEGAL_NAME, ORGANISATION_ADDITIONAL_NAME);
		legalForm(element, values.legalForm);
    }
	
	public void uidStructure(WriterElement parent, String tagName, UidStructure uid) throws Exception {
		if (uid != null && uid.value != null && uid.value.length() == UidStructure.LENGTH) {
			WriterElement uidStructure = parent.create(URI, tagName);
			uidStructure.text(UID_ORGANISATION_ID_CATEGORIE, uid.value.substring(0, 3)); // TYPO by schema
			uidStructure.text(UID_ORGANISATION_ID, uid.value.substring(3));
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

	private void legalForm(WriterElement parent, LegalForm legalForm) throws Exception {
		if (legalForm != null) {
			parent.text(LEGAL_FORM, legalForm.id);
		}
	}
	
}