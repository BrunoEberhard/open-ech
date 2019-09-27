package ch.ech.ech0229;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// @Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-01-10T17:35:10.079")
public class DocumentIdentification {
	public static final DocumentIdentification $ = Keys.of(DocumentIdentification.class);

	public CantonExtension cantonExtension;
	// public final OriginIdentification documentOrigin = new
	// OriginIdentification();
	public String documentOrigin;
	@NotEmpty
	@Size(100)
	public String documentType;
}