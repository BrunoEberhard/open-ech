package ch.openech.frontend.page;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectViewPage;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.ewk.PersonEditMenu;
import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;

public class PersonViewPage extends ObjectViewPage<Person> {

	private final Person person;
	private final int time;
	private final EchSchema echSchema;
	private final PersonPanel personPanel;
	private final PersonEditMenu menu;

	public PersonViewPage(String[] arguments) {
		this.echSchema = EchSchema.getNamespaceContext(20, arguments[0]);
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : 0;
		this.person = loadObject(arguments[1], time);
		this.personPanel = new PersonPanel(PersonEditMode.DISPLAY, echSchema);
		this.menu = time == 0 ? new PersonEditMenu(echSchema, person) : null;  
	}
	
	@Override
	public String getTitle() {
		return pageTitle(person);
	}

	@Override
	public ActionGroup getMenu() {
		if (menu != null) {
			return menu.getActions();
		} else {
			return null;
		}
	}

	private static Person loadObject(String personId, int time) {
		long id = Long.valueOf(personId);
		if (time == 0) {
			return Backend.getInstance().read(Person.class, id);
		} else {
			return Backend.getInstance().read(Person.class, id, time);
		}
	}

	@Override
	public Form<Person> createForm() {
		return personPanel;
	}
	
	@Override
	protected Person getObject() {
		return person;
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
