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
	private final int time;
	private OrganisationMenu menu;

	public OrganisationPage(EchSchema echSchema, Organisation organisation) {
		this(echSchema, organisation, 0);
	}
	
	public OrganisationPage(EchSchema echSchema, Organisation organisation, int time) {
		super(organisation);
		this.echSchema = echSchema;
		this.time = time;
	}

	public OrganisationPage(EchSchema echSchema, Object organisationId) {
		super(Organisation.class, organisationId);
		this.echSchema = echSchema;
		this.time = 0;
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
	public Organisation load() {
		Organisation organisation;
		if (time == 0) {
			organisation = Backend.getInstance().read(Organisation.class, getObjectId());
		} else {
			organisation = Backend.getInstance().execute(new ReadTransaction<Organisation>(Organisation.class, getObject().id, time));
		}
		return organisation;
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
