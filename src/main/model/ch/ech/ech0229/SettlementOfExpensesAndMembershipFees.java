package ch.ech.ech0229;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.488260400")
public class SettlementOfExpensesAndMembershipFees {
	public static final SettlementOfExpensesAndMembershipFees $ = Keys.of(SettlementOfExpensesAndMembershipFees.class);

	@NotEmpty
	public Long statutoryMembershipFees;
	@NotEmpty
	public Long expensesNotRequiredToGenerateTaxableIncome;
	@NotEmpty
	public Long surplusOfMembershipFees;
	public CantonExtension cantonExtension;
}