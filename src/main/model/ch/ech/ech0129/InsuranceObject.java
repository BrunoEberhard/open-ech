package ch.ech.ech0129;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.863")
public class InsuranceObject {
	public static final InsuranceObject $ = Keys.of(InsuranceObject.class);

	public final ch.openech.xml.NamedId localID = new ch.openech.xml.NamedId();
	public LocalDate startDate;
	public LocalDate endDate;
	@NotEmpty
	@Size(255) // unknown
	public String insuranceNumber;
	public UsageCode usageCode;
	@Size(255) // unknown
	public String usageDescription;
	public InsuranceValue insuranceValue;
	public InsuranceVolume volume;
}