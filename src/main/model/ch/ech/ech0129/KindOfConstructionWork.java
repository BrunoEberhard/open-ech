package ch.ech.ech0129;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.003")
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