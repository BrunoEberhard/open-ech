package ch.ech.ech0098;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Liquidation {
	public static final Liquidation $ = Keys.of(Liquidation.class);

	public ch.openech.model.DatePartiallyKnown liquidationDate;
	public ch.openech.model.DatePartiallyKnown liquidationStartDate;
	public LiquidationReason liquidationReason;
}