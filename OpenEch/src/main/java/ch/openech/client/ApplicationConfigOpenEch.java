package ch.openech.client;

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Action;

import ch.openech.client.ewk.BaseDeliveryEditor;
import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.client.ewk.event.ExportAllAction;
import ch.openech.client.ewk.event.KeyDeliveryAction;
import ch.openech.client.ewk.event.birth.BirthEvent;
import ch.openech.client.ewk.event.moveIn.MoveInWizard;
import ch.openech.client.org.BaseDeliveryOrganisationEditor;
import ch.openech.client.org.FoundationEditor;
import ch.openech.client.org.MoveInEditor;
import ch.openech.client.org.SearchOrganisationPage;
import ch.openech.client.preferences.PreferenceData.ApplicationSchemaData;
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
import ch.openech.mj.util.StringUtils;
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
		return new Class<?>[]{SearchPersonPage.class, SearchOrganisationPage.class};
	}

	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		boolean isDevMode = ApplicationMode.Entwicklermodus.getKey().equals(applicationData.applicationMode);
		
		updateEwkNamespaceContext();
		ActionGroup file = actionGroup.getOrCreateActionGroup(ActionGroup.FILE);
		ActionGroup niu = file.getOrCreateActionGroup(ActionGroup.NEW);
		
		if (ewkNamespaceContext != null) {
//			niu.add(new EditorDialogAction(new MoveInWizard(ewkNamespaceContext)));
			niu.add(new EditorPageAction(MoveInWizard.class, ewkNamespaceContext.getVersion()));
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
			niu.add(new EditorDialogAction(new BaseDeliveryOrganisationEditor(orgNamespaceContext)));
			niu.add(new EditorDialogAction(new FoundationEditor(orgNamespaceContext)));
			niu.add(new EditorDialogAction(new MoveInEditor(orgNamespaceContext)));
		}
		
		if (tpnNamespaceContext != null) {
			niu.add(new EditorDialogAction(new TpnMoveEditor(MoveDirection.IN.toString())));
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
		public String getInformation() {
			return "<html><b>Hinweis:</b> Die Einstellungen werden erst bei<br>einem neu geöffneten Fenster aktiv</html>";
		}

		@Override
		public Action[] getActions() {
			// ignore demoAction, doesnt make sense in Preferences
			return new Action[]{cancelAction, saveAction};
		}
		
		@Override
		protected void validate(ApplicationSchemaData data, List<ValidationMessage> resultList) {
			if (StringUtils.isEmpty(data.schema20) && StringUtils.isEmpty(data.schema93) && StringUtils.isEmpty(data.schema148)) {
				resultList.add(new ValidationMessage(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.schema20, "Mindestens ein Schema ist erforderlich"));
			}
		}

		@Override
		protected boolean save(ApplicationSchemaData object) {
			ApplicationConfigOpenEch.this.applicationData = object;
			return true;
		}
	
	}
}
