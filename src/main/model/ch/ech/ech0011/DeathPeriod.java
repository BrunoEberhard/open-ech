package ch.ech.ech0011;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.405")
public class DeathPeriod {
	public static final DeathPeriod $ = Keys.of(DeathPeriod.class);

	@NotEmpty
	public LocalDate dateFrom;
	public LocalDate dateTo;
}