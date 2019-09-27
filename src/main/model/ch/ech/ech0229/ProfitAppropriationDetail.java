package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.494")
public class ProfitAppropriationDetail {
	public static final ProfitAppropriationDetail $ = Keys.of(ProfitAppropriationDetail.class);

	@NotEmpty
	public ProfitAppropriationDescription profitAppropriationDescription;
	@Size(3)
	public Integer percentageOfPaidInCapital;
	@Size(400)
	public String description;
	@NotEmpty
	public Long amount;
}