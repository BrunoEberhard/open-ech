package ch.openech.client.page;

import static ch.openech.dm.organisation.Organisation.*;

import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchSchema0148;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.AbstractPage;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.toolkit.ITable.TableActionListener;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;

public class SearchOrganisationPage extends AbstractPage implements RefreshablePage {

	private final EchSchema echSchema;
	private final String text;
	private final ITable<Organisation> table;

	public static final Object[] FIELD_NAMES = {
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
		super(context);
		this.echSchema = EchSchema.getNamespaceContext(148, version);
		this.text = text;
		table = ClientToolkit.getToolkit().createTable(Organisation.class, FIELD_NAMES);
		table.setClickListener(new OrganisationTableClickListener());
		refresh();
	}

	@Override
	public IComponent getComponent() {
		return table;
	}
	
	@Override
	public String getTitle() {
		return "Suche Organisationen mit " + text;
	}

	@Override
	public ActionGroup getMenu() {
		return null;
	}

	private class OrganisationTableClickListener implements TableActionListener<Organisation> {
		@Override
		public void action(Organisation selectedObject, List<Organisation> selectedObjects) {
			show(OrganisationViewPage.class, echSchema.getVersion(), selectedObject.getId());
		}
	}
	
	@Override
	public void refresh() {
		List<Organisation> resultList = EchServer.getInstance().getPersistence().organisationIndex().find(text);
		table.setObjects(resultList);
	}
	
}
