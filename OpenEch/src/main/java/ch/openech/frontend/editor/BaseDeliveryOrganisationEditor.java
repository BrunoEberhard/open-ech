package ch.openech.frontend.editor;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.util.LoggingRuntimeException;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.organisation.Organisation;
import ch.openech.transaction.OrganisationTransaction;
import ch.openech.xml.write.EchSchema;


public class BaseDeliveryOrganisationEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {
	private final OpenEchPreferences preferences;

	public BaseDeliveryOrganisationEditor(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.BASE_DELIVERY, echSchema);
	}

	@Override
	public Organisation newInstance() {
		return MoveInEditor.newInstance(preferences);
	}
	
	@Override
	public Object save(Organisation organisation) {
		String xml = getXml(organisation).get(0);
		Object insertId = Backend.getInstance().execute(new OrganisationTransaction(xml));
		return insertId;
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
