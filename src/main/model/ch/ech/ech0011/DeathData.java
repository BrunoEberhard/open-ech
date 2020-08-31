package ch.ech.ech0011;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.756287500")
public class DeathData {
	public static final DeathData $ = Keys.of(DeathData.class);

	public final DeathPeriod deathPeriod = new DeathPeriod();
	public GeneralPlace placeOfDeath;
}