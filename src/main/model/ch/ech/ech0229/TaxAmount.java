package ch.ech.ech0229;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.487260100")
public class TaxAmount {
	public static final TaxAmount $ = Keys.of(TaxAmount.class);

	public Long cantonalTax;
	public Long federalTax;
	public CantonExtension cantonExtension;
}