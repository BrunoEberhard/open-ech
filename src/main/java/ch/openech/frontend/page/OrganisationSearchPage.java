package ch.openech.frontend.page;

import static ch.openech.model.organisation.Organisation.*;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.AbstractSearchPage.SimpleSearchPage;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0148;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationSearchPage extends SimpleSearchPage<Organisation> {

	public static final Object[] FIELD_NAMES = {
		$.technicalIds.localId.personId, // TODO move to invisible
		$.organisationName, //
		$.businessAddress.mailAddress.street, //
		$.businessAddress.mailAddress.houseNumber.houseNumber, //
		$.businessAddress.mailAddress.town, //
	};
	
	public OrganisationSearchPage() {
		super(FIELD_NAMES);
	}

	@Override
	protected List<Organisation> load(String query) {
		return Backend.getInstance().read(Organisation.class, Criteria.search(query), 100);
	}

	@Override
	protected ObjectPage<Organisation> createPage(Organisation organisation) {
		OpenEchPreferences preferences = (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences();
		EchSchema0148 schema0148 = preferences.applicationSchemaData.schema148;
		String version = schema0148.getVersion() + "." + schema0148.getMinorVersion();
		EchSchema schema = EchSchema.getNamespaceContext(20, version);

		return new OrganisationPage(schema, organisation);
	}
}
