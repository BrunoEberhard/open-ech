package ch.ech.ech0129;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class InsuranceValue {
	public static final InsuranceValue $ = Keys.of(InsuranceValue.class);

	public final ch.openech.model.NamedId localID = new ch.openech.model.NamedId();
	@NotEmpty
	public LocalDate validFrom;
	@NotEmpty
	public ChangeReason changeReason;
	public final InsuranceSum insuranceSum = new InsuranceSum();
}