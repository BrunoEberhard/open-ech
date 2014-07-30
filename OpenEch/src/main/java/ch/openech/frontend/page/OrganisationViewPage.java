package ch.openech.frontend.page;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.org.OrganisationMenu;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationViewPage extends ObjectPage<Organisation> {

	private final Organisation organisation;
	private final int time;
	private final EchSchema echSchema;
	private final OrganisationPanel organisationPanel;
	private final OrganisationMenu menu;

	public OrganisationViewPage(String[] arguments) {
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
		long id = Long.valueOf(organisationId);
		if (time == 0) {
			return Backend.getInstance().read(Organisation.class, id);
		} else {
			return Backend.getInstance().read(Organisation.class, id, time);
		}
	}

	@Override
	public Form<Organisation> createForm() {
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
