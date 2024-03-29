package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class TaxableResultIncrease {
	public static final TaxableResultIncrease $ = Keys.of(TaxableResultIncrease.class);

	@NotEmpty
	public TaxableResultIncreaseReason taxableResultIncreaseReason;
	@Size(400)
	public String description;
	public final TaxAmount amount = new TaxAmount();
	public CantonExtension cantonExtension;
}