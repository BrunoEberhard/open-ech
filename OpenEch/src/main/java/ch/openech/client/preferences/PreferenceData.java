package ch.openech.client.preferences;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.swing.PreferencesHelper;

public class PreferenceData {

	public final PreferencesDefaultsData preferencesDefaultsData = new PreferencesDefaultsData();
	public final PreferencesSedexData preferencesSedexData = new PreferencesSedexData();
	
	public static class ApplicationSchemaData {
		public static final ApplicationSchemaData APPLICATION_SCHEMA_DATA = Constants.of(ApplicationSchemaData.class);
		@Required
		public String applicationMode = "1";
		public String schema20 = "2.2";
		public String schema93 = "1.0";
		public String schema148 = "1.0";
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
