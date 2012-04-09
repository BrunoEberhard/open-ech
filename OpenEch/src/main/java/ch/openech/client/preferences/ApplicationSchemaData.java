package ch.openech.client.preferences;

import ch.openech.mj.db.model.Constants;

public class ApplicationSchemaData {
	public static final ApplicationSchemaData APPLICATION_SCHEMA_DATA = Constants.of(ApplicationSchemaData.class);
	
	public String applicationMode = "1";
	
	public String schema20 = "2.2";

	public String schema93 = "1.0";

	public String schema148 = "1.0";
}
