package ch.ech.ech0020.v3;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.546")
public class MaritalInfo {
	public static final MaritalInfo $ = Keys.of(MaritalInfo.class);

	public final ch.ech.ech0011.MaritalData maritalData = new ch.ech.ech0011.MaritalData();
	public ch.ech.ech0021.MaritalDataAddon maritalDataAddon;
}