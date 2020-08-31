package ch.ech.ech0196;

import java.util.List;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.438261100")
public class LumpSumTaxCreditSecurity {
	public static final LumpSumTaxCreditSecurity $ = Keys.of(LumpSumTaxCreditSecurity.class);

	public Object id;
	public LumpSumTaxCreditTaxValue taxValue;
	public List<LumpSumTaxCreditPayment> payment;
	@NotEmpty
	@Size(12)
	public Integer valorNumber;
	@NotEmpty
	@Size(12)
	public String isin;
	@NotEmpty
	@Size(2)
	public String country;
	@NotEmpty
	@Size(40)
	public String city;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public QuotationType quotationType;
	@NotEmpty
	public BigDecimal nominalValue;
	@NotEmpty
	public SecurityCategory securityCategory;
	@NotEmpty
	public SecurityType securityType;
	@NotEmpty
	@Size(60)
	public String securityName;
}