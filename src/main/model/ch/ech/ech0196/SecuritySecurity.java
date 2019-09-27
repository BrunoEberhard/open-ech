package ch.ech.ech0196;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.691")
public class SecuritySecurity {
	public static final SecuritySecurity $ = Keys.of(SecuritySecurity.class);

	public Object id;
	public List<SecurityTaxValue> taxValue;
	public List<SecurityPayment> payment;
	public List<SecurityStock> stock;
	public ch.openech.model.UidStructure uid;
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
	@NotEmpty
	public LocalDate issueDate;
	@NotEmpty
	public LocalDate redemptionDate;
	@NotEmpty
	public LocalDate redemptionDateEarly;
	@NotEmpty
	public BigDecimal issuePrice;
	@NotEmpty
	public BigDecimal redemptionPrice;
	@NotEmpty
	public BigDecimal redemptionPriceEarly;
	@NotEmpty
	public BigDecimal interestRate;
	@NotEmpty
	public Boolean variableInterest;
	@NotEmpty
	public Boolean iup;
	@NotEmpty
	public Boolean bfp;
}