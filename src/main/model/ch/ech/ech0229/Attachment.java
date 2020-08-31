package ch.ech.ech0229;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.485276200")
public class Attachment {
	public static final Attachment $ = Keys.of(Attachment.class);

	public Object id;
	public List<AttachmentFile> file;
	public DocumentIdentification documentIdentification;
	public CantonExtension cantonExtension;
	@NotEmpty
	@Size(400)
	public String title;
	@NotEmpty
	@Size(100)
	public String documentFormat;
	@Size(100)
	public String attachedToNumber;
}