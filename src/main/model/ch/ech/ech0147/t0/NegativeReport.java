package ch.ech.ech0147.t0;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class NegativeReport {
	public static final NegativeReport $ = Keys.of(NegativeReport.class);

	@NotEmpty
	public Errors errors;
	public ch.ech.ech0039.Comments comments;
	public ApplicationCustom applicationCustom;
}