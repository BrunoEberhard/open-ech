package ch.ech.ech0039;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.610")
public class Document {
	public static final Document $ = Keys.of(Document.class);

	@NotEmpty
	@Size(36)
	public String uuid;
	@NotEmpty
	public Titles titles;
	@NotEmpty
	public DocumentStatus status;
	@NotEmpty
	public Files files;
	public Classification classification;
	public OpenToThePublic openToThePublic;
	public Boolean hasPrivacyProtection;
	public LocalDate openingDate;
	@Size(255) // unknown
	public String owner;
	@Size(255) // unknown
	public String signer;
	@Size(255) // unknown
	public String ourRecordReference;
	public Comments comments;
	public Keywords keywords;
	public Boolean isLeadingDocument;
	public Integer sortOrder;
	@Size(255) // unknown
	public String documentKind;
}