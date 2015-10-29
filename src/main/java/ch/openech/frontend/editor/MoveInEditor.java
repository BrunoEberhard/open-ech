package ch.openech.frontend.editor;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.LoggingRuntimeException;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.frontend.page.OrganisationPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.organisation.Organisation;
import ch.openech.transaction.EchPersistence;
import ch.openech.transaction.OrganisationTransaction;
import ch.openech.xml.write.EchSchema;


public class MoveInEditor extends XmlEditor<Organisation, Organisation> implements XmlResult<Organisation> {

	public MoveInEditor(EchSchema ech) {
		super(ech);
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.MOVE_IN, echSchema);
	}

	@Override
	public Organisation createObject() {
		return newInstance();
	}

	static Organisation newInstance() {
		OpenEchPreferences preferences = EchPersistence.getPreferences();

		Organisation organisation = new Organisation();
		organisation.languageOfCorrespondance = preferences.preferencesDefaultsData.language;
		organisation.reportingMunicipality = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
		
		return organisation;
	}
	
	@Override
	public Organisation save(Organisation organisation) {
		String xml = getXml(organisation).get(0);
		return Backend.getInstance().execute(new OrganisationTransaction(xml));
	}
	
	@Override
	protected void finished(Organisation organisation) {
		Frontend.show(new OrganisationPage(echSchema, organisation));
	}
	
	@Override
	public List<String> getXml(Organisation organisation) {
		try {
			return Collections.singletonList(echSchema.getWriterEch0148().moveIn(organisation));
		} catch (Exception e) {
			throw new LoggingRuntimeException(e, logger, "XML generation failed");
		}
	}

}
