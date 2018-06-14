package ch.ech.ech0020.v3;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// Handmade
// Die Aufteilung in die verschiedenen Residence Typen kann weggelassen werden,
// wenn ReportingMunicipality um mainResidence und secondaryResidence erweitert wird
public class EventMoveIn {
	public static final EventMoveIn $ = Keys.of(EventMoveIn.class);

	@NotEmpty
	public BaseDeliveryPerson moveInPerson;
	public ReportingMunicipality hasMainResidence;
	public ReportingMunicipality hasSecondaryResidence;
	public ReportingMunicipality hasOtherResidence;
}