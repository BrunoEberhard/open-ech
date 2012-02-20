package ch.openech.client.ewk;

import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.ZipTown;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.swing.PreferencesHelper;

public class PreferenceData {

	public final PreferencesClientOptionsData preferencesClientOptionsData = new PreferencesClientOptionsData();
	public final PreferencesDefaultsData preferencesDefaultsData = new PreferencesDefaultsData();
	public final PreferencesSedexData preferencesSedexData = new PreferencesSedexData();
	
	
	public static class PreferencesClientOptionsData {
		public static final PreferencesClientOptionsData PREFERENCES_CLIENT_OPTIONS_DATA = Constants.of(PreferencesClientOptionsData.class);

		public boolean codesFree, codesClear;
		public boolean dateFormat;
		public boolean showXml, showLog;
		public boolean generateData;
		public boolean menuBaseDelivery;
		
		public void PreferencesClientOptionsData(PreferenceData preferenceData) {
			// presets
			dateFormat = false;
			codesFree = false;
			codesClear = false;
			generateData = true;
			showXml = true;
		}
	}
	
	public static class PreferencesDefaultsData {
		public static final PreferencesDefaultsData PREFERENCES_DEFAULTS_DATA = Constants.of(PreferencesDefaultsData.class);
		
		public final MunicipalityIdentification residence = new MunicipalityIdentification();
		public final ZipTown zipTown = new ZipTown();
		public String cantonAbbreviation;
		public String language;
		public String religion;
	}
	
	public static class PreferencesSedexData {
		public static final PreferencesSedexData PREFERENCES_SEDEX_DATA = Constants.of(PreferencesSedexData.class);

		public String sedexAddress;
		public String sedexInput;
		public String sedexOutput;
	}

	public static PreferenceData load() {
		PreferenceData preferenceData = new PreferenceData();
		PreferencesHelper.load(preferenceData);
		return preferenceData;
	}
	
	public static void save(PreferenceData preferenceData) {
		PreferencesHelper.save(preferenceData);
	}
}
