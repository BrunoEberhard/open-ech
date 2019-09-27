package ch.ech.ech0196;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.550")
public class Institution {
	public static final Institution $ = Keys.of(Institution.class);

	public ch.openech.model.UidStructure uid;
	@NotEmpty
	@Size(20)
	public String lei;
	@NotEmpty
	@Size(60)
	public String name;
}