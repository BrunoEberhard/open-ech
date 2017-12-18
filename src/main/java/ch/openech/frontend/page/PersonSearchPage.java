package ch.openech.frontend.page;

import static ch.openech.model.person.PersonSearch.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.SearchPage;
import org.minimalj.repository.query.By;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0020;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonSearch;
import ch.openech.transaction.EchRepository;
import ch.openech.xml.write.EchSchema;


public class PersonSearchPage extends SearchPage<PersonSearch> {

	public static final Object[] FIELDS = {
		$.firstName, //
		$.officialName, //
		$.dateOfBirth, //
		$.streetAndHouseNumber,
		$.town, //
		$.vn.getFormattedValue(), //
	};
	
	public PersonSearchPage(String query) {
		super(query, FIELDS);
	}

	@Override
	protected List<PersonSearch> load(String query) {
		return Backend.find(PersonSearch.class, By.search(query));
	}

	private Person load(PersonSearch personSearch) {
		return Backend.read(Person.class, personSearch.id);
	}

	@Override
	public PersonPage createDetailPage(PersonSearch personSearch) {
		Person person = load(personSearch); 
		OpenEchPreferences preferences = EchRepository.getPreferences();
		EchSchema0020 schema0020 = preferences.applicationSchemaData.schema20;
		String version = schema0020.getVersion() + "." + schema0020.getMinorVersion();
		EchSchema schema = EchSchema.getNamespaceContext(20, version);

		return new PersonPage(schema, person);
	}

}
