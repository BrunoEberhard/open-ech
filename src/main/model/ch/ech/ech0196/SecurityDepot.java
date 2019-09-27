package ch.ech.ech0196;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.675")
public class SecurityDepot {
	public static final SecurityDepot $ = Keys.of(SecurityDepot.class);

	public Object id;
	public List<SecuritySecurity> security;
	@NotEmpty
	@Size(30)
	public String depotNumber;
}