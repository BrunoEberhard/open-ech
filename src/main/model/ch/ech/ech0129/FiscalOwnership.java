package ch.ech.ech0129;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class FiscalOwnership {
	public static final FiscalOwnership $ = Keys.of(FiscalOwnership.class);

	@NotEmpty
	public LocalDate accessionDate;

	public enum FiscalRelationship { _1, _2, _3;	}
	@NotEmpty
	public FiscalRelationship fiscalRelationship;
	public LocalDate validFrom;
	public LocalDate validTill;
	@Size(7)
	public BigDecimal denominator;
	@Size(7)
	public BigDecimal tally;
}