package ch.openech.client.ewk;

import java.util.List;
import java.util.ResourceBundle;

import ch.openech.client.ApplicationSchemaData;
import ch.openech.client.ewk.event.ExportAllAction;
import ch.openech.client.ewk.event.KeyDeliveryAction;
import ch.openech.client.ewk.event.birth.BirthEvent;
import ch.openech.client.ewk.event.moveIn.MoveInWizard;
import ch.openech.client.tpn.MoveDirection;
import ch.openech.client.tpn.TpnMoveEditor;
import ch.openech.datagenerator.GeneratePersonAction;
import ch.openech.dm.code.ApplicationMode;
import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.xml.write.EchNamespaceContext;

public class ApplicationConfigOpenEch extends ApplicationConfig {

	private ApplicationSchemaData applicationData = new ApplicationSchemaData();
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
	public Class<?>[] getSearchClasses() {
		return new Class<?>[]{SearchPersonPage.class};
	}

	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		boolean isDevMode = ApplicationMode.Entwicklermodus.getKey().equals(applicationData.applicationMode);
		
		updateEwkNamespaceContext();
		ActionGroup file = actionGroup.getOrCreateActionGroup(ActionGroup.FILE);
		ActionGroup niu = file.getOrCreateActionGroup(ActionGroup.NEW);
		
		if (ewkNamespaceContext != null) {
			niu.add(new EditorDialogAction(new MoveInWizard(ewkNamespaceContext)));
			niu.add(new EditorDialogAction(new BirthEvent(ewkNamespaceContext)));
			
			ActionGroup export = file.getOrCreateActionGroup(ActionGroup.EXPORT);
			export.add(new ExportAllAction(ewkNamespaceContext));
			if (ewkNamespaceContext.keyDeliveryPossible()) {
				export.add(new KeyDeliveryAction(ewkNamespaceContext));
			}
			ActionGroup imprt = file.getOrCreateActionGroup(ActionGroup.IMPORT);
			imprt.add(new EditorPageAction(BaseDeliveryEditor.class, ewkNamespaceContext.getVersion()));
			if (isDevMode) {
				imprt.add(new GeneratePersonAction(ewkNamespaceContext));
			}
		}
		
		if (orgNamespaceContext != null) {
			niu.add(new EditorDialogAction(new CreateOrganisationEditor(orgNamespaceContext)));
		}
		
		if (tpnNamespaceContext != null) {
			niu.add(new EditorPageAction(TpnMoveEditor.class, MoveDirection.IN.toString()));
		}
		
		ActionGroup window = actionGroup.getOrCreateActionGroup(ActionGroup.WINDOW);
		ActionGroup preferences = window.getOrCreateActionGroup(ActionGroup.PREFERENCES);
		preferences.add(new EditorDialogAction(new ApplicationSchemaEditor()));
	}
	
	private void updateEwkNamespaceContext() {
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
	
	private class ApplicationSchemaEditor extends Editor<ApplicationSchemaData> {

		@Override
		protected FormVisual<ApplicationSchemaData> createForm() {
			AbstractFormVisual<ApplicationSchemaData> form = new AbstractFormVisual<ApplicationSchemaData>(ApplicationSchemaData.class, null, true);
			form.line(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.applicationMode);
			form.line(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.schema20);
			form.line(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.schema93);
			form.line(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.schema148);
			return form;
		}

		@Override
		protected ApplicationSchemaData load() {
			return applicationData;
		}

		@Override
		protected void validate(ApplicationSchemaData object, List<ValidationMessage> resultList) {
			// nothing to validate
		}

		@Override
		protected boolean save(ApplicationSchemaData object) {
			ApplicationConfigOpenEch.this.applicationData = object;
			return true;
		}
	
	}
}
