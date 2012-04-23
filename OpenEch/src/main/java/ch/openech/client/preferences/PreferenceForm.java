package ch.openech.client.preferences;

import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.client.ewk.event.EchFormPanel;

public class PreferenceForm extends EchFormPanel<OpenEchPreferences> {

	public PreferenceForm() {
		super(2);
		
		addTitle("Schema");
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.applicationMode, OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.schema20);
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.schema93,OpenEchPreferences.OPEN_ECH_PREFERENCES.applicationSchemaData.schema148);

		addTitle("Voreingestellte Werte");
		line(new SwissMunicipalityField(OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.residence, false), OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.zipTown);
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.cantonAbbreviation);
		line(OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.language, OpenEchPreferences.OPEN_ECH_PREFERENCES.preferencesDefaultsData.religion);
	}

//	private FormVisual<OpenEchPreferences.PreferencesSedexData> createSedexConfigurationTab() {
//		AbstractFormVisual<OpenEchPreferences.PreferencesSedexData> panelSedexPreferences = new EchFormPanel<OpenEchPreferences.PreferencesSedexData>(OpenEchPreferences.PreferencesSedexData.class);
//		
//		panelSedexPreferences.line(PREFERENCES_SEDEX_DATA.sedexAddress);
//// TODO Vaadin Directory Field
////		panelSedexPreferences.line(new SwingDirectoryField(PREFERENCES_SEDEX_DATA.sedexInput));
////		panelSedexPreferences.line(new SwingDirectoryField(PREFERENCES_SEDEX_DATA.sedexOutput));
//
//		return panelSedexPreferences;
//	}
	
}
