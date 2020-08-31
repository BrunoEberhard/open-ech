package ch.ech.ech0078;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.130083100")
public class Attachment {
	public static final Attachment $ = Keys.of(Attachment.class);

	@NotEmpty
	@Size(100)
	public String title;
	@NotEmpty
	@Size(250)
	public String pathFileName;
	@NotEmpty
	public Boolean leadingDocument;
	@NotEmpty
	public Integer sortOrder;
	@NotEmpty
	public byte[] documentType;
}