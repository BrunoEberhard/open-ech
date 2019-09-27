package ch.ech.ech0020.v3;

import java.util.List;
import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.624")
public class EventDataRequest {
	public static final EventDataRequest $ = Keys.of(EventDataRequest.class);

	public Object id;
	public List<ch.ech.ech0044.PersonIdentification> dataRequestPerson;
	public ch.ech.ech0007.SwissMunicipality municipality;
	public LocalDate dataValidFrom;
}