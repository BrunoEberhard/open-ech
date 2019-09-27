package ch.ech.ech0098;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.886")
public class Liquidation {
	public static final Liquidation $ = Keys.of(Liquidation.class);

	public ch.openech.xml.DatePartiallyKnown liquidationDate;
	public ch.openech.xml.DatePartiallyKnown liquidationStartDate;
	public LiquidationReason liquidationReason;
}