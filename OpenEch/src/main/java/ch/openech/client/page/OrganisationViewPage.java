package ch.openech.client.page;

import ch.openech.client.org.OrganisationMenu;
import ch.openech.client.org.OrganisationPanel;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.ObjectViewPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;

public class OrganisationViewPage extends ObjectViewPage<Organisation> {

	private final Organisation organisation;
	private final int time;
	private final EchSchema echSchema;
	private final OrganisationPanel organisationPanel;
	private final OrganisationMenu menu;

	public OrganisationViewPage(PageContext pageContext, String[] arguments) {
		super(pageContext);
		this.echSchema = EchSchema.getNamespaceContext(148, arguments[0]);
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : 0;
		this.organisation = loadObject(arguments[1], time);
		this.organisationPanel = new OrganisationPanel(Organisation.EditMode.DISPLAY, echSchema);
		this.menu = time == 0 ? new OrganisationMenu(echSchema, organisation) : null;  
	}

	@Override
	public ActionGroup getMenu() {
		if (menu != null) {
			return menu.getActions();
		} else {
			return null;
		}
	}

	private static Organisation loadObject(String organisationId, int time) {
		Organisation actualOrganisation = EchServer.getInstance().getPersistence().organisationLocalIdIndex().find(organisationId);
		if (time == 0) {
			return actualOrganisation;
		} else {
			int id = EchServer.getInstance().getPersistence().organisation().getId(actualOrganisation);
			return EchServer.getInstance().getPersistence().organisation.read(id, time);
		}
	}

	@Override
	public IForm<Organisation> createForm() {
		return organisationPanel;
	}
	
	@Override
	protected Organisation getObject() {
		return organisation;
	}

	private static final int MAX_NAME_LENGTH = 20;
	
	@Override
	public String getTitle() {
		String title;
		if (organisation.identification.organisationName != null) {
			if (organisation.identification.organisationName.length() <= MAX_NAME_LENGTH) {
				title = organisation.identification.organisationName;
			} else {
				title = organisation.identification.organisationName.substring(0, MAX_NAME_LENGTH-1) + "..";
			}
		} else {
			title = Resources.getString("Organisation");
		}
		return title;
	}

}
