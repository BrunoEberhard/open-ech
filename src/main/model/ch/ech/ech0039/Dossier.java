package ch.ech.ech0039;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.625")
public class Dossier {
	public static final Dossier $ = Keys.of(Dossier.class);

	@NotEmpty
	@Size(36)
	public String uuid;
	@NotEmpty
	public DossierStatus status;
	@NotEmpty
	public Titles titles;
	public Classification classification;
	public Boolean hasPrivacyProtection;
	public OpenToThePublic openToThePublicType;
	@Size(255) // unknown
	public String caseReferenceLocalId;
	public LocalDate openingDate;
	public Keywords keywords;
	public Comments comments;
	public Links links;
}