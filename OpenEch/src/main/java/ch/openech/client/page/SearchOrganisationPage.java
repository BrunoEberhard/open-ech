package ch.openech.client.page;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;

import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchSchema0148;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.TablePage;
import ch.openech.mj.search.FulltextIndexSearch;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;

public class SearchOrganisationPage extends TablePage<Organisation> {

	private final EchSchema echSchema;
	private final String text;

	public static final Object[] FIELD_NAMES = {
		ORGANISATION.identification.technicalIds.localId.personId, // TODO move to invisible
		ORGANISATION.identification.organisationName, //
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
		super(context, new FulltextIndexSearch<Organisation>(EchServer.getInstance().getPersistence().organisationIndex()), FIELD_NAMES, text);
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
	protected void clicked(int selectedId, List<Integer> selectedIds) {
//		List<String> pageLinks = new ArrayList<String>(20);
//		for (Item i : getItems()) {
//			String link = link(OrganisationViewPage.class, echSchema.getVersion(), (String)i.getValue(ORGANISATION.identification.technicalIds.localId.personId));
//			pageLinks.add(link);
//		}
//		int index = getItems().indexOf(item);
//		getPageContext().show(pageLinks, index);
		Organisation organisation = EchServer.getInstance().getPersistence().organisation().read(selectedId);
		String link = link(OrganisationViewPage.class, echSchema.getVersion(), organisation.identification.technicalIds.localId.personId);
		getPageContext().show(link);
	}
	
}
