package ch.ech.ech0011;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.483")
public class PartnerIdOrganisation {
	public static final PartnerIdOrganisation $ = Keys.of(PartnerIdOrganisation.class);

	public Object id;
	public final ch.openech.xml.NamedId localPersonId = new ch.openech.xml.NamedId();
	public List<ch.openech.xml.NamedId> otherPersonId;
}