package ch.ech.ech0196;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.613")
public class BankAccountPayment {
	public static final BankAccountPayment $ = Keys.of(BankAccountPayment.class);

	@NotEmpty
	public LocalDate paymentDate;
	@NotEmpty
	@Size(200)
	public String name;
	@NotEmpty
	@Size(3)
	public String amountCurrency;
	@NotEmpty
	public BigDecimal amount;
	@NotEmpty
	public BigDecimal exchangeRate;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal grossRevenueA;
	@NotEmpty
	public BigDecimal grossRevenueB;
	@NotEmpty
	public BigDecimal withHoldingTaxClaim;
}