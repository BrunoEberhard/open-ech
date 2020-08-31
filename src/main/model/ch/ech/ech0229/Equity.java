package ch.ech.ech0229;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.491260")
public class Equity {
	public static final Equity $ = Keys.of(Equity.class);

	public Object id;
	public final TaxAmount paidInCapitalOrNetAsset = new TaxAmount();
	public List<DisclosedReserves> disclosedReserves;
	public ValuationDifferenceRealEstate valuationDifferenceRealEstate;
	public TaxedHiddenReservesList taxedHiddenReservesList;
	public TaxAmount correctionForOwnShares;
	public TaxAmount hiddenEquity;
	public TaxAmount equityBeforeDeductionOfLosses;
	public TaxAmount lossBroughtForward;
	public final TaxAmount totalEquity = new TaxAmount();
	@NotEmpty
	public Long totalTaxableEquity;
	@NotEmpty
	public Long totalTaxableEquityCanton;
	public CantonExtension cantonExtension;
}