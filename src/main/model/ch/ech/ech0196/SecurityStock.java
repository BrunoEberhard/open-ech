package ch.ech.ech0196;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.691")
public class SecurityStock {
	public static final SecurityStock $ = Keys.of(SecurityStock.class);

	@NotEmpty
	public LocalDate referenceDate;
	@NotEmpty
	public Boolean mutation;
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
	public BigDecimal reductionCost;
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
}