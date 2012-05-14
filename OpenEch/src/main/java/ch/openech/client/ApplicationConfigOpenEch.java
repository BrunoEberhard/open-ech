package ch.openech.client;

import java.util.ResourceBundle;

import ch.openech.client.ewk.BaseDeliveryEditor;
import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.client.ewk.event.ExportAllAction;
import ch.openech.client.ewk.event.ImportAllAction;
import ch.openech.client.ewk.event.KeyDeliveryAction;
import ch.openech.client.ewk.event.birth.BirthEvent;
import ch.openech.client.ewk.event.moveIn.MoveInWizard;
import ch.openech.client.org.BaseDeliveryOrganisationEditor;
import ch.openech.client.org.FoundationEditor;
import ch.openech.client.org.MoveInEditor;
import ch.openech.client.org.SearchOrganisationPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.client.preferences.OpenEchPreferences.ApplicationSchemaData;
import ch.openech.client.preferences.PreferencesEditor;
import ch.openech.datagenerator.GeneratePersonAction;
import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.xml.write.EchNamespaceContext;

public class ApplicationConfigOpenEch extends ApplicationConfig {

	private EchNamespaceContext ewkNamespaceContext;
	private EchNamespaceContext orgNamespaceContext;
	private EchNamespaceContext tpnNamespaceContext;
	
	@Override
	public ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle("ch.openech.resources.OpenEch");
	}

	@Override
	public String getWindowTitle(PageContext pageContext) {
		return "Open eCH";
	}

	@Override
	public Class<?> getPreferencesClass() {
		return OpenEchPreferences.class;
	}

	@Override
	public Class<?>[] getSearchClasses() {
//		return new Class<?>[]{SearchOrganisationPage.class};
		return new Class<?>[]{SearchPersonPage.class, SearchOrganisationPage.class};
	}

	@Override
	public void fillActionGroup(PageContext context, ActionGroup actionGroup) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		boolean isDevMode = preferences.devMode();
		
		updateEwkNamespaceContext(context);
		ActionGroup file = actionGroup.getOrCreateActionGroup(ActionGroup.FILE);
		ActionGroup niu = file.getOrCreateActionGroup(ActionGroup.NEW);
		
		if (ewkNamespaceContext != null) {
			niu.add(new EditorPageAction(MoveInWizard.class, ewkNamespaceContext.getVersion()));
			niu.add(new EditorDialogAction(new BirthEvent(ewkNamespaceContext)));
			niu.add(new EditorPageAction(BaseDeliveryEditor.class, ewkNamespaceContext.getVersion()));
			
			ActionGroup export = file.getOrCreateActionGroup(ActionGroup.EXPORT);
			export.add(new ExportAllAction(ewkNamespaceContext));
//			export.add(new ExportAllDialogAction(ewkNamespaceContext));
			if (ewkNamespaceContext.keyDeliveryPossible()) {
				export.add(new KeyDeliveryAction(ewkNamespaceContext));
			}
			ActionGroup imprt = file.getOrCreateActionGroup(ActionGroup.IMPORT);
			imprt.add(new ImportAllAction());
			if (isDevMode) {
				imprt.add(new GeneratePersonAction(ewkNamespaceContext));
			}
		}
//		
		if (orgNamespaceContext != null) {
			niu.add(new EditorPageAction(FoundationEditor.class, orgNamespaceContext.getVersion()));
			niu.add(new EditorPageAction(MoveInEditor.class, orgNamespaceContext.getVersion()));
			niu.add(new EditorPageAction(BaseDeliveryOrganisationEditor.class, orgNamespaceContext.getVersion()));
		}
//		
//		if (tpnNamespaceContext != null) {
////			niu.add(new EditorDialogAction(new TpnMoveEditor(MoveDirection.IN.toString())));
//		}
		
		ActionGroup window = actionGroup.getOrCreateActionGroup(ActionGroup.WINDOW);
		window.addSeparator();
		window.add(new EditorDialogAction(new PreferencesEditor()));
	}
	
	private void updateEwkNamespaceContext(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		ApplicationSchemaData applicationData = preferences.applicationSchemaData;
		if (applicationData.schema20 != null) {
			if (ewkNamespaceContext == null || !applicationData.schema20.equals(ewkNamespaceContext.getVersion())) {
				ewkNamespaceContext = EchNamespaceContext.getNamespaceContext(20, applicationData.schema20);
			}
		} else {
			ewkNamespaceContext = null;
		}
		
		if (applicationData.schema93 != null) {
			if (tpnNamespaceContext == null || !applicationData.schema93.equals(tpnNamespaceContext.getVersion())) {
				tpnNamespaceContext = EchNamespaceContext.getNamespaceContext(93, applicationData.schema93);
			}
		} else {
			tpnNamespaceContext = null;
		}
		
		if (applicationData.schema148 != null) {
			if (orgNamespaceContext == null || !applicationData.schema148.equals(orgNamespaceContext.getVersion())) {
				orgNamespaceContext = EchNamespaceContext.getNamespaceContext(148, applicationData.schema148);
			}
		} else {
			orgNamespaceContext = null;
		}
	}
}
