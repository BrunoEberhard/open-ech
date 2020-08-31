package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.488260400")
public class DisclosedReserves {
	public static final DisclosedReserves $ = Keys.of(DisclosedReserves.class);

	@NotEmpty
	public DisclosedReserveDescription disclosedReserveDescription;
	@Size(400)
	public String description;
	public final TaxAmount amount = new TaxAmount();
	public CantonExtension cantonExtension;
}