package ch.openech.client.page;

import static ch.openech.dm.organisation.Organisation.*;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchSchema0148;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.TablePage;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.xml.write.EchSchema;

public class SearchOrganisationPage extends TablePage<Organisation> {

	private final EchSchema echSchema;
	private final String text;

	public static final Object[] FIELD_NAMES = {
		ORGANISATION.technicalIds.localId.personId, // TODO move to invisible
		ORGANISATION.organisationName, //
		ORGANISATION.businessAddress.mailAddress.street, //
		ORGANISATION.businessAddress.mailAddress.houseNumber.houseNumber, //
		ORGANISATION.businessAddress.mailAddress.town, //
	};
	
	public SearchOrganisationPage(PageContext context, String text) {
		this(context, getVersionFromPreference(context), text);
	}
	
	private static String getVersionFromPreference(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		EchSchema0148 schema = preferences.applicationSchemaData.schema148;
		return schema.getVersion() + "." + schema.getMinorVersion();
	}
	
	public SearchOrganisationPage(PageContext context, String version, String text) {
		super(context, FIELD_NAMES, text);
		this.echSchema = EchSchema.getNamespaceContext(148, version);
		this.text = text;
	}

	@Override
	public String getTitle() {
		return "Suche Organisationen mit " + text;
	}

	@Override
	public ActionGroup getMenu() {
		return null;
	}

	@Override
	protected void clicked(Organisation organisation, List<Organisation> selectedObjects) {
		List<String> pageLinks = new ArrayList<String>(selectedObjects.size());
		int index = 0;
		int count = 0;
		for (Organisation o : selectedObjects) {
			String link = link(OrganisationViewPage.class, echSchema.getVersion(), o.technicalIds.localId.personId);
			pageLinks.add(link);
			if (o == organisation) {
				index = count;
			}
			count++;
		}
		getPageContext().show(pageLinks, index);
	}

	@Override
	protected List<Organisation> load(String query) {
		return Services.get(DbService.class).search(Organisation.class, query, 100);
	}
	
}
