package ch.openech.vaadin;

import java.util.ResourceBundle;

import ch.openech.client.ApplicationConfigOpenEch;
import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.vaadin.MinimalJVaadinApplication;

public class OpenEchVaadinApplication extends MinimalJVaadinApplication {

	public OpenEchVaadinApplication() {
	}
	
	static {
		ApplicationConfig.setApplicationConfig(new ApplicationConfigOpenEch());
		Resources.addResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch"));
	}

}
