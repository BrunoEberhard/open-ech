package editor;

import java.util.Collections;
import java.util.List;

import page.OrganisationViewPage;
import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.org.OrganisationPanel;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;


public class BaseDeliveryOrganisationEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {
	
	public BaseDeliveryOrganisationEditor(PageContext context, String version) {
		super(EchSchema.getNamespaceContext(148, version), (OpenEchPreferences) context.getApplicationContext().getPreferences() );
	}

	@Override
	public IForm<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.BASE_DELIVERY, echSchema);
	}

	@Override
	public Organisation newInstance() {
		return MoveInEditor.newInstance(preferences);
	}

	@Override
	public boolean save(Organisation organisation) {
		String xml;
		try {
			xml = getXml(organisation).get(0);
			EchServer.getInstance().processOrg(xml);
			setFollowLink(Page.link(OrganisationViewPage.class, echSchema.getVersion(), getObject().getId()));
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Organisation konnte nicht gespeichert werden", e);
		}
	}
	
	@Override
	public List<String> getXml(Organisation organisation) throws Exception {
		return Collections.singletonList(echSchema.getWriterEch0148().organisationBaseDelivery(Collections.singletonList(organisation)));
	}

}
