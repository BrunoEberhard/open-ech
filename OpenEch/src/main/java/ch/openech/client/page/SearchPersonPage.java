package ch.openech.client.page;

import static ch.openech.dm.person.Person.*;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchSchema0020;
import ch.openech.dm.person.Person;
import ch.openech.mj.backend.Backend;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.TablePage;
import ch.openech.mj.util.IdUtils;
import ch.openech.xml.write.EchSchema;


public class SearchPersonPage extends TablePage<Person> {

	private final EchSchema echSchema;
	private final String text;

	public static final Object[] FIELD_NAMES = {
		PERSON.firstName, //
		PERSON.officialName, //
		PERSON.dateOfBirth, //
		PERSON.getStreet(), // PERSON::getStreet()
		PERSON.getStreetNumber(), //
		PERSON.getTown(), //
		PERSON.vn.value, //
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
		super(context, FIELD_NAMES, text);
		this.echSchema = EchSchema.getNamespaceContext(20, version);
		this.text = text;
	}

	@Override
	public String getTitle() {
		return "Suche Personen mit " + text;
	}

	@Override
	public ActionGroup getMenu() {
		return null;
	}

	@Override
	protected void clicked(Person person, List<Person> selectedObjects) {
		List<String> pageLinks = new ArrayList<String>(selectedObjects.size());
		int index = 0;
		int count = 0;
		for (Person p : selectedObjects) {
			String link = link(PersonViewPage.class, echSchema.getVersion(), IdUtils.getIdString(p));
			pageLinks.add(link);
			if (p == person) {
				index = count;
			}
			count++;
		}
		getPageContext().show(pageLinks, index);
	}

	@Override
	protected List<Person> load(String query) {
		return Backend.getInstance().search(Person.class, query, 100);
	}

}
