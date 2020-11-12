package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventNaturalizeSwiss {
	public static final EventNaturalizeSwiss $ = Keys.of(EventNaturalizeSwiss.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification naturalizeSwissPerson;
	public List<PlaceOfOriginInfo> placeOfOriginInfo;
}