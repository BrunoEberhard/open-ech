package ch.openech.frontend.preferences;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;

import  ch.openech.model.EchSchema0020;
import  ch.openech.model.EchSchema0093;
import  ch.openech.model.EchSchema0148;
import  ch.openech.model.common.CantonAbbreviation;
import  ch.openech.model.common.MunicipalityIdentification;
import  ch.openech.model.person.types.Religion;
import  ch.openech.model.types.Language;

public class OpenEchPreferences implements Validation {
	public static final OpenEchPreferences OPEN_ECH_PREFERENCES = Keys.of(OpenEchPreferences.class);
	
	public final ApplicationSchemaData applicationSchemaData = new ApplicationSchemaData();
	public final PreferencesSedexData preferencesSedexData = new PreferencesSedexData();
	public final PreferencesDefaultsData preferencesDefaultsData = new PreferencesDefaultsData();
	
	public static class ApplicationSchemaData {
		public static final ApplicationSchemaData APPLICATION_SCHEMA_DATA = Keys.of(ApplicationSchemaData.class);
		@Required
		public EchSchema0020 schema20 = EchSchema0020._2_2;
		public EchSchema0093 schema93 = EchSchema0093._1_0;
		public EchSchema0148 schema148 = EchSchema0148._1_0;
	}
	
	public static class PreferencesSedexData {
		public static final PreferencesSedexData PREFERENCES_SEDEX_DATA = Keys.of(PreferencesSedexData.class);

		public String sedexAddress;
		public String sedexInput;
		public String sedexOutput;
	}

	public static class PreferencesDefaultsData {
		public static final PreferencesDefaultsData PREFERENCES_DEFAULTS_DATA = Keys.of(PreferencesDefaultsData.class);
		
		public final MunicipalityIdentification residence = new MunicipalityIdentification();
		@Size(4)
		public String plz;
		public final CantonAbbreviation cantonAbbreviation = new CantonAbbreviation();
		public Language language = Language.de;
		public Religion religion = Religion.unbekannt;
	}
	
	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (applicationSchemaData.schema20 == null && applicationSchemaData.schema93 == null && applicationSchemaData.schema148 == null) {
			resultList.add(new ValidationMessage(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.schema20, "Mindestens ein Schema ist erforderlich"));
		}
	}

}