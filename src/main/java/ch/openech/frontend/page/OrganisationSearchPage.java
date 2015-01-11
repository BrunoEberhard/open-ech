package ch.openech.frontend.page;

import static ch.openech.model.organisation.Organisation.*;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.IdUtils;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.EchSchema0148;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationSearchPage extends TablePage<Organisation> {

	private final EchSchema echSchema;
	private final String text;

	public static final Object[] FIELD_NAMES = {
		$.technicalIds.localId.personId, // TODO move to invisible
		$.organisationName, //
		$.businessAddress.mailAddress.street, //
		$.businessAddress.mailAddress.houseNumber.houseNumber, //
		$.businessAddress.mailAddress.town, //
	};
	
	public OrganisationSearchPage(String text) {
		this(getVersionFromPreference(), text);
	}
	
	private static String getVersionFromPreference() {
		OpenEchPreferences preferences = (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences();
		EchSchema0148 schema = preferences.applicationSchemaData.schema148;
		return schema.getVersion() + "." + schema.getMinorVersion();
	}
	
	public OrganisationSearchPage(String version, String text) {
		super(FIELD_NAMES, text);
		this.echSchema = EchSchema.getNamespaceContext(148, version);
		this.text = text;
	}

	@Override
	public String getTitle() {
		return "Suche Organisationen mit " + text;
	}

	@Override
	protected void clicked(Organisation organisation, List<Organisation> selectedObjects) {
		List<String> pageLinks = new ArrayList<String>(selectedObjects.size());
		int index = 0;
		int count = 0;
		for (Organisation o : selectedObjects) {
			String link = PageLink.link(OrganisationPage.class, echSchema.getVersion(), IdUtils.getIdString(o));
			pageLinks.add(link);
			if (o == organisation) {
				index = count;
			}
			count++;
		}
		ClientToolkit.getToolkit().show(pageLinks, index);
	}

	@Override
	protected List<Organisation> load(String searchText) {
		return Backend.getInstance().read(Organisation.class, Criteria.search(searchText), 100);
	}
	
}
