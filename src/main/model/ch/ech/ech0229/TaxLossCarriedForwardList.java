package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class TaxLossCarriedForwardList {
	public static final TaxLossCarriedForwardList $ = Keys.of(TaxLossCarriedForwardList.class);

	public Object id;
	public List<TaxLossCarriedForward> taxLossCarriedForward;
	public final TaxAmount totalTaxLossCarriedForward = new TaxAmount();
	public CantonExtension cantonExtension;
}