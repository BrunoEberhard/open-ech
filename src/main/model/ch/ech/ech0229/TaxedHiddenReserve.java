package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.493260200")
public class TaxedHiddenReserve {
	public static final TaxedHiddenReserve $ = Keys.of(TaxedHiddenReserve.class);

	@NotEmpty
	public NameOfTaxedHiddenReserve nameOfTaxedHiddenReserve;
	@Size(400)
	public String description;
	public TaxAmount valueAtStartOfPeriod;
	public Long changeOfValue;
	public final TaxAmount valueAtEndOfPeriod = new TaxAmount();
	public CantonExtension cantonExtension;
}