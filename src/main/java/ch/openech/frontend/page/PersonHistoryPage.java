package ch.openech.frontend.page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.backend.persistence.ReadHistoryTransaction;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;

public class PersonHistoryPage extends EchEventTablePage<Person, PersonPage> {

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
	protected List<EchEvent<Person>> load() {
		Person person = personPage.load();
		
		List<Person> persons = Backend.execute(new ReadHistoryTransaction<Person>(person));
//		Collections.sort(times);
//		Collections.reverse(times);

		List<EchEvent<Person>> versions = new ArrayList<>();
		for (Person p : persons) {
			EchEvent<Person> version = new EchEvent<>();
			version.object = p;
			version.version = "" + p.version;
			version.time = getTime(p);
			version.description = getDescription(p);
			versions.add(version);
		}

		return versions;
	}

	protected LocalDateTime getTime(Person object) {
		return object.event.time;
	}

	protected String getDescription(Person object) {
		if (object.event != null) {
			String resourceName = StringUtils.upperFirstChar(object.event.type) + "Event";
			return Resources.getString(resourceName);
		} else {
			return "-";
		}
	}

	@Override
	protected PersonPage createDetailPage(ch.openech.frontend.page.EchEventTablePage.EchEvent<Person> echEvent) {
		return new PersonPage(echSchema, echEvent.object);
	}
	
	@Override
	protected PersonPage updateDetailPage(PersonPage page, ch.openech.frontend.page.EchEventTablePage.EchEvent<Person> echEvent) {
		page.setObject(echEvent.object);
		return page;
	}

	
}
