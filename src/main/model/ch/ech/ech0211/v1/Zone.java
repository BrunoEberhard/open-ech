package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.466260600")
public class Zone {
	public static final Zone $ = Keys.of(Zone.class);

	@Size(25)
	public String abbreviatedDesignation;
	@NotEmpty
	@Size(255)
	public String zoneDesignation;
	@Size(255)
	public String zoneType;
}