package ch.ech.ech0097;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class OrganisationIdentification {
	public static final OrganisationIdentification $ = Keys.of(OrganisationIdentification.class);

	public Object id;
	public final ch.openech.model.UidStructure uid = new ch.openech.model.UidStructure();
	public ch.openech.model.NamedId localOrganisationId;
	public List<ch.openech.model.NamedId> OtherOrganisationId;
	@NotEmpty
	@Size(255)
	public String organisationName;
	@Size(255)
	public String organisationLegalName;
	@Size(255)
	public String organisationAdditionalName;
	@Size(4)
	public String legalForm;
}