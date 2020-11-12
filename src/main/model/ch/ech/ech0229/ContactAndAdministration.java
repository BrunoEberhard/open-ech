package ch.ech.ech0229;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ContactAndAdministration {
	public static final ContactAndAdministration $ = Keys.of(ContactAndAdministration.class);

	public Object id;
	public Representative representative;
	@NotEmpty
	public ch.ech.ech0097.OrganisationIdentification organisation;
	public final ch.ech.ech0046.Address headOffice = new ch.ech.ech0046.Address();
	public List<ch.ech.ech0046.Address> secondaryTaxDomicile;
	@NotEmpty
	@Size(400)
	public String purpose;
	public LocalDate foundationDate;
	public LocalDate incorporationDate;
	public List<MemberOfAdministrativeBody> administrativeBody;
	public Representative auditor;
	public Representative contactForInquiries;
	public Boolean requestForPrivilegedTaxation;
	public Boolean separateTaxationOfCapitalAndRevaluationGainsOnParticipations;
	public Boolean changeOfInvestmentCostsOfQualifyingParticipations;
	@Size(400)
	public String remarks;
	public CantonExtension cantonExtension;
}