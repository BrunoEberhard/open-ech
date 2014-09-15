package ch.openech.frontend.preferences;

import static ch.openech.frontend.preferences.OpenEchPreferences.*;
import ch.openech.frontend.e07.MunicipalityField;
import ch.openech.frontend.ewk.event.EchForm;

public class PreferenceForm extends EchForm<OpenEchPreferences> {

	public PreferenceForm() {
		super(2);
		
		addTitle("Schema");
		line(OPEN_ECH_PREFERENCES.applicationSchemaData.schema20);
		line(OPEN_ECH_PREFERENCES.applicationSchemaData.schema93,OPEN_ECH_PREFERENCES.applicationSchemaData.schema148);

		addTitle("Voreingestellte Werte");
		line(new MunicipalityField(OPEN_ECH_PREFERENCES.preferencesDefaultsData.residence, false), OPEN_ECH_PREFERENCES.preferencesDefaultsData.plz);
		line(OPEN_ECH_PREFERENCES.preferencesDefaultsData.canton);
		line(OPEN_ECH_PREFERENCES.preferencesDefaultsData.language, OPEN_ECH_PREFERENCES.preferencesDefaultsData.religion);

		text("<b>Hinweis:</b> Die Einstellungen werden erst bei neu geöffneten Ansichten aktiv");
	}
	
}
