package ch.ech.ech0129;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class DwellingUsage {
	public static final DwellingUsage $ = Keys.of(DwellingUsage.class);

	public DwellingUsageCode usageCode;
	public DwellingInformationSource informationSource;
	public LocalDate revisionDate;
	@Size(2000)
	public String remark;
	public Boolean personWithMainResidence;
	public Boolean personWithSecondaryResidence;
	public LocalDate dateFirstOccupancy;
	public LocalDate dateLastOccupancy;
}