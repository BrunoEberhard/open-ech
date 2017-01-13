package ch.openech.frontend.page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.backend.repository.ReadHistoryTransaction;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationHistoryPage extends EchEventTablePage<Organisation, OrganisationPage> {

	private final EchSchema echSchema;
	private final OrganisationPage organisationPage;

	public OrganisationHistoryPage(OrganisationPage organisationPage) {
		this.echSchema = organisationPage.getEchSchema();
		this.organisationPage = organisationPage;
	}

	@Override
	public String getTitle() {
		return "Verlauf Firma";
	}

	@Override
	protected List<EchEvent<Organisation>> load() {
		Organisation organisation = organisationPage.load();
		
		List<Organisation> organisations = Backend.execute(new ReadHistoryTransaction<Organisation>(organisation));
//		Collections.sort(times);
//		Collections.reverse(times);

		List<EchEvent<Organisation>> versions = new ArrayList<>();
		for (Organisation p : organisations) {
			EchEvent<Organisation> version = new EchEvent<>();
			version.object = p;
			version.version = "" + p.version;
			version.time = getTime(p);
			version.description = getDescription(p);
			versions.add(version);
		}

		return versions;
	}

	protected LocalDateTime getTime(Organisation object) {
		return object.event.time;
	}

	protected String getDescription(Organisation object) {
		if (object.event != null) {
			String resourceName = StringUtils.upperFirstChar(object.event.type) + "Event";
			return Resources.getString(resourceName);
		} else {
			return "-";
		}
	}

	@Override
	protected OrganisationPage createDetailPage(ch.openech.frontend.page.EchEventTablePage.EchEvent<Organisation> echEvent) {
		return new OrganisationPage(echSchema, echEvent.object);
	}
	
	@Override
	protected OrganisationPage updateDetailPage(OrganisationPage page, ch.openech.frontend.page.EchEventTablePage.EchEvent<Organisation> echEvent) {
		page.setObject(echEvent.object);
		return page;
	}

	
}
