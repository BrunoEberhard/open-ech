package ch.ech.ech0196;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.428260500")
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