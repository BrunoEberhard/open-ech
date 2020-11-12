package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class AttachmentFile {
	public static final AttachmentFile $ = Keys.of(AttachmentFile.class);

	public CantonExtension cantonExtension;
	@NotEmpty
	@Size(400)
	public String pathFileName;
	@NotEmpty
	public Integer internalSortOrder;
}