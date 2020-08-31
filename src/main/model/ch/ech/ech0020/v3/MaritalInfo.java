package ch.ech.ech0020.v3;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.804311700")
public class MaritalInfo {
	public static final MaritalInfo $ = Keys.of(MaritalInfo.class);

	public final ch.ech.ech0011.MaritalData maritalData = new ch.ech.ech0011.MaritalData();
	public ch.ech.ech0021.MaritalDataAddon maritalDataAddon;
}