package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.382083700")
public class Right {
	public static final Right $ = Keys.of(Right.class);

	@NotEmpty
	@Size(255) // unknown
	public String EREID;
}