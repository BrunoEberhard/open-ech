package ch.openech.client.preferences;

import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.ZipTown;
import ch.openech.mj.db.model.Constants;

public class PreferencesDefaultsData {
	public static final PreferencesDefaultsData PREFERENCES_DEFAULTS_DATA = Constants.of(PreferencesDefaultsData.class);
	
	public final MunicipalityIdentification residence = new MunicipalityIdentification();
	public final ZipTown zipTown = new ZipTown();
	public String cantonAbbreviation;
	public String language;
	public String religion;
}