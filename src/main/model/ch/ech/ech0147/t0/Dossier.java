package ch.ech.ech0147.t0;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.401083200")
public class Dossier {
	public static final Dossier $ = Keys.of(Dossier.class);

	public Object id;
	@NotEmpty
	@Size(36)
	public String uuid;
	@NotEmpty
	public ch.ech.ech0039.DossierStatus status;
	@NotEmpty
	public ch.ech.ech0039.Titles titles;
	public ch.ech.ech0039.Classification classification;
	public Boolean hasPrivacyProtection;
	public ch.ech.ech0039.OpenToThePublic openToThePublicType;
	@Size(255) // unknown
	public String caseReferenceLocalId;
	public LocalDate openingDate;
	public ch.ech.ech0039.Keywords keywords;
	public ch.ech.ech0039.Comments comments;
	public ch.ech.ech0039.Links links;
	public Addresses addresses;
	public Dossiers dossiers;
	public Documents documents;
	public Folders folders;
	public List<ApplicationCustom> applicationCustom;
}