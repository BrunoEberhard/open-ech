package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.485276200")
public class AttachmentFile {
	public static final AttachmentFile $ = Keys.of(AttachmentFile.class);

	public CantonExtension cantonExtension;
	@NotEmpty
	@Size(400)
	public String pathFileName;
	@NotEmpty
	public Integer internalSortOrder;
}