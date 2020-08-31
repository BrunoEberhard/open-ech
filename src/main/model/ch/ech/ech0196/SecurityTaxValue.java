package ch.ech.ech0196;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.435260900")
public class SecurityTaxValue {
	public static final SecurityTaxValue $ = Keys.of(SecurityTaxValue.class);

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
	public Boolean blocked;
	@NotEmpty
	public LocalDate blockingTo;
	@NotEmpty
	public Boolean undefined;
	@NotEmpty
	public Boolean kursliste;
}