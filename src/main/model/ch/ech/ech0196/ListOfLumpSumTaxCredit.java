package ch.ech.ech0196;

import java.util.List;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ListOfLumpSumTaxCredit {
	public static final ListOfLumpSumTaxCredit $ = Keys.of(ListOfLumpSumTaxCredit.class);

	public Object id;
	public List<LumpSumTaxCreditDepot> depot;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalTaxValue;
	@NotEmpty
	public BigDecimal totalGrossRevenueB;
	@NotEmpty
	public BigDecimal totalGrossRevenueBCanton;
	@NotEmpty
	public BigDecimal totalLumpSumTaxCredit;
	@NotEmpty
	public BigDecimal totalNonRecoverableTax;
	@NotEmpty
	public BigDecimal totalAdditionalWithHoldingTaxUSA;
}