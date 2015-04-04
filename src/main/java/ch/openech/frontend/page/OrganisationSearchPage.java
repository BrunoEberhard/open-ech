package ch.openech.frontend.page;

import static ch.openech.model.organisation.Organisation.*;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.AbstractSearchPage;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0148;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationSearchPage extends AbstractSearchPage<Organisation> {

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
	public void action(Organisation organisation, List<Organisation> selectedObjects) {
		OpenEchPreferences preferences = (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences();
		EchSchema0148 schema0148 = preferences.applicationSchemaData.schema148;
		String version = schema0148.getVersion() + "." + schema0148.getMinorVersion();
		EchSchema schema = EchSchema.getNamespaceContext(20, version);

		List<Page> pages = new ArrayList<>(selectedObjects.size());
		int index = 0;
		int count = 0;
		for (Organisation o : selectedObjects) {
			Page page = new OrganisationPage(schema, o);
			pages.add(page);
			if (o == organisation) {
				index = count;
			}
			count++;
		}
		ClientToolkit.getToolkit().show(pages, index);
	}

	@Override
	protected List<Organisation> load(String query) {
		return Backend.getInstance().read(Organisation.class, Criteria.search(query), 100);
	}
	
}
