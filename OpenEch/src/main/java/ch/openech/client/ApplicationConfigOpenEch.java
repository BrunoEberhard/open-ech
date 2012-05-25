package ch.openech.client;

import java.util.ResourceBundle;

import page.SearchOrganisationPage;
import page.SearchPersonPage;
import ch.openech.client.ewk.event.ExportAllPersonAction;
import ch.openech.client.ewk.event.ImportAllPersonAction;
import ch.openech.client.ewk.event.KeyDeliveryPersonAction;
import ch.openech.client.ewk.event.birth.BirthEvent;
import ch.openech.client.org.ExportAllOrganisationAction;
import ch.openech.client.org.ImportAllOrganisationAction;
import ch.openech.client.org.KeyDeliveryOrganisationAction;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.client.preferences.OpenEchPreferences.ApplicationSchemaData;
import ch.openech.client.preferences.PreferencesEditor;
import ch.openech.datagenerator.GeneratePersonAction;
import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.xml.write.EchSchema;
import editor.BaseDeliveryEditor;
import editor.BaseDeliveryOrganisationEditor;
import editor.FoundationEditor;
import editor.MoveInEditor;
import editor.MoveInWizard;

public class ApplicationConfigOpenEch extends ApplicationConfig {

	private EchSchema ewkSchema;
	private EchSchema orgSchema;
	private EchSchema tpnNamespaceContext;
	
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
		
		if (ewkSchema != null) {
			niu.add(new EditorPageAction(MoveInWizard.class, ewkSchema.getVersion()));
			niu.add(new EditorDialogAction(new BirthEvent(ewkSchema, preferences)));
			niu.add(new EditorPageAction(BaseDeliveryEditor.class, ewkSchema.getVersion()));
			
			ActionGroup export = file.getOrCreateActionGroup(ActionGroup.EXPORT);
			export.add(new ExportAllPersonAction(ewkSchema));
//			export.add(new ExportAllDialogAction(ewkNamespaceContext));
			if (ewkSchema.keyDeliveryPossible()) {
				export.add(new KeyDeliveryPersonAction(ewkSchema));
			}
			ActionGroup imprt = file.getOrCreateActionGroup(ActionGroup.IMPORT);
			imprt.add(new ImportAllPersonAction());
		}
//		
		if (orgSchema != null) {
			niu.add(new EditorPageAction(FoundationEditor.class, orgSchema.getVersion()));
			niu.add(new EditorPageAction(MoveInEditor.class, orgSchema.getVersion()));
			niu.add(new EditorPageAction(BaseDeliveryOrganisationEditor.class, orgSchema.getVersion()));

			ActionGroup export = file.getOrCreateActionGroup(ActionGroup.EXPORT);
			export.add(new ExportAllOrganisationAction(orgSchema));
			export.add(new KeyDeliveryOrganisationAction(orgSchema));

			ActionGroup imprt = file.getOrCreateActionGroup(ActionGroup.IMPORT);
			imprt.add(new ImportAllOrganisationAction());
		}
//		
//		if (tpnNamespaceContext != null) {
////			niu.add(new EditorDialogAction(new TpnMoveEditor(MoveDirection.IN.toString())));
//		}

		if (isDevMode && (ewkSchema != null || orgSchema != null)) {
			ActionGroup imprt = file.getOrCreateActionGroup(ActionGroup.IMPORT);
			imprt.add(new GeneratePersonAction(ewkSchema, orgSchema));
		}
		
		ActionGroup window = actionGroup.getOrCreateActionGroup(ActionGroup.WINDOW);
		window.addSeparator();
		window.add(new EditorDialogAction(new PreferencesEditor(context.getApplicationContext())));
	}
	
	private void updateEwkNamespaceContext(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		ApplicationSchemaData applicationData = preferences.applicationSchemaData;
		if (applicationData.schema20 != null) {
			if (ewkSchema == null || !applicationData.schema20.equals(ewkSchema.getVersion())) {
				ewkSchema = EchSchema.getNamespaceContext(20, applicationData.schema20);
			}
		} else {
			ewkSchema = null;
		}
		
		if (applicationData.schema93 != null) {
			if (tpnNamespaceContext == null || !applicationData.schema93.equals(tpnNamespaceContext.getVersion())) {
				tpnNamespaceContext = EchSchema.getNamespaceContext(93, applicationData.schema93);
			}
		} else {
			tpnNamespaceContext = null;
		}
		
		if (applicationData.schema148 != null) {
			if (orgSchema == null || !applicationData.schema148.equals(orgSchema.getVersion())) {
				orgSchema = EchSchema.getNamespaceContext(148, applicationData.schema148);
			}
		} else {
			orgSchema = null;
		}
	}
}
