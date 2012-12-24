package ch.openech.page;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;

import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualTable;
import ch.openech.mj.toolkit.VisualTable.ClickListener;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;

public class SearchOrganisationPage extends Page implements RefreshablePage {

	private final EchSchema echSchema;
	private String text;
	private VisualTable<Organisation> table;

	public static final Object[] FIELD_NAMES = {
		ORGANISATION.organisationName, //
		ORGANISATION.businessAddress.mailAddress.street, //
		ORGANISATION.businessAddress.mailAddress.houseNumber.houseNumber, //
		ORGANISATION.businessAddress.mailAddress.town, //
	};
	
	// Damit wird die Version des Fenster overruled
	@Deprecated
	public SearchOrganisationPage(PageContext context, String text) {
		this(context,"1.0", text);
	}
	
	public SearchOrganisationPage(PageContext context, String version, String text) {
		super(context);
		this.echSchema = EchSchema.getNamespaceContext(148, version);
		this.text = text;
		table = ClientToolkit.getToolkit().createVisualTable(Organisation.class, FIELD_NAMES);
		table.setClickListener(new OrganisationTableClickListener());
		refresh();
	}

	@Override
	public IComponent getComponent() {
		return table;
	}

	private class OrganisationTableClickListener implements ClickListener {

		@Override
		public void clicked() {
			Organisation organisation = table.getSelectedObject();
			if (organisation != null) {
				show(OrganisationViewPage.class, echSchema.getVersion(), organisation.getId());
			}
		}
	}
	
	@Override
	public void refresh() {
		List<Organisation> resultList = EchServer.getInstance().getPersistence().organisation().find(text);
		table.setObjects(resultList);
	}
	
}
