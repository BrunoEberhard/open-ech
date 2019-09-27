package ch.ech.ech0196;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.597")
public class BankAccountTaxValue {
	public static final BankAccountTaxValue $ = Keys.of(BankAccountTaxValue.class);

	@NotEmpty
	public LocalDate referenceDate;
	@NotEmpty
	@Size(200)
	public String name;
	@NotEmpty
	@Size(3)
	public String balanceCurrency;
	@NotEmpty
	public BigDecimal balance;
	@NotEmpty
	public BigDecimal exchangeRate;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal value;
}