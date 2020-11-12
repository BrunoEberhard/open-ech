package ch.ech.ech0147.t2;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Content {
	public static final Content $ = Keys.of(Content.class);

	@NotEmpty
	public Directives directives;
}