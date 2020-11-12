package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class TaxedHiddenReservesList {
	public static final TaxedHiddenReservesList $ = Keys.of(TaxedHiddenReservesList.class);

	public Object id;
	public List<TaxedHiddenReserve> taxedHiddenReserve;
	public TaxAmount totalTaxedHiddenReserves;
	public CantonExtension cantonExtension;
}