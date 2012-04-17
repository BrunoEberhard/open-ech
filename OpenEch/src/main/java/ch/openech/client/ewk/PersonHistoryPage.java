package ch.openech.client.ewk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.application.HistoryPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.util.DateUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class PersonHistoryPage extends HistoryPage<Person> implements RefreshablePage {

	private final EchNamespaceContext echNamespaceContext;
	private final String personId;

	public PersonHistoryPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public PersonHistoryPage(PageContext context, String version, String personId) {
		this(context, EchNamespaceContext.getNamespaceContext(20, version), personId);
	}
	
	public PersonHistoryPage(PageContext context, EchNamespaceContext echNamespaceContext, String personId) {
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
				version.version = time;
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
		return object.event != null ? DateUtils.formatCH(object.event.time) : "-";
	}

	@Override
	protected String getDescription(Person object) {
		return object.event != null ? object.event.type : "-";
	}

	@Override
	protected String link(Person object, int version) {
		return link(PersonViewPage.class, echNamespaceContext.getVersion(), object.getId(), "" + version);
	}
	
}