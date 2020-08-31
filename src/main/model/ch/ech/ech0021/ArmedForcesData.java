package ch.ech.ech0021;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.880609700")
public class ArmedForcesData {
	public static final ArmedForcesData $ = Keys.of(ArmedForcesData.class);

	public ch.openech.model.YesNo armedForcesService;
	public ch.openech.model.YesNo armedForcesLiability;
	public LocalDate armedForcesValidFrom;
}