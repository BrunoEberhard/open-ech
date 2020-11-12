package ch.ech.ech0147.t0;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Document {
	public static final Document $ = Keys.of(Document.class);

	public Object id;
	@NotEmpty
	@Size(36)
	public String uuid;
	@NotEmpty
	public ch.ech.ech0039.Titles titles;
	@NotEmpty
	public ch.ech.ech0039.DocumentStatus status;
	@NotEmpty
	public Files files;
	public ch.ech.ech0039.Classification classification;
	public ch.ech.ech0039.OpenToThePublic openToThePublic;
	public Boolean hasPrivacyProtection;
	public LocalDate openingDate;
	@Size(255) // unknown
	public String owner;
	@Size(255) // unknown
	public String signer;
	@Size(255) // unknown
	public String ourRecordReference;
	public ch.ech.ech0039.Comments comments;
	public ch.ech.ech0039.Keywords keywords;
	public Boolean isLeadingDocument;
	public Integer sortOrder;
	@Size(255) // unknown
	public String documentKind;
	public List<ApplicationCustom> applicationCustom;
}