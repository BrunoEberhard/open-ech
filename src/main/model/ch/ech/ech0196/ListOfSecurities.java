package ch.ech.ech0196;

import java.util.List;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ListOfSecurities {
	public static final ListOfSecurities $ = Keys.of(ListOfSecurities.class);

	public Object id;
	public List<SecurityDepot> depot;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalTaxValue;
	@NotEmpty
	public BigDecimal totalGrossRevenueA;
	@NotEmpty
	public BigDecimal totalGrossRevenueACanton;
	@NotEmpty
	public BigDecimal totalGrossRevenueB;
	@NotEmpty
	public BigDecimal totalGrossRevenueBCanton;
	@NotEmpty
	public BigDecimal totalWithHoldingTaxClaim;
	@NotEmpty
	public BigDecimal totalGrossRevenueIUP;
	@NotEmpty
	public BigDecimal totalGrossRevenueConversion;
}