package ch.ech.ech0229;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.572")
public class Content {
	public static final Content $ = Keys.of(Content.class);

	@NotEmpty
	public ContactAndAdministration contactAndAdministration;
	public final NetProfit netProfit = new NetProfit();
	public ProfitAppropriation profitAppropriation;
	@NotEmpty
	public Equity equity;
	public DepreciationOnAssetsAppreciatedInPreviousYearsList depreciationOnAssetsAppreciatedInPreviousYearsList;
	public CantonExtension cantonExtension;
}