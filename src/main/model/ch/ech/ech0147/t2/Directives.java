package ch.ech.ech0147.t2;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.409083700")
public class Directives {
	public static final Directives $ = Keys.of(Directives.class);

	public Object id;
	@NotEmpty
	public List<Directive> directive;
}