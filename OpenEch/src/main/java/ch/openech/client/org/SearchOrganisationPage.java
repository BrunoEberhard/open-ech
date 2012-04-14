package ch.openech.client.org;

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
import ch.openech.xml.write.EchNamespaceContext;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;

public class SearchOrganisationPage extends Page implements RefreshablePage {

	private final EchNamespaceContext echNamespaceContext;
	private String text;
	private VisualTable<Organisation> table;

	public static final Object[] FIELD_NAMES = {
		ORGANISATION.organisationName, //
	};
	
	// Damit wird die Version des Fenster overruled
	@Deprecated
	public SearchOrganisationPage(PageContext context, String text) {
		this(context,"1.0", text);
	}
	
	public SearchOrganisationPage(PageContext context, String version, String text) {
		super(context);
		this.echNamespaceContext = EchNamespaceContext.getNamespaceContext(148, version);
		this.text = text;
		table = ClientToolkit.getToolkit().createVisualTable(Organisation.class, FIELD_NAMES);
		table.setClickListener(new OrganisationTableClickListener());
		refresh();
	}

	@Override
	public IComponent getPanel() {
		return table;
	}

	private class OrganisationTableClickListener implements ClickListener {

		@Override
		public void clicked() {
			Organisation organisation = table.getSelectedObject();
			if (organisation != null) {
				show(OrganisationViewPage.class, echNamespaceContext.getVersion(), organisation.getId());
			}
		}
	}
	
	@Override
	public void refresh() {
		List<Organisation> resultList = EchServer.getInstance().getPersistence().organisation().find(text);
		table.setObjects(resultList);
	}
	
}
