package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.463")
public class TaxAdjustmentsDecreasingTaxableResult {
	public static final TaxAdjustmentsDecreasingTaxableResult $ = Keys.of(TaxAdjustmentsDecreasingTaxableResult.class);

	public Object id;
	public List<TaxableResultDecrease> taxableResultDecrease;
	public final TaxAmount totalTaxableResultDecrease = new TaxAmount();
	public CantonExtension cantonExtension;
}