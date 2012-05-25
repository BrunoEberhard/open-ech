package page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import ch.openech.dm.person.Person;
import ch.openech.mj.page.HistoryPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;

public class PersonHistoryPage extends HistoryPage<Person> implements RefreshablePage {

	private final EchSchema echNamespaceContext;
	private final String personId;

	public PersonHistoryPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public PersonHistoryPage(PageContext context, String version, String personId) {
		this(context, EchSchema.getNamespaceContext(20, version), personId);
	}
	
	public PersonHistoryPage(PageContext context, EchSchema echNamespaceContext, String personId) {
		super(context);
		this.personId = personId;
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	protected List<HistoryVersion> loadVersions() {
		List<HistoryVersion> versions = new ArrayList<HistoryVersion>();
		try {
			Person person = EchServer.getInstance().getPersistence().person().getByLocalPersonId(personId);
			int id = EchServer.getInstance().getPersistence().person().getId(person);
			List<Integer> times = EchServer.getInstance().getPersistence().person().readVersions(id);
			Collections.reverse(times);
			HistoryVersion version = new HistoryVersion();
			version.object = person;
			version.time = getTime(person);
			version.description = getDescription(person);
			versions.add(version);
			for (int time : times) {
				person = EchServer.getInstance().getPersistence().person().read(id, time);
				version = new HistoryVersion();
				version.object = person;
				version.version = "" + time;
				version.time = getTime(person);
				version.description = getDescription(person);
				versions.add(version);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return versions;
	}

	@Override
	protected String getTime(Person object) {
		return object.event != null ? object.event.time : "-";
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