package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.947")
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