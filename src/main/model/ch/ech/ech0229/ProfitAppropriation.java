package ch.ech.ech0229;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.510")
public class ProfitAppropriation {
	public static final ProfitAppropriation $ = Keys.of(ProfitAppropriation.class);

	public Object id;
	@NotEmpty
	public Long undistributedProfitOrLossCarriedForward;
	@NotEmpty
	public Long taxableIncome;
	@NotEmpty
	public Long totalProfitToBeDistributed;
	public List<ProfitAppropriationDetail> profitAppropriationDetails;
	@NotEmpty
	public Long totalProfitAppropriation;
	@NotEmpty
	public Long profitOrLossBroughtForward;
	public CantonExtension cantonExtension;
}