package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.490260")
public class TaxLossCarriedForward {
	public static final TaxLossCarriedForward $ = Keys.of(TaxLossCarriedForward.class);

	@NotEmpty
	@Size(4)
	public Integer yearOfTaxLossCarriedForward;
	public final TaxAmount amountOfTaxLossCarriedForward = new TaxAmount();
	public CantonExtension cantonExtension;
}