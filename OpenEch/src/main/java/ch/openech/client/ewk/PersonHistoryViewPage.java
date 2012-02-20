package ch.openech.client.ewk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.dm.person.Person;
import ch.openech.mj.application.HistoryViewPage;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.util.DateUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class PersonHistoryViewPage extends HistoryViewPage<Person> implements RefreshablePage {

	private final EchNamespaceContext echNamespaceContext;
	private final String personId;
	private PersonEditMenu menu;

	public PersonHistoryViewPage(PageContext context, String version, String personId) {
		this(context, EchNamespaceContext.getNamespaceContext(20, version), personId);
	}
	
	public PersonHistoryViewPage(PageContext context, EchNamespaceContext echNamespaceContext, String personId) {
		super(context);
		this.personId = personId;
		this.echNamespaceContext = echNamespaceContext;
		this.menu = new PersonEditMenu(echNamespaceContext);
	}

	@Override
	protected void showObject(Person person, int row) {
		super.showObject(person, row);
		// TODO actual!
		menu.setPerson(person, row == 0);
	}

	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		super.fillActionGroup(pageContext, actionGroup);
		menu.fillActionGroup(pageContext, actionGroup);
	}
	
	@Override
	protected List<Person> loadObjects() {
		List<Person> persons = new ArrayList<Person>();
		try {
			Person person = EchServer.getInstance().getPersistence().person().getByLocalPersonId(personId);
			int id = EchServer.getInstance().getPersistence().person().getId(person);
			List<Integer> versions = EchServer.getInstance().getPersistence().person().readVersions(id);
			Collections.reverse(versions);
			persons.add(EchServer.getInstance().getPersistence().person().read(id, null));
			for (int version : versions) {
				persons.add(EchServer.getInstance().getPersistence().person().read(id, version));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return persons;
	}

	@Override
	protected String getTime(Person object) {
		return object.event != null ? DateUtils.formatCH(object.event.time) : "-";
	}

	@Override
	protected String getDescription(Person object) {
		return object.event != null ? object.event.type : "-";
	}

	@Override
	public FormVisual<Person> createForm() {
		return new PersonPanel(PersonPanelType.DISPLAY, echNamespaceContext);
	}
	
}