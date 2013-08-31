package ch.openech.client.editor;

import java.util.Collections;
import java.util.List;

import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.org.OrganisationPanel;
import ch.openech.client.page.OrganisationViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.page.PageLink;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;


public class MoveInEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {

	private final OpenEchPreferences preferences;
	
	public MoveInEditor(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}

	@Override
	public IForm<Organisation> createForm() {
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
		String xml;
		try {
			xml = getXml(organisation).get(0);
			EchServer.getInstance().processOrg(xml);
			return PageLink.link(OrganisationViewPage.class, echSchema.getVersion(), getObject().getId());
		} catch (Exception e) {
			throw new RuntimeException("Organisation konnte nicht gespeichert werden", e);
		}
	}
	
	@Override
	public List<String> getXml(Organisation organisation) throws Exception {
		return Collections.singletonList(echSchema.getWriterEch0148().moveIn(organisation));
	}

}
