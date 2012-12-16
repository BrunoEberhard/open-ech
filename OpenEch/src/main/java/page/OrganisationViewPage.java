package page;

import java.sql.SQLException;

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

	private final String organisationId;
	private final int time;
	private final EchSchema echSchema;
	private final OrganisationPanel organisationPanel;
	private final OrganisationMenu menu;

	public OrganisationViewPage(PageContext context, String[] arguments) {
		super(context);
		this.echSchema = EchSchema.getNamespaceContext(148, arguments[0]);
		this.organisationId = arguments[1];
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : 0;
		this.organisationPanel = new OrganisationPanel(Organisation.EditMode.DISPLAY, echSchema);
		this.menu = time == 0 ? new OrganisationMenu(context, echSchema) : null;  
	}

	@Override
	protected void showObject(Organisation organisation) {
		super.showObject(organisation);
		if (menu != null) {
			menu.setOrganisation(organisation, true);
		}
		setTitle(pageTitle(organisation));
	}
	
	@Override
	public void fillActionGroup(ActionGroup actionGroup) {
		if (menu != null) {
			menu.fillActionGroup(actionGroup);
		}
	}

	@Override
	protected Organisation loadObject() {
		Organisation actualOrganisation = EchServer.getInstance().getPersistence().organisation().getByLocalOrganisationId(organisationId);
		if (time == 0) {
			return actualOrganisation;
		} else {
			try {
				return EchServer.getInstance().getPersistence().organisation().read(actualOrganisation, time);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public IForm<Organisation> createForm() {
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
