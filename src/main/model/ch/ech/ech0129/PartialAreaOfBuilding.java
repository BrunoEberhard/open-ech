package ch.ech.ech0129;

import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.988")
public class PartialAreaOfBuilding {
	public static final PartialAreaOfBuilding $ = Keys.of(PartialAreaOfBuilding.class);

	@Size(12)
	public BigDecimal squareMeasure;
}