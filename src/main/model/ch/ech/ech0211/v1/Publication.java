package ch.ech.ech0211.v1;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.465260800")
public class Publication {
	public static final Publication $ = Keys.of(Publication.class);

	@NotEmpty
	@Size(255)
	public String officialGazette;
	@NotEmpty
	@Size(1000)
	public String publicationText;
	@NotEmpty
	public LocalDate publicationDate;
	public LocalDate publicationTill;
}