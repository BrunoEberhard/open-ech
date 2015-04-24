package ch.openech.frontend.preferences;

import ch.openech.frontend.e07.MunicipalityFormElement;
import ch.openech.frontend.ewk.event.EchForm;

public class PreferenceForm extends EchForm<OpenEchPreferences> {

	public PreferenceForm() {
		super(2);
		
		addTitle("Schema");
		line(OpenEchPreferences.$.applicationSchemaData.schema20);
		line(OpenEchPreferences.$.applicationSchemaData.schema93,OpenEchPreferences.$.applicationSchemaData.schema148);

		addTitle("Voreingestellte Werte");
		line(new MunicipalityFormElement(OpenEchPreferences.$.preferencesDefaultsData.residence, false), OpenEchPreferences.$.preferencesDefaultsData.plz);
		line(OpenEchPreferences.$.preferencesDefaultsData.canton);
		line(OpenEchPreferences.$.preferencesDefaultsData.language, OpenEchPreferences.$.preferencesDefaultsData.religion);

		text("<b>Hinweis:</b> Die Einstellungen werden erst bei neu geöffneten Ansichten aktiv");
	}
	
}
