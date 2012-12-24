/*
	Copyright (C) 2010, Bruno Eberhard, bruno.eberhard@pop.ch

	This file is part of Open-eCH.

    Open-eCH is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Open-eCH is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Open-eCH.  If not, see <http://www.gnu.org/licenses/>.
*/
package ch.openech.client;

import java.util.ResourceBundle;

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
import ch.openech.editor.BaseDeliveryEditor;
import ch.openech.editor.BaseDeliveryOrganisationEditor;
import ch.openech.editor.FoundationEditor;
import ch.openech.editor.MoveInEditor;
import ch.openech.editor.MoveInWizard;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.model.Codes;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.page.SearchOrganisationPage;
import ch.openech.page.SearchPersonPage;
import ch.openech.xml.write.EchSchema;

public class OpenEchApplication extends MjApplication {

	private EchSchema ewkSchema;
	private EchSchema orgSchema;
	private EchSchema tpnNamespaceContext;
	
	@Override
	public void init() {
		super.init();
		Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.organisation.types.ech_organisation"));
		Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.person.types.ech_person"));
	}

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
				ewkSchema = EchSchema.getNamespaceContext(applicationData.schema20);
			}
		} else {
			ewkSchema = null;
		}
		
		if (applicationData.schema93 != null) {
			if (tpnNamespaceContext == null || !applicationData.schema93.equals(tpnNamespaceContext.getVersion())) {
				tpnNamespaceContext = EchSchema.getNamespaceContext(applicationData.schema93);
			}
		} else {
			tpnNamespaceContext = null;
		}
		
		if (applicationData.schema148 != null) {
			if (orgSchema == null || !applicationData.schema148.equals(orgSchema.getVersion())) {
				orgSchema = EchSchema.getNamespaceContext(applicationData.schema148);
			}
		} else {
			orgSchema = null;
		}
	}
}
