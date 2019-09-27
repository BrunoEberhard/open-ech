package ch.ech.ech0039;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.735")
public class Reference {
	public static final Reference $ = Keys.of(Reference.class);

	@NotEmpty
	@Size(36)
	public String uuid;
	@Size(255) // unknown
	public String serviceInventoryId;
	@Size(255) // unknown
	public String serviceId;
	@Size(255) // unknown
	public String serviceTitle;
	@Size(255) // unknown
	public String serviceProvider;
	@Size(255) // unknown
	public String caseId;
	@Size(255) // unknown
	public String caseTitle;
	@Size(255) // unknown
	public String caseAnnotation;
}