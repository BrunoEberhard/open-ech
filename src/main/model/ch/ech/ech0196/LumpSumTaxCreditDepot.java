package ch.ech.ech0196;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.438261100")
public class LumpSumTaxCreditDepot {
	public static final LumpSumTaxCreditDepot $ = Keys.of(LumpSumTaxCreditDepot.class);

	public Object id;
	public List<LumpSumTaxCreditSecurity> security;
	@NotEmpty
	@Size(30)
	public String depotNumber;
}