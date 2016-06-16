package ch.openech.frontend.page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.HistoryPage;
import org.minimalj.frontend.page.Page;
import org.minimalj.transaction.persistence.ReadHistoryTransaction;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;

public class PersonHistoryPage extends HistoryPage<Person> {

	private final EchSchema echSchema;
	private final PersonPage personPage;

	public PersonHistoryPage(PersonPage personPage) {
		this.echSchema = personPage.getEchSchema();
		this.personPage = personPage;
	}

	@Override
	public String getTitle() {
		return "Verlauf Person";
	}

	@Override
	protected List<HistoryVersion<Person>> loadVersions() {
		Person person = personPage.load();
		
		List<Person> persons = Backend.execute(new ReadHistoryTransaction<Person>(person));
//		Collections.sort(times);
//		Collections.reverse(times);

		List<HistoryVersion<Person>> versions = new ArrayList<>();
		HistoryVersion<Person> version = new HistoryVersion<Person>();
		version.object = person;
		version.time = getTime(person);
		version.description = getDescription(person);
		versions.add(version);
		for (Person p : persons) {
			version = new HistoryVersion<Person>();
			version.object = p;
			version.version = "" + p.version;
			version.time = getTime(p);
			version.description = getDescription(p);
			versions.add(version);
		}

		return versions;
	}

	@Override
	protected LocalDateTime getTime(Person object) {
		return object.event.time;
	}

	@Override
	protected String getDescription(Person object) {
		if (object.event != null) {
			String resourceName = StringUtils.upperFirstChar(object.event.type) + "Event";
			return Resources.getString(resourceName);
		} else {
			return "-";
		}
	}

	@Override
	protected Page click(Person object, String version) {
		return new PersonPage(echSchema, object);
	}
	
}
