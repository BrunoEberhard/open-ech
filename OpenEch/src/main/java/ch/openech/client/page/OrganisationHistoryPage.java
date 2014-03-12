package ch.openech.client.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.HistoryPage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;

public class OrganisationHistoryPage extends HistoryPage<Organisation> implements RefreshablePage {

	private final EchSchema echNamespaceContext;
	private final long organisationId;

	public OrganisationHistoryPage(PageContext context, String[] arguments) {
		this(context, arguments[0], arguments[1]);
	}
	
	public OrganisationHistoryPage(PageContext context, String version, String organisationId) {
		this(context, EchSchema.getNamespaceContext(20, version), organisationId);
	}
	
	public OrganisationHistoryPage(PageContext context, EchSchema echNamespaceContext, String organisationId) {
		super(context);
		this.organisationId = Long.valueOf(organisationId);
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	public String getTitle() {
		return "Verlauf Firma";
	}

	@Override
	protected List<HistoryVersion<Organisation>> loadVersions() {
		Organisation organisation = Services.get(DbService.class).read(Organisation.class, organisationId);
		Map<Integer, Organisation> organisations = Services.get(DbService.class).loadHistory(organisation);
		List<Integer> times = new ArrayList<>(organisations.keySet());
		Collections.sort(times);
		Collections.reverse(times);

		List<HistoryVersion<Organisation>> versions = new ArrayList<>();
		HistoryVersion<Organisation> version = new HistoryVersion<Organisation>();
		version.object = organisation;
		version.time = getTime(organisation);
		version.description = getDescription(organisation);
		versions.add(version);
		for (int time : times) {
			organisation = organisations.get(times);
			version = new HistoryVersion<Organisation>();
			version.object = organisation;
			version.version = "" + time;
			version.time = getTime(organisation);
			version.description = getDescription(organisation);
			versions.add(version);
		}

		return versions;
	}

	@Override
	protected LocalDateTime getTime(Organisation object) {
		return object.event.time;
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
