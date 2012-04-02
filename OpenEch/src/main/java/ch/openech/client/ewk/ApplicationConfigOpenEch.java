package ch.openech.client.ewk;

import java.util.ResourceBundle;

import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.application.WindowConfig;

public class ApplicationConfigOpenEch extends ApplicationConfig {

	private WindowConfig initialWindowConfig = new WindowConfigEwk("2.2");
	
	@Override
	public ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle("ch.openech.resources.OpenEch");
	}
	
	@Override
	public WindowConfig getInitialWindowConfig() {
		return initialWindowConfig;
	}
	
	@Override
	public WindowConfig[] getWindowConfigs() {
		return new WindowConfig[]{ //
				initialWindowConfig, //
				new WindowConfigEwk("2.1"), //
				new WindowConfigEwk("2.0"), //
				new WindowConfigEwk("1.1"), //
				new WindowConfigEwk("1.0"), //
				new WindowConfigOrg(), //
				new WindowConfigTpn(), //
		};
	}

}
