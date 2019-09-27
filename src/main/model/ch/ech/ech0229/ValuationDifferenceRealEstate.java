package ch.ech.ech0229;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.541")
public class ValuationDifferenceRealEstate {
	public static final ValuationDifferenceRealEstate $ = Keys.of(ValuationDifferenceRealEstate.class);

	@NotEmpty
	public Long netWorthTax;
	@NotEmpty
	public Long carryingAmount;
	@NotEmpty
	public Long valuationDifference;
	public CantonExtension cantonExtension;
}