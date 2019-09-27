package ch.ech.ech0196;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.769")
public class TaxStatement {
	public static final TaxStatement $ = Keys.of(TaxStatement.class);

	public Object id;
	public final Institution institution = new Institution();
	public List<Client> client;
	public List<AccompanyingLetter> accompanyingLetter;
	public ListOfBankAccounts listOfBankAccounts;
	public ListOfLiabilities listOfLiabilities;
	public ListOfExpenses listOfExpenses;
	public ListOfSecurities listOfSecurities;
	public ListOfLumpSumTaxCredit listOfLumpSumTaxCredit;
	@NotEmpty
	public LocalDateTime creationDate;
	@NotEmpty
	@Size(4)
	public Integer taxPeriod;
	@NotEmpty
	public LocalDate periodFrom;
	@NotEmpty
	public LocalDate periodTo;
	@NotEmpty
	@Size(2)
	public String country;
	@NotEmpty
	public ch.ech.ech0071.CantonAbbreviation canton;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalTaxValue;
	@NotEmpty
	public BigDecimal totalGrossRevenueA;
	@NotEmpty
	public BigDecimal totalGrossRevenueACanton;
	@NotEmpty
	public BigDecimal totalGrossRevenueB;
	@NotEmpty
	public BigDecimal totalGrossRevenueBCanton;
	@NotEmpty
	public BigDecimal totalWithHoldingTaxClaim;
	@NotEmpty
	public Integer minorVersion;
}