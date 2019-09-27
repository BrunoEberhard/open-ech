package ch.ech.ech0020.v3;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

//Handmade
public class EventBaseDelivery {
	public static final EventBaseDelivery $ = Keys.of(EventBaseDelivery.class);

	@NotEmpty
	public BaseDeliveryPerson baseDeliveryPerson;
	public ReportingMunicipality hasMainResidence;
	public ReportingMunicipality hasSecondaryResidence;
	public ReportingMunicipality hasOtherResidence;

	public LocalDate baseDeliveryValidFrom;
}