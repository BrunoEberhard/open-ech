package ch.openech.frontend.page;

import static ch.openech.model.person.PersonSearch.*;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.AbstractSearchPage;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0020;
import ch.openech.model.person.PersonSearch;
import ch.openech.xml.write.EchSchema;


public class PersonSearchPage extends AbstractSearchPage<PersonSearch> {

	public static final Object[] FIELD_NAMES = {
		$.firstName, //
		$.officialName, //
		$.dateOfBirth, //
		$.getStreet(), // PERSON::getStreet()S
		$.getStreetNumber(), //
		$.getTown(), //
		$.vn.getFormattedValue(), //
	};
	
	public PersonSearchPage() {
		super(FIELD_NAMES);
	}

	@Override
	public void action(PersonSearch person, List<PersonSearch> selectedObjects) {
		OpenEchPreferences preferences = (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences();
		EchSchema0020 schema0020 = preferences.applicationSchemaData.schema20;
		String version = schema0020.getVersion() + "." + schema0020.getMinorVersion();
		EchSchema schema = EchSchema.getNamespaceContext(20, version);
		
		List<Page> pages = new ArrayList<>(selectedObjects.size());
		int index = 0;
		int count = 0;
		for (PersonSearch p : selectedObjects) {
			Page page = new PersonPage(schema, p.id);
			pages.add(page);
			if (p == person) {
				index = count;
			}
			count++;
		}
		ClientToolkit.getToolkit().show(pages, index);
	}

	@Override
	protected List<PersonSearch> load(String query) {
		return Backend.getInstance().read(PersonSearch.class, Criteria.search(query), 100);
	}
}
