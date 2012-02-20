package ch.openech.client.ewk;

import ch.openech.client.tpn.MoveDirection;
import ch.openech.client.tpn.TpnMoveEditor;
import ch.openech.mj.application.WindowConfig;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;

public class WindowConfigTpn implements WindowConfig {

	public WindowConfigTpn () {
	}
	
	@Override
	public String getTitle() {
		return "Drittmeldepflicht";
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		ActionGroup niu = actionGroup.getOrCreateActionGroup(ActionGroup.NEW);
		niu.add(new EditorPageAction(TpnMoveEditor.class, MoveDirection.IN.toString()));
	}
	
	@Override
	public Class<?>[] getSearchClasses() {
		return new Class[0];
	}
}
