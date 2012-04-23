package ch.openech.vaadin;

import java.util.ResourceBundle;

import ch.openech.client.ApplicationConfigOpenEch;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchFormats;
import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.vaadin.MinimalJVaadinApplication;

public class OpenEchVaadinApplication extends MinimalJVaadinApplication {

	public OpenEchVaadinApplication() {
		// note: in Vaadin there is an Application per session
		// and so there is one preferences object for each session/user
		OpenEchPreferences preferences = new OpenEchPreferences();
		getApplicationContext().setPreferences(preferences);
	}
	
	static {
		ApplicationConfig.setApplicationConfig(new ApplicationConfigOpenEch());
		Resources.addResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch"));
		EchFormats.initialize();
	}

}
