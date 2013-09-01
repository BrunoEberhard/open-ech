package ch.openech.client.page;

import ch.openech.client.ewk.PersonEditMenu;
import ch.openech.client.ewk.PersonPanel;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.ObjectViewPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.server.EchServer;
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
		Person actualPerson = EchServer.getInstance().getPersistence().personLocalPersonIdIndex().find(personId);
		if (time == 0) {
			return actualPerson;
		} else {
			int id = EchServer.getInstance().getPersistence().person().getId(actualPerson);
			return EchServer.getInstance().getPersistence().person().read(id, time);
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
		PersonIdentification identification = person.personIdentification;
		String title;
		if (identification.officialName != null) {
			if (identification.officialName.length() <= MAX_NAME_LENGTH) {
				title = identification.officialName;
			} else {
				title = identification.officialName.substring(0, MAX_NAME_LENGTH-1) + "..";
			}
			if (identification.firstName != null) {
				title = title + ", ";
				if (identification.firstName.length() <= MAX_NAME_LENGTH) {
					title = title + identification.firstName;
				} else {
					title = title + identification.firstName.substring(0, MAX_NAME_LENGTH-1) + "..";
				}
			}
		} else {
			title = Resources.getString("Person");
		}
		return title;
	}

}
