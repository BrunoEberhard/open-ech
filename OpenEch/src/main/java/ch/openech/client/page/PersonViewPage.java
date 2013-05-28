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

	private final String personId;
	private final Integer time;
	private final EchSchema echSchema;
	private final PersonPanel personPanel;
	private final PersonEditMenu menu;

	public PersonViewPage(PageContext context, String[] arguments) {
		super(context);
		this.echSchema = EchSchema.getNamespaceContext(20, arguments[0]);
		this.personId = arguments[1];
		this.time = arguments.length > 2 ? Integer.parseInt(arguments[2]) : null;
		this.personPanel = new PersonPanel(PersonEditMode.DISPLAY, echSchema);
		this.menu = time == null ? new PersonEditMenu(context, echSchema) : null; 
	}

	@Override
	protected void showObject(Person person) {
		super.showObject(person);
		if (menu != null) {
			menu.setPerson(person, true);
		}
		setTitle(pageTitle(person));
	}
	
	@Override
	public void fillActionGroup(ActionGroup actionGroup) {
		if (menu != null) {
			menu.fillActionGroup(actionGroup);
		}
	}

	@Override
	protected Person loadObject() {
		Person actualPerson = EchServer.getInstance().getPersistence().person().getByLocalPersonId(personId);
		if (time == null) {
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
