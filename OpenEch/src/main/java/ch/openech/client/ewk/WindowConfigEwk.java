package ch.openech.client.ewk;

import ch.openech.client.ewk.event.ExportAllAction;
import ch.openech.client.ewk.event.KeyDeliveryAction;
import ch.openech.client.ewk.event.birth.BirthEvent;
import ch.openech.client.ewk.event.moveIn.MoveInWizard;
import ch.openech.datagenerator.GeneratePersonAction;
import ch.openech.mj.application.WindowConfig;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.xml.write.EchNamespaceContext;

public class WindowConfigEwk implements WindowConfig {

	private final EchNamespaceContext echNamespaceContext;
	
	public WindowConfigEwk (EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}
	
	@Override
	public String getTitle() {
		return Resources.getString("Application.title") + " - Schema: " + echNamespaceContext.getVersion();
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		ActionGroup create = actionGroup.getOrCreateActionGroup(ActionGroup.NEW);
		create.add(new EditorDialogAction(new MoveInWizard(echNamespaceContext)));
		create.add(new EditorDialogAction(new BirthEvent(echNamespaceContext)));
		
		ActionGroup admin = actionGroup.getOrCreateActionGroup("admin");
		ActionGroup export = admin.getOrCreateActionGroup("export");
		export.add(new ExportAllAction(echNamespaceContext));
		if (echNamespaceContext.keyDeliveryPossible()) {
			export.add(new KeyDeliveryAction(echNamespaceContext));
		}
		ActionGroup imprt = admin.getOrCreateActionGroup("imprt");
		imprt.add(new EditorPageAction(BaseDeliveryEditor.class, echNamespaceContext.getVersion()));
		// TODO updateBaseDeliveryPageMenuVisibility();
		imprt.add(new GeneratePersonAction(echNamespaceContext));
		// TODO updateGenerateDataVisibility();
	}
	
//	private void updateBaseDeliveryPageMenuVisibility() {
//		boolean visible = preferences.getBoolean("menuBaseDelivery", false);
//		menuItemBaseDeliveryPage.setVisible(visible);
//	}
//
//	private void updateGenerateDataVisibility() {
//		boolean visible = preferences.getBoolean("generateData", true);
//		menuItemGenerateData.setVisible(visible);
//	}
	
	@Override
	public Class<?>[] getSearchClasses() {
		return new Class<?>[]{SearchPersonPage.class};
	}
		
}
