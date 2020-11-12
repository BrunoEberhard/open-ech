package ch.ech.ech0211.v1;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class DecisionRuling {
	public static final DecisionRuling $ = Keys.of(DecisionRuling.class);

	public Judgement judgement;
	@NotEmpty
	@Size(1000)
	public String ruling;
	@NotEmpty
	public LocalDate date;
	@NotEmpty
	public ch.ech.ech0097.OrganisationIdentification rulingAuthority;
}