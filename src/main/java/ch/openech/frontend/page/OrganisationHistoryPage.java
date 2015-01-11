package ch.openech.frontend.page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.HistoryPage;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.transaction.persistence.ReadHistoryTransaction;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationHistoryPage extends HistoryPage<Organisation> {

	private final EchSchema echNamespaceContext;
	private final long organisationId;

	public OrganisationHistoryPage(String[] arguments) {
		this(arguments[0], arguments[1]);
	}
	
	public OrganisationHistoryPage(String version, String organisationId) {
		this(EchSchema.getNamespaceContext(20, version), organisationId);
	}
	
	public OrganisationHistoryPage(EchSchema echNamespaceContext, String organisationId) {
		this.organisationId = Long.valueOf(organisationId);
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	public String getTitle() {
		return "Verlauf Firma";
	}

	@Override
	protected List<HistoryVersion<Organisation>> loadVersions() {
		Organisation organisation = Backend.getInstance().read(Organisation.class, organisationId);
		List<Organisation> organisations = Backend.getInstance().execute(new ReadHistoryTransaction<Organisation>(organisation));
//		Collections.sort(times);
//		Collections.reverse(times);

		List<HistoryVersion<Organisation>> versions = new ArrayList<>();
		HistoryVersion<Organisation> version = new HistoryVersion<Organisation>();
		version.object = organisation;
		version.time = getTime(organisation);
		version.description = getDescription(organisation);
		versions.add(version);
		for (Organisation p : organisations) {
			version = new HistoryVersion<Organisation>();
			version.object = p;
			version.version = "" + p.version;
			version.time = getTime(p);
			version.description = getDescription(p);
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
			String resourceName = StringUtils.upperFirstChar(object.event.type) + "Event";
			return Resources.getString(resourceName);
		} else {
			return "-";
		}
	}


	@Override
	protected String link(Organisation object, String version) {
		if (version != null) {
			return PageLink.link(OrganisationPage.class, echNamespaceContext.getVersion(), object.getId(), version);
		} else {
			return PageLink.link(OrganisationPage.class, echNamespaceContext.getVersion(), object.getId());
		}
	}
	
}
