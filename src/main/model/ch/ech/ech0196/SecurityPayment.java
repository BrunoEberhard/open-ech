package ch.ech.ech0196;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.436262400")
public class SecurityPayment {
	public static final SecurityPayment $ = Keys.of(SecurityPayment.class);

	public Object id;
	public List<SecurityPurchaseDisposition> purchase;
	public SecurityPurchaseDisposition disposition;
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
	public BigDecimal grossRevenueA;
	@NotEmpty
	public BigDecimal grossRevenueACanton;
	@NotEmpty
	public BigDecimal grossRevenueB;
	@NotEmpty
	public BigDecimal grossRevenueBCanton;
	@NotEmpty
	public BigDecimal withHoldingTaxClaim;
	@NotEmpty
	public Boolean lumpSumTaxCredit;
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
	public Boolean iup;
	@NotEmpty
	public Boolean conversion;
	@NotEmpty
	public Boolean gratis;
	@NotEmpty
	public Boolean securitiesLending;
	@NotEmpty
	public Boolean lendingFee;
	@NotEmpty
	public Boolean retrocession;
	@NotEmpty
	public Boolean undefined;
	@NotEmpty
	public Boolean kursliste;
	@NotEmpty
	@Size(4)
	public String sign;
}