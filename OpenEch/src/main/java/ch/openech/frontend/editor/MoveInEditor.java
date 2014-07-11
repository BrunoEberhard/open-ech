package ch.openech.frontend.editor;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.LoggingRuntimeException;
import org.minimalj.util.SerializationContainer;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.frontend.page.OrganisationViewPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import  ch.openech.model.organisation.Organisation;
import ch.openech.transaction.OrganisationTransaction;
import ch.openech.xml.write.EchSchema;


public class MoveInEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {

	private final OpenEchPreferences preferences;
	
	public MoveInEditor(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.MOVE_IN, echSchema);
	}

	@Override
	public Organisation newInstance() {
		return newInstance(preferences);
	}

	static Organisation newInstance(OpenEchPreferences preferences) {
		Organisation organisation = new Organisation();
		
		organisation.languageOfCorrespondance = preferences.preferencesDefaultsData.language;
		organisation.reportingMunicipality = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
		
		return organisation;
	}
	
	@Override
	public String save(Organisation organisation) {
		String xml = getXml(organisation).get(0);
		organisation = (Organisation) SerializationContainer.unwrap(Backend.getInstance().execute(new OrganisationTransaction(xml)));
		return PageLink.link(OrganisationViewPage.class, echSchema.getVersion(), organisation.getId());
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
