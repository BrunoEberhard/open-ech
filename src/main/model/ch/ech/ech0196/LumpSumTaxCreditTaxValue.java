package ch.ech.ech0196;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class LumpSumTaxCreditTaxValue {
	public static final LumpSumTaxCreditTaxValue $ = Keys.of(LumpSumTaxCreditTaxValue.class);

	@NotEmpty
	public LocalDate referenceDate;
	@NotEmpty
	@Size(200)
	public String name;
	@NotEmpty
	public QuotationType quotationType;
	@NotEmpty
	public BigDecimal quantity;
	@NotEmpty
	@Size(3)
	public String balanceCurrency;
	@NotEmpty
	public BigDecimal unitPrice;
	@NotEmpty
	public BigDecimal balance;
	@NotEmpty
	public BigDecimal exchangeRate;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal value;
	@NotEmpty
	public Boolean undefined;
	@NotEmpty
	public Boolean kursliste;
}