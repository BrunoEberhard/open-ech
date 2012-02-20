package ch.openech.client.ewk;

import static ch.openech.client.ewk.PreferenceData.PreferencesDefaultsData.PREFERENCES_DEFAULTS_DATA;
import static ch.openech.client.ewk.PreferenceData.PreferencesSedexData.PREFERENCES_SEDEX_DATA;
import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormAdapter;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.form.TabbedForm;
import ch.openech.mj.swing.toolkit.SwingDirectoryField;

public class PreferenceForm extends TabbedForm<PreferenceData> {

	public PreferenceForm() {
		addForm("preferencesDefaultsData", createDefaultsTab());
		addForm("preferencesSedexData", createSedexConfigurationTab());
		addForm("preferencesClientOptionsData", createClientOptionsTab());
		
//		setTitle("Einstellungen");

//		tabbedPane.addTab("Voreingestellte Werte", createDefaultsTab());
//		tabbedPane.addTab("Benutzer Einstellungen", createClientOptionsPanel());
//		tabbedPane.addTab("Sedex", createSedexConfigurationTab());
//
//		setSize(new Dimension(500, 300));
	}


	@Override
	public boolean isResizable() {
		return true;
	}


	private FormVisual<PreferenceData.PreferencesDefaultsData> createDefaultsTab() {
		AbstractFormVisual<PreferenceData.PreferencesDefaultsData> panelPreferences = new EchFormPanel<PreferenceData.PreferencesDefaultsData>(PreferenceData.PreferencesDefaultsData.class);
		
		panelPreferences.line(new SwissMunicipalityField(PREFERENCES_DEFAULTS_DATA.residence, false));
		panelPreferences.line(PREFERENCES_DEFAULTS_DATA.zipTown);
		panelPreferences.line(PREFERENCES_DEFAULTS_DATA.cantonAbbreviation);
		panelPreferences.line(PREFERENCES_DEFAULTS_DATA.language);
		panelPreferences.line(PREFERENCES_DEFAULTS_DATA.religion);

		return panelPreferences;
	}

	private FormVisual<PreferenceData> createClientOptionsTab() {
		ClientOptionsPanel clientOptionsPanel = new ClientOptionsPanel();
		FormVisual<PreferenceData> form = new FormAdapter<PreferenceData>(clientOptionsPanel);
		return form;
	}

	private FormVisual<PreferenceData.PreferencesSedexData> createSedexConfigurationTab() {
		AbstractFormVisual<PreferenceData.PreferencesSedexData> panelSedexPreferences = new EchFormPanel<PreferenceData.PreferencesSedexData>(PreferenceData.PreferencesSedexData.class);
		
		panelSedexPreferences.line(PREFERENCES_SEDEX_DATA.sedexAddress);
		panelSedexPreferences.line(new SwingDirectoryField(PREFERENCES_SEDEX_DATA.sedexInput));
		panelSedexPreferences.line(new SwingDirectoryField(PREFERENCES_SEDEX_DATA.sedexOutput));

		return panelSedexPreferences;
	}
	
}
