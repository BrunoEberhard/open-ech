package ch.openech.frontend.page;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.HistoryPage;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;
import java.time.LocalDateTime;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;

public class PersonHistoryPage extends HistoryPage<Person> {

	private final EchSchema echNamespaceContext;
	private final String personId;

	public PersonHistoryPage(String[] arguments) {
		this(arguments[0], arguments[1]);
	}
	
	public PersonHistoryPage(String version, String personId) {
		this(EchSchema.getNamespaceContext(20, version), personId);
	}
	
	public PersonHistoryPage(EchSchema echNamespaceContext, String personId) {
		this.personId = personId;
		this.echNamespaceContext = echNamespaceContext;
	}
	
	@Override
	public String getTitle() {
		return "Verlauf Person";
	}

	@Override
	protected List<HistoryVersion<Person>> loadVersions() {
		Person person = Backend.getInstance().read(Person.class, personId);
		List<Person> persons = Backend.getInstance().loadHistory(person);
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
	protected String link(Person object, String version) {
		if (version != null) {
			return link(PersonPage.class, echNamespaceContext.getVersion(), object.getId(), version);
		} else {
			return link(PersonPage.class, echNamespaceContext.getVersion(), object.getId());
		}
	}
	
}
