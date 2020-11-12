package ch.ech.ech0147.t2;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Directive {
	public static final Directive $ = Keys.of(Directive.class);

	public Object id;
	@NotEmpty
	@Size(36)
	public String uuid;
	@NotEmpty
	public ch.ech.ech0039.DirectiveInstruction instruction;
	@NotEmpty
	public ch.ech.ech0039.Priority priority;
	public ch.ech.ech0039.Titles titles;
	public LocalDate deadline;
	@Size(255) // unknown
	public String serviceId;
	public ch.ech.ech0039.Comments comments;
	public ch.ech.ech0147.t0.Dossiers dossiers;
	public ch.ech.ech0147.t0.Documents documents;
	public ch.ech.ech0147.t0.Addresses addresses;
	public List<ch.ech.ech0147.t0.ApplicationCustom> applicationCustom;
}