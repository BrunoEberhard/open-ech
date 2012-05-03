package ch.openech.client.org;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.org.OrganisationPanel.OrganisationPanelType;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;


public class BaseDeliveryOrganisationEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {
	private final WriterEch0148 writerEch0148;
	
	public BaseDeliveryOrganisationEditor(String version) {
		this(EchNamespaceContext.getNamespaceContext(148, version));
	}
	
	public BaseDeliveryOrganisationEditor(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
		this.writerEch0148 = new WriterEch0148(echNamespaceContext);
	}

	@Override
	public IForm<Organisation> createForm() {
		return new OrganisationPanel(OrganisationPanelType.BASE_DELIVERY, getEchNamespaceContext());
	}

	@Override
	public Organisation newInstance() {
		return MoveInEditor.newInstance(context);
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
		return Collections.singletonList(writerEch0148.organisationBaseDelivery(Collections.singletonList(organisation)));
	}

}
