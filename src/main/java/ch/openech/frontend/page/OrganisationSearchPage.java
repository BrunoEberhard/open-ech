package ch.openech.frontend.page;

import static ch.openech.model.organisation.Organisation.*;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.frontend.page.SearchPage.SimpleSearchPage;
import org.minimalj.transaction.criteria.By;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0148;
import ch.openech.model.organisation.Organisation;
import ch.openech.transaction.EchPersistence;
import ch.openech.xml.write.EchSchema;

public class OrganisationSearchPage extends SimpleSearchPage<Organisation> {

	public static final Object[] FIELDS = {
		$.organisationName, //
		$.businessAddress.mailAddress.street, //
		$.businessAddress.mailAddress.houseNumber.houseNumber, //
		$.businessAddress.mailAddress.town, //
	};
	
	public OrganisationSearchPage(String query) {
		super(query, FIELDS);
	}

	@Override
	protected List<Organisation> load(String query) {
		return Backend.read(Organisation.class, By.search(query), 100);
	}

	@Override
	public ObjectPage<Organisation> createDetailPage(Organisation organisation) {
		OpenEchPreferences preferences = EchPersistence.getPreferences();
		EchSchema0148 schema0148 = preferences.applicationSchemaData.schema148;
		String version = schema0148.getVersion() + "." + schema0148.getMinorVersion();
		EchSchema schema = EchSchema.getNamespaceContext(148, version);

		return new OrganisationPage(schema, organisation);
	}
}
