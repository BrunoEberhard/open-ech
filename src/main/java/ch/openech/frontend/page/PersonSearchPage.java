package ch.openech.frontend.page;

import static ch.openech.model.person.PersonSearch.*;

import java.util.List;

import org.minimalj.application.Preferences;
import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.frontend.page.SearchPage;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0020;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonSearch;
import ch.openech.xml.write.EchSchema;


public class PersonSearchPage extends SearchPage<PersonSearch, Person> {

	public static final Object[] FIELDS = {
		$.firstName, //
		$.officialName, //
		$.dateOfBirth, //
		$.getStreet(), // PERSON::getStreet()S
		$.getStreetNumber(), //
		$.getTown(), //
		$.vn.getFormattedValue(), //
	};
	
	public PersonSearchPage(String query) {
		super(query, FIELDS);
	}

	@Override
	protected List<PersonSearch> load(String query) {
		return Backend.persistence().read(PersonSearch.class, Criteria.search(query), 100);
	}

	@Override
	protected Person load(PersonSearch personSearch) {
		return Backend.persistence().read(Person.class, personSearch.id);
	}

	@Override
	public ObjectPage<Person> createDetailPage(Person person) {
		OpenEchPreferences preferences = Preferences.getPreferences(OpenEchPreferences.class);
		EchSchema0020 schema0020 = preferences.applicationSchemaData.schema20;
		String version = schema0020.getVersion() + "." + schema0020.getMinorVersion();
		EchSchema schema = EchSchema.getNamespaceContext(20, version);

		return new PersonPage(schema, person);
	}
}
