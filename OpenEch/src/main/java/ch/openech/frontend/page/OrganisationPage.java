package ch.openech.frontend.page;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.transaction.persistence.ReadTransaction;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.org.OrganisationMenu;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationPage extends ObjectPage<Organisation> {

	private final EchSchema echSchema;
	private final String organisationId;
	private final int time;
	private OrganisationMenu menu;

	public OrganisationPage(String[] arguments) {
		this.echSchema = EchSchema.getNamespaceContext(148, arguments[0]);
		this.organisationId = arguments[1];
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : 0;
	}

	@Override
	public ActionGroup getMenu() {
		if (time == 0 && menu == null) {
			menu = new OrganisationMenu(echSchema, getObject());
		}
		if (menu != null) {
			return menu.getActions();
		} else {
			return null;
		}
	}
	
	@Override
	public void refresh() {
		super.refresh();
		if (menu != null) {
			menu.update(getObject(), true);
		}
	}

	@Override
	protected Organisation loadObject() {
		return loadObject(organisationId, time);
	}

	private static Organisation loadObject(String organisationId, int time) {
		if (time == 0) {
			return Backend.getInstance().read(Organisation.class, organisationId);
		} else {
			return Backend.getInstance().execute(new ReadTransaction<Organisation>(Organisation.class, organisationId, time));
		}
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.DISPLAY, echSchema);
	}
	
	private static final int MAX_NAME_LENGTH = 20;
	
	@Override
	public String getTitle() {
		Organisation organisation = getObject();
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
