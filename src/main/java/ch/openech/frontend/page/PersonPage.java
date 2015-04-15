package ch.openech.frontend.page;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.transaction.persistence.ReadTransaction;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.ewk.PersonEditMenu;
import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;

public class PersonPage extends ObjectPage<Person> {

	private final EchSchema echSchema;
	private final int time;
	private PersonEditMenu menu;

	public PersonPage(EchSchema echSchema, Person person) {
		this(echSchema, person, 0);
	}
	
	public PersonPage(EchSchema echSchema, Person person, int time) {
		super(person);
		this.echSchema = echSchema;
		this.time = time;
	}

	public PersonPage(EchSchema echSchema, Object personId) {
		super(Person.class, personId);
		this.echSchema = echSchema;
		this.time = 0;
	}

	@Override
	public ActionGroup getMenu() {
		if (time == 0 && menu == null) {
			menu = new PersonEditMenu(echSchema, getObject());
		}
		if (menu != null) {
			menu.update(getObject(), true);
			return menu.getActions();
		} else {
			return null;
		}
	}
	
	@Override
	public Person load() {
		Person person;
		if (time == 0) {
			person = Backend.getInstance().read(Person.class, getObjectId());
		} else {
			person = Backend.getInstance().execute(new ReadTransaction<Person>(Person.class, getObject().id, time));
		}
		return person;
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.DISPLAY, echSchema);
	}
	
	private static final int MAX_NAME_LENGTH = 10;
	
	@Override
	public String getTitle() {
		Person person = getObject();
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
