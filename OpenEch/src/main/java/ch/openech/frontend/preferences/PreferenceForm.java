package ch.openech.frontend.preferences;

import ch.openech.frontend.e07.MunicipalityField;
import ch.openech.frontend.ewk.event.EchForm;

public class PreferenceForm extends EchForm<OpenEchPreferences> {

	public PreferenceForm() {
		super(2);
		
		addTitle("Schema");
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.schema20);
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.schema93,OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.schema148);

		addTitle("Voreingestellte Werte");
		line(new MunicipalityField(OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.residence, false), OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.plz);
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.cantonAbbreviation.canton);
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.language, OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.religion);

		text("<b>Hinweis:</b> Die Einstellungen werden erst bei neu geöffneten Ansichten aktiv");
	}
	
}
