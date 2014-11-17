package ch.openech.frontend.page;

import static ch.openech.model.person.PersonSearch.*;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.IdUtils;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0020;
import ch.openech.model.person.PersonSearch;
import ch.openech.xml.write.EchSchema;


public class PersonSearchPage extends TablePage<PersonSearch> {

	private final EchSchema echSchema;
	private final String text;

	public static final Object[] FIELD_NAMES = {
		$.firstName, //
		$.officialName, //
		$.dateOfBirth, //
		$.getStreet(), // PERSON::getStreet()S
		$.getStreetNumber(), //
		$.getTown(), //
		$.vn.getFormattedValue(), //
	};
	
	public PersonSearchPage(String text) {
		this(getVersionFromPreference(), text);
	}

	private static String getVersionFromPreference() {
		OpenEchPreferences preferences = (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences();
		EchSchema0020 schema = preferences.applicationSchemaData.schema20;
		return schema.getVersion() + "." + schema.getMinorVersion();
	}
	
	public PersonSearchPage(String version, String text) {
		super(FIELD_NAMES, text);
		this.echSchema = EchSchema.getNamespaceContext(20, version);
		this.text = text;
	}

	@Override
	public String getTitle() {
		return "Suche Personen mit " + text;
	}

	@Override
	protected void clicked(PersonSearch person, List<PersonSearch> selectedObjects) {
		List<String> pageLinks = new ArrayList<String>(selectedObjects.size());
		int index = 0;
		int count = 0;
		for (PersonSearch p : selectedObjects) {
			String link = link(PersonPage.class, echSchema.getVersion(), IdUtils.getIdString(p));
			pageLinks.add(link);
			if (p == person) {
				index = count;
			}
			count++;
		}
		ClientToolkit.getToolkit().show(pageLinks, index);
	}

	@Override
	protected List<PersonSearch> load(String searchText) {
		return Backend.getInstance().read(PersonSearch.class, Criteria.search(searchText), 100);
	}

}
