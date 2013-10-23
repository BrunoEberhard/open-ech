package ch.openech.client.page;

import static ch.openech.dm.person.Person.*;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchSchema0020;
import ch.openech.dm.person.Person;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.TablePage;
import ch.openech.mj.search.FulltextIndexSearch;
import ch.openech.mj.search.Item;
import ch.openech.mj.search.Search;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;


public class SearchPersonPage extends TablePage<Person> {

	private final EchSchema echSchema;
	private final String text;

	public static final Object[] FIELD_NAMES = {
		PERSON.personIdentification.firstName, //
		PERSON.personIdentification.officialName, //
		PERSON.personIdentification.dateOfBirth, //
		PERSON.getStreet(), //
		PERSON.getStreetNumber(), //
		PERSON.getTown(), //
		PERSON.personIdentification.vn.value, //
	};
	
	private static Search<Person> search = new FulltextIndexSearch<>(Person.class, EchServer.getInstance().getPersistence().personIndex(), FIELD_NAMES);
	
	public SearchPersonPage(PageContext context, String text) {
		this(context, getVersionFromPreference(context), text);
	}

	private static String getVersionFromPreference(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		EchSchema0020 schema = preferences.applicationSchemaData.schema20;
		return schema.getVersion() + "." + schema.getMinorVersion();
	}
	
	public SearchPersonPage(PageContext context, String version, String text) {
		super(context, search, text);
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
	protected void clicked(Item item, List<Item> items) {
		List<String> pageLinks = new ArrayList<String>(getItems().size());
		for (Item i : getItems()) {
			String link = link(PersonViewPage.class, echSchema.getVersion(), (String)i.getValue(PERSON.personIdentification.technicalIds.localId.personId));
			pageLinks.add(link);
		}
		int index = getItems().indexOf(item);
		getPageContext().show(pageLinks, index);
	}
	
}
