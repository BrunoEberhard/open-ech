package ch.openech.frontend.page;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.ewk.PersonEditMenu;
import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;

public class PersonPage extends ObjectPage<Person> {

	private final EchSchema echSchema;
	private final String personId;
	private final int time;
	private PersonEditMenu menu;

	public PersonPage(String[] arguments) {
		this.echSchema = EchSchema.getNamespaceContext(20, arguments[0]);
		this.personId = arguments[1];
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : 0;
	}
	
	@Override
	public String getTitle() {
		return pageTitle(getObject());
	}

	@Override
	public ActionGroup getMenu() {
		if (time == 0 && menu == null) {
			menu = new PersonEditMenu(echSchema, getObject());
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
	protected Person loadObject() {
		return loadObject(personId, time);
	}

	private static Person loadObject(String personId, int time) {
		if (time == 0) {
			return Backend.getInstance().read(Person.class, personId);
		} else {
			return Backend.getInstance().read(Person.class, personId, time);
		}
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.DISPLAY, echSchema);
	}
	
	private static final int MAX_NAME_LENGTH = 10;
	
	private static String pageTitle(Person person) {
		String title;
		if (person.officialName != null) {
			if (person.officialName.length() <= MAX_NAME_LENGTH) {
				title = person.officialName;
			} else {
				title = person.officialName.substring(0, MAX_NAME_LENGTH-1) + "..";
			}
			if (person.firstName != null) {
				title = title + ", ";
				if (person.firstName.length() <= MAX_NAME_LENGTH) {
					title = title + person.firstName;
				} else {
					title = title + person.firstName.substring(0, MAX_NAME_LENGTH-1) + "..";
				}
			}
		} else {
			title = Resources.getString("Person");
		}
		return title;
	}

}
