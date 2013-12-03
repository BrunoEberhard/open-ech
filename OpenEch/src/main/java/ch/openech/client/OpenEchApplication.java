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

import java.util.List;
import java.util.ResourceBundle;

import ch.openech.client.editor.BaseDeliveryEditor;
import ch.openech.client.editor.BaseDeliveryOrganisationEditor;
import ch.openech.client.editor.FoundationEditor;
import ch.openech.client.editor.MoveInEditor;
import ch.openech.client.editor.MoveInWizard;
import ch.openech.client.ewk.event.ExportAllPersonAction;
import ch.openech.client.ewk.event.ImportAllPersonAction;
import ch.openech.client.ewk.event.KeyDeliveryPersonAction;
import ch.openech.client.ewk.event.birth.BirthEvent;
import ch.openech.client.org.ExportAllOrganisationAction;
import ch.openech.client.org.ImportAllOrganisationAction;
import ch.openech.client.org.KeyDeliveryOrganisationAction;
import ch.openech.client.page.SearchOrganisationPage;
import ch.openech.client.page.SearchPersonPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.client.preferences.OpenEchPreferences.ApplicationSchemaData;
import ch.openech.client.preferences.PreferencesEditor;
import ch.openech.datagenerator.GeneratePersonEditor;
import ch.openech.mj.application.DevMode;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.toolkit.IAction;
import ch.openech.xml.write.EchSchema;

public class OpenEchApplication extends MjApplication {

	private EchSchema ewkSchema;
	private EchSchema orgSchema;
	private EchSchema tpnSchema;
	
	@Override
	public void init() {
		super.init();

		final String demoPersons = System.getProperty("DemoPerson");

//		if (!StringUtils.isEmpty(demoPersons)) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						int count = Integer.parseInt(demoPersons);
//						ewkSchema = EchSchema.getNamespaceContext(EchSchema0020._2_2);
//						WriterEch0020 writerEch0020 = new WriterEch0020(ewkSchema);
//						for (int i = 0; i<count; i++) {
//							DataGenerator.generatePerson(writerEch0020);
//						}
//					} catch (Exception x) {
//						System.err.println("Could not generate DemoPerson");
//						x.printStackTrace();
//					}
//				}
//			}).start();
//		}
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
	public List<IAction> getActionsNew(PageContext context) {
		ActionGroup menu = new ActionGroup(null);
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		updateEwkNamespaceContext(context);
		
		if (ewkSchema != null) {
			menu.add(new MoveInWizard(ewkSchema, preferences));
			menu.add(new BirthEvent(ewkSchema, preferences)); // Dialog!
			menu.add(new BaseDeliveryEditor(ewkSchema, preferences));
		}
		if (orgSchema != null) {
			menu.add(new FoundationEditor(orgSchema, preferences));
			menu.add(new MoveInEditor(orgSchema, preferences));
			menu.add(new BaseDeliveryOrganisationEditor(orgSchema, preferences));
		}
//			if (tpnSchema != null) {
////				actions.add(new EditorDialogAction(new TpnMoveEditor(MoveDirection.IN.toString())));
//			}
		
		return menu.getItems();
	}

	@Override
	public List<IAction> getActionsImport(PageContext context) {
		ActionGroup menu = new ActionGroup(null);
		updateEwkNamespaceContext(context);
		
		if (ewkSchema != null) {
			menu.add(new ImportAllPersonAction());
		}
		if (orgSchema != null) {
			menu.add(new ImportAllOrganisationAction());
		}
		
		boolean isDevMode = DevMode.isActive();
		if (isDevMode && (ewkSchema != null || orgSchema != null)) {
			menu.add(new GeneratePersonEditor(ewkSchema, orgSchema));
		}
		
		return menu.getItems();
	}

	@Override
	public List<IAction> getActionsExport(PageContext context) {
		ActionGroup menu = new ActionGroup(null);
		updateEwkNamespaceContext(context);
		
		if (ewkSchema != null) {
			menu.add(new ExportAllPersonAction(ewkSchema));
			if (ewkSchema.keyDeliveryPossible()) {
				menu.add(new KeyDeliveryPersonAction(ewkSchema));
			}
		}
		if (orgSchema != null) {
			menu.add(new ExportAllOrganisationAction(orgSchema));
			menu.add(new KeyDeliveryOrganisationAction(orgSchema));
		}
		return menu.getItems();
	}

	@Override
	public List<IAction> getActionsWindow(PageContext context) {
		ActionGroup menu = new ActionGroup(null);
		menu.add(new PreferencesEditor(context.getApplicationContext()));
		return menu.getItems();
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
			if (tpnSchema == null || !applicationData.schema93.equals(tpnSchema.getVersion())) {
				tpnSchema = EchSchema.getNamespaceContext(applicationData.schema93);
			}
		} else {
			tpnSchema = null;
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
