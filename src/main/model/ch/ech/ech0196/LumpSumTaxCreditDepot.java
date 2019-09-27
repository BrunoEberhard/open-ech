package ch.ech.ech0196;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.738")
public class LumpSumTaxCreditDepot {
	public static final LumpSumTaxCreditDepot $ = Keys.of(LumpSumTaxCreditDepot.class);

	public Object id;
	public List<LumpSumTaxCreditSecurity> security;
	@NotEmpty
	@Size(30)
	public String depotNumber;
}