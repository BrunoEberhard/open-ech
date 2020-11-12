package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class DwellingIdentification {
	public static final DwellingIdentification $ = Keys.of(DwellingIdentification.class);

	@NotEmpty
	@Size(9)
	public Integer EGID;
	@NotEmpty
	@Size(2)
	public Integer EDID;
	@NotEmpty
	@Size(3)
	public Integer EWID;
	public final ch.openech.model.NamedId localID = new ch.openech.model.NamedId();
}