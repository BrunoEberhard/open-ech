package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.estate.Building.ThermotechnicalDevice;

public class ThermotechnicalDeviceFormElement extends ListFormElement<ThermotechnicalDevice> {

	public ThermotechnicalDeviceFormElement(List<ThermotechnicalDevice> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected Form<ThermotechnicalDevice> createForm(boolean edit) {
		return new ThermotechnicalDevicePanel();
	}
	
	public static class ThermotechnicalDevicePanel extends Form<ThermotechnicalDevice> {
		
		public ThermotechnicalDevicePanel() {
			super(2);
			line(ThermotechnicalDevice.$.heatGenerator, ThermotechnicalDevice.$.heatingType);
			line(ThermotechnicalDevice.$.informationSource, ThermotechnicalDevice.$.revisionDate);
			line(ThermotechnicalDevice.$.heatForHeating, ThermotechnicalDevice.$.heatForWarmwater);
			line(ThermotechnicalDevice.$.energySource);
		}

	}


}