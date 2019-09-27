package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.671")
public class EventUndoSwiss {
	public static final EventUndoSwiss $ = Keys.of(EventUndoSwiss.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification undoSwissPerson;
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
	public ch.ech.ech0011.ResidencePermitData residencePermitData;
	public LocalDate undoSwissValidFrom;
}