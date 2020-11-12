package ch.ech.ech0098;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class DwellingAddress {
	public static final DwellingAddress $ = Keys.of(DwellingAddress.class);

	@Size(9)
	public Integer EGID;
	@Size(3)
	public Integer EWID;
	public final ch.ech.ech0010.AddressInformation address = new ch.ech.ech0010.AddressInformation();
	public LocalDate movingDate;
}