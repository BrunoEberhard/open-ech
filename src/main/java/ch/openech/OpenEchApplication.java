/*
	Copyright (C) 2015, Bruno Eberhard, bruno.eberhard@pop.ch
	
	This file is part of Open-eCH.
	
	Open-eCH is free software: you can redistribute it and/or modify
	it under the terms of the GNU Affero General Public License as
	published by the Free Software Foundation, either version 3 of the
	License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Affero General Public License for more details.
	
	You should have received a copy of the GNU Affero General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package ch.openech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.minimalj.application.Application;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.impl.swing.toolkit.SwingFrontend;
import org.minimalj.frontend.page.HtmlPage;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.SearchPage;
import org.minimalj.security.Subject;

import ch.openech.datagenerator.GeneratePersonEditor;
import ch.openech.frontend.editor.BaseDeliveryEditor;
import ch.openech.frontend.editor.BaseDeliveryOrganisationEditor;
import ch.openech.frontend.editor.FoundationEditor;
import ch.openech.frontend.editor.MoveInEditor;
import ch.openech.frontend.editor.MoveInWizard;
import ch.openech.frontend.ewk.event.ExportAllPersonAction;
import ch.openech.frontend.ewk.event.ImportAllPersonAction;
import ch.openech.frontend.ewk.event.KeyDeliveryPersonAction;
import ch.openech.frontend.ewk.event.birth.BirthEvent;
import ch.openech.frontend.org.ExportAllOrganisationAction;
import ch.openech.frontend.org.ImportAllOrganisationAction;
import ch.openech.frontend.org.ImportSwissDataAction;
import ch.openech.frontend.org.KeyDeliveryOrganisationAction;
import ch.openech.frontend.page.OrganisationSearchPage;
import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.frontend.preferences.OpenEchPreferences.ApplicationSchemaData;
import ch.openech.frontend.preferences.PreferencesEditor;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;
import ch.openech.transaction.EchPersistence;
import ch.openech.xml.write.EchSchema;

public class OpenEchApplication extends Application {

	private EchSchema ewkSchema;
	private EchSchema orgSchema;
	private EchSchema tpnSchema;
	
	public OpenEchApplication() {
		// empty
	}
	
	@Override
	protected Set<String> getResourceBundleNames() {
		return Collections.singleton("ch.openech.resources.OpenEch");
	}

	@Override
	public Page createDefaultPage() {
		return new HtmlPage("anleitung.html", "Open eCH - Register");
	}
	
	@Override
	public Page createSearchPage(String query) {
		PersonSearchPage personSearchPage = new PersonSearchPage(query);
		OrganisationSearchPage organisationSearchPage = new OrganisationSearchPage(query);
		return SearchPage.handle(personSearchPage, organisationSearchPage);
	}

	@Override
	public List<Action> getNavigation() {
		List<Action> actions = new ArrayList<>();
		boolean modifyRole = Subject.hasRole(OpenEchRoles.modify);
		boolean administrateRole = Subject.hasRole(OpenEchRoles.administrate);
		boolean importExportAvailable = Frontend.getInstance() instanceof SwingFrontend;
		boolean importExportRole = Subject.hasRole(OpenEchRoles.importExport);
		
		if (!modifyRole) {
			return actions;
		}

		updateEwkNamespaceContext();
		
		if (ewkSchema != null) {
			ActionGroup actionGroupPerson = new ActionGroup("Person");
			actionGroupPerson.add(new MoveInWizard(ewkSchema));
			actionGroupPerson.add(new BirthEvent(ewkSchema));
			if (importExportRole) {
				actionGroupPerson.add(new BaseDeliveryEditor(ewkSchema));
			}
			actions.add(actionGroupPerson);
		}
		if (orgSchema != null) {
			ActionGroup actionGroupOrganisation = new ActionGroup("Firma");
			actionGroupOrganisation.add(new FoundationEditor(orgSchema));
			actionGroupOrganisation.add(new MoveInEditor(orgSchema));
			if (importExportRole) {
				actionGroupOrganisation.add(new BaseDeliveryOrganisationEditor(orgSchema));
			}
			actions.add(actionGroupOrganisation);
		}
		
		if (importExportAvailable && importExportRole) {
			ActionGroup actionGroupImport = new ActionGroup("Import");
			if (ewkSchema != null) {
				actionGroupImport.add(new ImportAllPersonAction());
			}
			if (orgSchema != null) {
				actionGroupImport.add(new ImportAllOrganisationAction());
			}
			actions.add(actionGroupImport);
			
			ActionGroup actionGroupExport = new ActionGroup("Export");
			if (ewkSchema != null) {
				actionGroupExport.add(new ExportAllPersonAction(ewkSchema.getVersion()));
				if (ewkSchema.keyDeliveryPossible()) {
					actionGroupExport.add(new KeyDeliveryPersonAction(ewkSchema.getVersion()));
				}
			}
			if (orgSchema != null) {
				actionGroupExport.add(new ExportAllOrganisationAction(orgSchema.getVersion()));
				actionGroupExport.add(new KeyDeliveryOrganisationAction(orgSchema.getVersion()));
			}
			actions.add(actionGroupExport);
		}

		if (administrateRole) {
			ActionGroup actionGroup = new ActionGroup("Database Setup");
			actionGroup.add(new ImportSwissDataAction());
			if (ewkSchema != null || orgSchema != null) {
				actionGroup.add(new GeneratePersonEditor(ewkSchema, orgSchema));
			}
			actions.add(actionGroup);
		}
		
		ActionGroup actionGroupSettings = new ActionGroup("Einstellungen");
		actionGroupSettings.add(new PreferencesEditor());
		actions.add(actionGroupSettings);
		
		return actions;
	}

	private void updateEwkNamespaceContext() {
		OpenEchPreferences preferences = EchPersistence.getPreferences();
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

	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[]{Person.class, Organisation.class, OpenEchPreferences.class};
	}
	
}
