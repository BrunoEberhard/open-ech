package ch.openech.frontend.page;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.HistoryPage;
import org.minimalj.frontend.page.PageContext;
import org.minimalj.frontend.page.RefreshablePage;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;

public class PersonHistoryPage extends HistoryPage<Person> implements RefreshablePage {

	private final EchSchema echNamespaceContext;
	private final long personId;

	public PersonHistoryPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public PersonHistoryPage(PageContext context, String version, String personId) {
		this(context, EchSchema.getNamespaceContext(20, version), personId);
	}
	
	public PersonHistoryPage(PageContext context, EchSchema echNamespaceContext, String personId) {
		super(context);
		this.personId = Long.valueOf(personId);
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
			String resourceName = StringUtils.upperFirstChar(object.event.type) + "Event.text";
			return Resources.getString(resourceName);
		} else {
			return "-";
		}
	}

	@Override
	protected String link(Person object, String version) {
		if (version != null) {
			return link(PersonViewPage.class, echNamespaceContext.getVersion(), object.getId(), version);
		} else {
			return link(PersonViewPage.class, echNamespaceContext.getVersion(), object.getId());
		}
	}
	
}
