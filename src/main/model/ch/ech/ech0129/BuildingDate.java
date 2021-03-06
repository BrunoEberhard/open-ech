package ch.ech.ech0129;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class BuildingDate {
	public static final BuildingDate $ = Keys.of(BuildingDate.class);

	public LocalDate yearMonthDay;
	@Size(7)
	public String yearMonth;
	@Size(4)
	public Integer year;
	public PeriodOfConstruction periodOfConstruction;
}