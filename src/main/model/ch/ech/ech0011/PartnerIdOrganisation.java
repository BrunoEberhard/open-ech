package ch.ech.ech0011;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PartnerIdOrganisation {
	public static final PartnerIdOrganisation $ = Keys.of(PartnerIdOrganisation.class);

	public Object id;
	public final ch.openech.model.NamedId localPersonId = new ch.openech.model.NamedId();
	public List<ch.openech.model.NamedId> otherPersonId;
}