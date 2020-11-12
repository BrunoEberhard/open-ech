package ch.ech.ech0011;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class BirthData {
	public static final BirthData $ = Keys.of(BirthData.class);

	public final ch.openech.model.DatePartiallyKnown dateOfBirth = new ch.openech.model.DatePartiallyKnown();
	public final GeneralPlace placeOfBirth = new GeneralPlace();
	@NotEmpty
	public ch.ech.ech0044.Sex sex;
}