package ch.openech.client.ewk;

import ch.openech.mj.application.WindowConfig;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.xml.write.EchNamespaceContext;

public class WindowConfigOrg implements WindowConfig {

	private EchNamespaceContext echNamespaceContext;

	public WindowConfigOrg () {
	}
	
	@Override
	public String getTitle() {
		return "Organisationen";
	}
	
	private synchronized void loadEchNamespaceContext() {
		if (echNamespaceContext == null) {
			echNamespaceContext = EchNamespaceContext.getNamespaceContext(148, "1.0");
		}
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		loadEchNamespaceContext();
		
		ActionGroup create = actionGroup.getOrCreateActionGroup(ActionGroup.NEW);
		create.add(new EditorDialogAction(new CreateOrganisationEditor(echNamespaceContext)));
	}
	
	@Override
	public Class<?>[] getSearchClasses() {
		return new Class<?>[0];
	}
	
}
