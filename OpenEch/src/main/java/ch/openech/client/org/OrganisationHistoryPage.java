package ch.openech.client.org;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.HistoryPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class OrganisationHistoryPage extends HistoryPage<Organisation> implements RefreshablePage {

	private final EchNamespaceContext echNamespaceContext;
	private final String organisationId;

	public OrganisationHistoryPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public OrganisationHistoryPage(PageContext context, String version, String organisationId) {
		this(context, EchNamespaceContext.getNamespaceContext(20, version), organisationId);
	}
	
	public OrganisationHistoryPage(PageContext context, EchNamespaceContext echNamespaceContext, String organisationId) {
		super(context);
		this.organisationId = organisationId;
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	protected List<HistoryVersion> loadVersions() {
		List<HistoryVersion> versions = new ArrayList<HistoryVersion>();
		try {
			Organisation organisation = EchServer.getInstance().getPersistence().organisation().getByLocalOrganisationId(organisationId);
			int id = EchServer.getInstance().getPersistence().organisation().getId(organisation);
			List<Integer> times = EchServer.getInstance().getPersistence().organisation().readVersions(id);
			Collections.reverse(times);
			HistoryVersion version = new HistoryVersion();
			version.object = organisation;
			version.time = getTime(organisation);
			version.description = getDescription(organisation);
			versions.add(version);
			for (int time : times) {
				organisation = EchServer.getInstance().getPersistence().organisation().read(id, time);
				version = new HistoryVersion();
				version.object = organisation;
				version.version = "" + time;
				version.time = getTime(organisation);
				version.description = getDescription(organisation);
				versions.add(version);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return versions;
	}

	@Override
	protected String getTime(Organisation object) {
		return object.event != null ? object.event.time : "-";
	}

	@Override
	protected String getDescription(Organisation object) {
		if (object.event != null) {
			String resourceName = StringUtils.upperFirstChar(object.event.type) + "Event.text";
			return Resources.getString(resourceName);
		} else {
			return "-";
		}
	}


	@Override
	protected String link(Organisation object, String version) {
		if (version != null) {
			return link(OrganisationViewPage.class, echNamespaceContext.getVersion(), object.getId(), version);
		} else {
			return link(OrganisationViewPage.class, echNamespaceContext.getVersion(), object.getId());
		}
	}
	
}