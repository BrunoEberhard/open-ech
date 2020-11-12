package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class TaxAdjustmentsIncreasingTaxableResult {
	public static final TaxAdjustmentsIncreasingTaxableResult $ = Keys.of(TaxAdjustmentsIncreasingTaxableResult.class);

	public Object id;
	public List<TaxableResultIncrease> taxableResultIncrease;
	public final TaxAmount totalTaxableResultIncrease = new TaxAmount();
	public CantonExtension cantonExtension;
}