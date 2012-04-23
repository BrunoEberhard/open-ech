package ch.openech.client.preferences;

import ch.openech.dm.code.ApplicationMode;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Zip;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.value.Required;

public class OpenEchPreferences {
	public static final OpenEchPreferences OPEN_ECH_PREFERENCES = Constants.of(OpenEchPreferences.class);
	
	public final ApplicationSchemaData applicationSchemaData = new ApplicationSchemaData();
	public final PreferencesSedexData preferencesSedexData = new PreferencesSedexData();
	public final PreferencesDefaultsData preferencesDefaultsData = new PreferencesDefaultsData();
	
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

	public static class PreferencesDefaultsData {
		public static final PreferencesDefaultsData PREFERENCES_DEFAULTS_DATA = Constants.of(PreferencesDefaultsData.class);
		
		public final MunicipalityIdentification residence = new MunicipalityIdentification();
		public final Zip zipTown = new Zip();
		public String cantonAbbreviation;
		public String language;
		public String religion;
	}
	
	public boolean devMode() {
		return ApplicationMode.Entwicklermodus.getKey().equals(applicationSchemaData.applicationMode);
	}
}
