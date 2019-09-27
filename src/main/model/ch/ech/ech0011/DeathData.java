package ch.ech.ech0011;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.405")
public class DeathData {
	public static final DeathData $ = Keys.of(DeathData.class);

	public final DeathPeriod deathPeriod = new DeathPeriod();
	public GeneralPlace placeOfDeath;
}