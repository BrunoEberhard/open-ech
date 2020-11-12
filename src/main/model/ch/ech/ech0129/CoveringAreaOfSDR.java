package ch.ech.ech0129;

import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class CoveringAreaOfSDR {
	public static final CoveringAreaOfSDR $ = Keys.of(CoveringAreaOfSDR.class);

	@NotEmpty
	@Size(12)
	public BigDecimal squareMeasure;
	public final RealestateIdentification realestateIdentification = new RealestateIdentification();
}