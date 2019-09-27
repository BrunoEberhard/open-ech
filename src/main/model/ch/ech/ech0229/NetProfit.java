package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.479")
public class NetProfit {
	public static final NetProfit $ = Keys.of(NetProfit.class);

	public final TaxAmount taxableIncome = new TaxAmount();
	public TaxAdjustmentsIncreasingTaxableResult taxAdjustmentsIncreasingTaxableResult;
	public final TaxAmount sumOftaxableIncomeAndTotalTaxAdjustmentsIncreasingTaxableResult = new TaxAmount();
	public TaxAdjustmentsDecreasingTaxableResult taxAdjustmentDecreasingTaxableResult;
	public final TaxAmount netProfitOrLossActualYear = new TaxAmount();
	public TaxLossCarriedForwardList taxLossCarriedForwardList;
	public final TaxAmount netProfitOrLossIncludingTaxLossesCarriedForward = new TaxAmount();
	public final TaxAmount taxableNetProfitOrLoss = new TaxAmount();
	public TaxAmount foreignOrNonCantonalShareOfTaxableNetProfitOrLoss;
	public SettlementOfExpensesAndMembershipFees settlementOfExpensesAndMembershipFees;
	@Size(3)
	public Integer participationExemptionInPercent;
	public CantonExtension cantonExtension;
}