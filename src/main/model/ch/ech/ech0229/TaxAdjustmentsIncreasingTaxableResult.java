package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.489261100")
public class TaxAdjustmentsIncreasingTaxableResult {
	public static final TaxAdjustmentsIncreasingTaxableResult $ = Keys.of(TaxAdjustmentsIncreasingTaxableResult.class);

	public Object id;
	public List<TaxableResultIncrease> taxableResultIncrease;
	public final TaxAmount totalTaxableResultIncrease = new TaxAmount();
	public CantonExtension cantonExtension;
}