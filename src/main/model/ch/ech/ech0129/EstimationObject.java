package ch.ech.ech0129;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EstimationObject {
	public static final EstimationObject $ = Keys.of(EstimationObject.class);

	public Object id;
	public final ch.openech.model.NamedId localID = new ch.openech.model.NamedId();
	@Size(7)
	public Integer volume;
	@Size(4)
	public Integer yearOfConstruction;
	@Size(1000)
	public String description;
	public LocalDate validFrom;
	@Size(30)
	public String estimationReason;
	public List<EstimationValue> estimationValue;
}