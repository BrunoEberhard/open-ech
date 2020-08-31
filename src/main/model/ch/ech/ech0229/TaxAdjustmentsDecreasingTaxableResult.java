package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.489261100")
public class TaxAdjustmentsDecreasingTaxableResult {
	public static final TaxAdjustmentsDecreasingTaxableResult $ = Keys.of(TaxAdjustmentsDecreasingTaxableResult.class);

	public Object id;
	public List<TaxableResultDecrease> taxableResultDecrease;
	public final TaxAmount totalTaxableResultDecrease = new TaxAmount();
	public CantonExtension cantonExtension;
}