package ch.openech.client.ewk;

import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.application.ObjectViewPage;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class PersonViewPage extends ObjectViewPage<Person> {

	private final String personId;
	private final EchNamespaceContext echNamespaceContext;
	private final PersonPanel personPanel;
	private final PersonEditMenu menu;

	public PersonViewPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public PersonViewPage(PageContext context, String version, String personId) {
		super(context);
		this.personId = personId;
		this.echNamespaceContext = EchNamespaceContext.getNamespaceContext(20, version);
		this.personPanel = new PersonPanel(PersonPanelType.DISPLAY, echNamespaceContext);
		this.menu = new PersonEditMenu(echNamespaceContext); 
	}

	@Override
	protected void showObject(Person person) {
		super.showObject(person);
		menu.setPerson(person, true);
		setTitle(pageTitle(person));
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		menu.fillActionGroup(pageContext, actionGroup);
	}

	@Override
	protected Person loadObject() {
		return EchServer.getInstance().getPersistence().person().getByLocalPersonId(personId);
	}

	@Override
	public FormVisual<Person> createForm() {
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
