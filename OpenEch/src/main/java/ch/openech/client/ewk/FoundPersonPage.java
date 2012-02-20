package ch.openech.client.ewk;

import static ch.openech.dm.person.Person.PERSON;

import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualTable;
import ch.openech.mj.toolkit.VisualTable.ClickListener;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;


public class FoundPersonPage extends Page implements RefreshablePage {

	private final EchNamespaceContext echNamespaceContext;
	private String text;
	private VisualTable<Person> table;

	public static final Object[] FIELD_NAMES = {
		Person.MR_MRS, //
		PERSON.personIdentification.firstName, //
		PERSON.personIdentification.officialName, //
		PERSON.personIdentification.dateOfBirth, //
		Person.STREET, //
		Person.STREET_NUMBER, //
		Person.TOWN, //
		PERSON.personIdentification.vn, //
	};
	
	// Damit wird die Version des Fenster overruled
	@Deprecated
	public FoundPersonPage(PageContext context, String text) {
		this(context,"2.2", text);
	}
	
	public FoundPersonPage(PageContext context, String version, String text) {
		super(context);
		this.echNamespaceContext = EchNamespaceContext.getNamespaceContext(20, version);
		this.text = text;
		table = ClientToolkit.getToolkit().createVisualTable(Person.class, FIELD_NAMES);
		table.setClickListener(new PersonTableClickListener());
		refresh();
	}

	@Override
	public IComponent getPanel() {
		return table;
	}

	private class PersonTableClickListener implements ClickListener {

		@Override
		public void clicked() {
			Person person = table.getSelectedObject();
			if (person != null) {
				show(PersonViewPage.class, echNamespaceContext.getVersion(), person.getId());
			}
		}
	}
	
	@Override
	public void refresh() {
		List<Person> resultList = EchServer.getInstance().getPersistence().person().find(text);
		table.setObjects(resultList);
	}
	
}
