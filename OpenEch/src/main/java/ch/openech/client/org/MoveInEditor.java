package ch.openech.client.org;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.org.OrganisationPanel.OrganisationPanelType;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;


public class MoveInEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {
	private final WriterEch0148 writerEch0148;
	
	public MoveInEditor(String version) {
		this(EchNamespaceContext.getNamespaceContext(148, version));
	}
	
	public MoveInEditor(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
		this.writerEch0148 = new WriterEch0148(echNamespaceContext);
	}

	@Override
	public IForm<Organisation> createForm() {
		return new OrganisationPanel(OrganisationPanelType.MOVE_IN, getEchNamespaceContext());
	}

	@Override
	public Organisation newInstance() {
		return newInstance(context);
	}

	static Organisation newInstance(PageContext context) {
		Organisation organisation = new Organisation();
		
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		organisation.languageOfCorrespondance = preferences.preferencesDefaultsData.language;
		organisation.reportingMunicipality = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
		
		return organisation;
	}
	
	@Override
	public void validate(Organisation object, List<ValidationMessage> resultList) {
	}

	@Override
	public boolean save(Organisation organisation) {
		String xml;
		try {
			xml = getXml(organisation).get(0);
			EchServer.getInstance().processOrg(xml);
			setFollowLink(Page.link(OrganisationViewPage.class, getEchNamespaceContext().getVersion(), getObject().getId()));
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Organisation konnte nicht gespeichert werden", e);
		}
	}
	
	@Override
	public List<String> getXml(Organisation organisation) throws Exception {
		return Collections.singletonList(writerEch0148.moveIn(organisation));
	}

}
