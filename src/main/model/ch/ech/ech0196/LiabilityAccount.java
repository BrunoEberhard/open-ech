package ch.ech.ech0196;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.431260400")
public class LiabilityAccount {
	public static final LiabilityAccount $ = Keys.of(LiabilityAccount.class);

	public Object id;
	public LiabilityAccountTaxValue taxValue;
	public List<LiabilityAccountPayment> payment;
	@NotEmpty
	@Size(30)
	public String iban;
	@NotEmpty
	@Size(30)
	public String bankAccountNumber;
	@NotEmpty
	@Size(40)
	public String bankAccountName;
	@NotEmpty
	@Size(2)
	public String bankAccountCountry;
	@NotEmpty
	@Size(3)
	public String bankAccountCurrency;
	@NotEmpty
	public LocalDate openingDate;
	@NotEmpty
	public LocalDate closingDate;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalTaxValue;
	@NotEmpty
	public BigDecimal totalGrossRevenueB;
}