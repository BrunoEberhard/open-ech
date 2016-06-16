package ch.openech.frontend.editor;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.LoggingRuntimeException;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.model.organisation.Organisation;
import ch.openech.transaction.OrganisationTransaction;
import ch.openech.xml.write.EchSchema;


public class BaseDeliveryOrganisationEditor extends XmlEditor<Organisation, Organisation> implements XmlResult<Organisation> {

	public BaseDeliveryOrganisationEditor(EchSchema ech) {
		super(ech);
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.BASE_DELIVERY, echSchema);
	}

	@Override
	public Organisation createObject() {
		return MoveInEditor.newInstance();
	}
	
	@Override
	public Organisation save(Organisation organisation) {
		String xml = getXml(organisation).get(0);
		return Backend.execute(new OrganisationTransaction(xml));
	}
	
	@Override
	public List<String> getXml(Organisation organisation) {
		try {
			return Collections.singletonList(echSchema.getWriterEch0148().organisationBaseDelivery(Collections.singletonList(organisation)));
		} catch (Exception e) {
			throw new LoggingRuntimeException(e, logger, "XML generation failed");
		}
	}

}
