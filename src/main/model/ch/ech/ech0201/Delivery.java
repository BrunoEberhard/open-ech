package ch.ech.ech0201;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.838")
public class Delivery {
	public static final Delivery $ = Keys.of(Delivery.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0058.Header deliveryHeader;
	public List<ReportedPerson> reportedPerson;
	public final String version = "1.0";
}