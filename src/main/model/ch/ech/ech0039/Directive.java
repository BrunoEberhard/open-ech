package ch.ech.ech0039;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Directive {
	public static final Directive $ = Keys.of(Directive.class);

	@NotEmpty
	@Size(36)
	public String uuid;
	@NotEmpty
	public DirectiveInstruction instruction;
	@NotEmpty
	public Priority priority;
	public Titles titles;
	public LocalDate deadline;
	@Size(255) // unknown
	public String serviceId;
	public Comments comments;
}