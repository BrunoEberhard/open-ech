package ch.ech.ech0011;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class DeathPeriod {
	public static final DeathPeriod $ = Keys.of(DeathPeriod.class);

	@NotEmpty
	public LocalDate dateFrom;
	public LocalDate dateTo;
}