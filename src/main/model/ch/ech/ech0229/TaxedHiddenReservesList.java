package ch.ech.ech0229;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.493260200")
public class TaxedHiddenReservesList {
	public static final TaxedHiddenReservesList $ = Keys.of(TaxedHiddenReservesList.class);

	public Object id;
	public List<TaxedHiddenReserve> taxedHiddenReserve;
	public TaxAmount totalTaxedHiddenReserves;
	public CantonExtension cantonExtension;
}