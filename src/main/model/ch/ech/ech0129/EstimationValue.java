package ch.ech.ech0129;

import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.034")
public class EstimationValue {
	public static final EstimationValue $ = Keys.of(EstimationValue.class);

	public final ch.openech.xml.NamedId localID = new ch.openech.xml.NamedId();
	@Size(4)
	public Integer baseYear;
	public LocalDate validFrom;
	@Size(6)
	public BigDecimal indexValue;
	public final Value value = new Value();
	@NotEmpty
	public TypeOfvalue typeOfvalue;
}