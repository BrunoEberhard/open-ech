package ch.ech.ech0229;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class TaxDeclarationLegalEntity {
	public static final TaxDeclarationLegalEntity $ = Keys.of(TaxDeclarationLegalEntity.class);

	@NotEmpty
	public Header header;
	public final Content content = new Content();
	public CantonExtension cantonExtension;
	@NotEmpty
	public Integer minorVersion;
}