package ch.ech.ech0196;

import java.util.List;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ListOfLiabilities {
	public static final ListOfLiabilities $ = Keys.of(ListOfLiabilities.class);

	public Object id;
	public List<LiabilityAccount> liabilityAccount;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalTaxValue;
	@NotEmpty
	public BigDecimal totalGrossRevenueB;
}