package ch.ech.ech0196;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.769")
public class LumpSumTaxCreditPayment {
	public static final LumpSumTaxCreditPayment $ = Keys.of(LumpSumTaxCreditPayment.class);

	@NotEmpty
	public LocalDate paymentDate;
	@NotEmpty
	public LocalDate exDate;
	@NotEmpty
	@Size(200)
	public String name;
	@NotEmpty
	public QuotationType quotationType;
	@NotEmpty
	public BigDecimal quantity;
	@NotEmpty
	@Size(3)
	public String amountCurrency;
	@NotEmpty
	public BigDecimal amountPerUnit;
	@NotEmpty
	public BigDecimal amount;
	@NotEmpty
	public BigDecimal exchangeRate;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal grossRevenueB;
	@NotEmpty
	public BigDecimal grossRevenueBCanton;
	@NotEmpty
	public BigDecimal lumpSumTaxCreditPercent;
	@NotEmpty
	public BigDecimal lumpSumTaxCreditAmount;
	@NotEmpty
	public BigDecimal nonRecoverableTaxPercent;
	@NotEmpty
	public BigDecimal nonRecoverableTaxAmount;
	@NotEmpty
	public BigDecimal additionalWithHoldingTaxUSA;
	@NotEmpty
	public Boolean undefined;
	@NotEmpty
	public Boolean kursliste;
	@NotEmpty
	@Size(4)
	public String sign;
}