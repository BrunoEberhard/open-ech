package ch.ech.ech0129;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.386083100")
public class KindOfConstructionWork {
	public static final KindOfConstructionWork $ = Keys.of(KindOfConstructionWork.class);

	@NotEmpty
	public KindOfWork kindOfWork;
	public Boolean energeticRestauration;
	public Boolean renovationHeatingsystem;
	public Boolean innerConversionRenovation;
	public Boolean conversion;
	public Boolean extensionHeighteningHeated;
	public Boolean extensionHeighteningNotHeated;
	public Boolean thermicSolarFacility;
	public Boolean photovoltaicSolarFacility;
	public Boolean otherWorks;
}