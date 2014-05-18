package ch.openech.client.page;

import ch.openech.client.ewk.PersonEditMenu;
import ch.openech.client.ewk.PersonPanel;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.mj.backend.Backend;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.ObjectViewPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.xml.write.EchSchema;

public class PersonViewPage extends ObjectViewPage<Person> {

	private final Person person;
	private final int time;
	private final EchSchema echSchema;
	private final PersonPanel personPanel;
	private final PersonEditMenu menu;

	public PersonViewPage(PageContext pageContext, String[] arguments) {
		super(pageContext);
		this.echSchema = EchSchema.getNamespaceContext(20, arguments[0]);
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : 0;
		this.person = loadObject(arguments[1], time);
		this.personPanel = new PersonPanel(PersonEditMode.DISPLAY, echSchema);
		this.menu = time == 0 ? new PersonEditMenu(echSchema, pageContext, person) : null;  
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
	public IForm<Person> createForm() {
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
