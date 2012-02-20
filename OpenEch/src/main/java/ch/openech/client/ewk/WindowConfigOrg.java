package ch.openech.client.ewk;

import ch.openech.mj.application.WindowConfig;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;

public class WindowConfigOrg implements WindowConfig {

	public WindowConfigOrg () {
	}
	
	@Override
	public String getTitle() {
		return "Open-eCH Verzeichnis Organisationen";
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		ActionGroup niu = actionGroup.getOrCreateActionGroup(ActionGroup.NEW);
	}
	
	@Override
	public Class<?>[] getSearchClasses() {
		return new Class<?>[0];
	}
	
}
