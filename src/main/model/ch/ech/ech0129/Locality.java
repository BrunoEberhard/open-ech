package ch.ech.ech0129;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class Locality {
	public static final Locality $ = Keys.of(Locality.class);

	public Object id;

	@Size(4)
	public Integer swissZipCode;
	@Size(2)
	public String swissZipCodeAddOn;
	public final LocalityName name = new LocalityName();
}