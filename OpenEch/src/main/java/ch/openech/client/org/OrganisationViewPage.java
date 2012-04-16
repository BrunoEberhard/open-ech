package ch.openech.client.org;

import ch.openech.client.org.OrganisationPanel.OrganisationPanelType;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.application.ObjectViewPage;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class OrganisationViewPage extends ObjectViewPage<Organisation> {

	private final String organisationId;
	private final EchNamespaceContext echNamespaceContext;
	private final OrganisationPanel organisationPanel;
	private final OrganisationMenu menu;

	public OrganisationViewPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public OrganisationViewPage(PageContext context, String version, String organisationId) {
		super(context);
		this.organisationId = organisationId;
		this.echNamespaceContext = EchNamespaceContext.getNamespaceContext(148, version);
		this.organisationPanel = new OrganisationPanel(OrganisationPanelType.DISPLAY, echNamespaceContext);
		this.menu = new OrganisationMenu(echNamespaceContext); 
	}

	@Override
	protected void showObject(Organisation organisation) {
		super.showObject(organisation);
		menu.setOrganisation(organisation, true);
		setTitle(pageTitle(organisation));
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		menu.fillActionGroup(pageContext, actionGroup);
	}

	@Override
	protected Organisation loadObject() {
		return EchServer.getInstance().getPersistence().organisation().getByLocalOrganisationId(organisationId);
	}

	@Override
	public FormVisual<Organisation> createForm() {
		return organisationPanel;
	}
	
	private static final int MAX_NAME_LENGTH = 20;
	
	private static String pageTitle(Organisation organisation) {
		String title;
		if (organisation.organisationName != null) {
			if (organisation.organisationName.length() <= MAX_NAME_LENGTH) {
				title = organisation.organisationName;
			} else {
				title = organisation.organisationName.substring(0, MAX_NAME_LENGTH-1) + "..";
			}
		} else {
			title = Resources.getString("Organisation");
		}
		return title;
	}

}
