package ch.ech.ech0196;

import java.util.List;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ListOfBankAccounts {
	public static final ListOfBankAccounts $ = Keys.of(ListOfBankAccounts.class);

	public Object id;
	public List<BankAccount> bankAccount;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalTaxValue;
	@NotEmpty
	public BigDecimal totalGrossRevenueA;
	@NotEmpty
	public BigDecimal totalGrossRevenueB;
	@NotEmpty
	public BigDecimal totalWithHoldingTaxClaim;
}