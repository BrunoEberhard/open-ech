package ch.ech.ech0129;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ThermotechnicalDevice {
	public static final ThermotechnicalDevice $ = Keys.of(ThermotechnicalDevice.class);

	@NotEmpty
	public ThermotechnicalHeatGenerator heatGenerator;
	public ThermotechnicalDeviceHeatingType heatingType;
	public ThermotechnicalDeviceInformationSource informationSource;
	public LocalDate revisionDate;
	@NotEmpty
	public Boolean heatForHeating;
	@NotEmpty
	public Boolean heatForWarmwater;
	public ThermotechnicalDeviceEnergySource energySource;
}