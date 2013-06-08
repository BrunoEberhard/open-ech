package ch.openech.client.page;

import static ch.openech.dm.person.Person.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchSchema0020;
import ch.openech.dm.person.Person;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ITable;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;


public class SearchPersonPage extends Page implements RefreshablePage {

	private final EchSchema echSchema;
	private String text;
	private ITable<Person> table;
	private List<Person> resultList;

	public static final Object[] FIELD_NAMES = {
		PERSON.personIdentification.firstName, //
		PERSON.personIdentification.officialName, //
		PERSON.personIdentification.dateOfBirth, //
		PERSON.getStreet(), //
		PERSON.getStreetNumber(), //
		PERSON.getTown(), //
		PERSON.personIdentification.vn.value, //
	};
	
	public SearchPersonPage(PageContext context, String text) {
		this(context, getVersionFromPreference(context), text);
	}

	private static String getVersionFromPreference(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		EchSchema0020 schema = preferences.applicationSchemaData.schema20;
		return schema.getVersion() + "." + schema.getMinorVersion();
	}
	
	public SearchPersonPage(PageContext context, String version, String text) {
		super(context);
		this.echSchema = EchSchema.getNamespaceContext(20, version);
		this.text = text;
		table = ClientToolkit.getToolkit().createTable(Person.class, FIELD_NAMES);
		table.setClickListener(new PersonTableClickListener());
		refresh();
	}

	@Override
	public IComponent getComponent() {
		return table;
	}

	private class PersonTableClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> pageLinks = new ArrayList<String>(resultList.size());
			for (Person person : resultList) {
				String link = link(PersonViewPage.class, echSchema.getVersion(), person.getId());
				pageLinks.add(link);
			}
			int index = resultList.indexOf(table.getSelectedObject());
			getPageContext().show(pageLinks, index);
		}
	}
	
	@Override
	public void refresh() {
		resultList = EchServer.getInstance().getPersistence().personIndex().find(text);
		table.setObjects(resultList);
	}
	
}
