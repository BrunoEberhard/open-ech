package ch.ech.ech0147.t0;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.402083100")
public class Error {
	public static final Error $ = Keys.of(Error.class);

	@NotEmpty
	public ErrorKind errorKind;
	public ch.ech.ech0039.Comments comments;
}