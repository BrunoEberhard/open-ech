package ch.ech.ech0098;

import org.minimalj.model.Keys;

// AddressNames... isses das?
public class SwissHeadquarter {
	public static final SwissHeadquarter $ = Keys.of(SwissHeadquarter.class);

	public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
	public ch.ech.ech0007.SwissMunicipality headquarterMunicipality;
	public static class BusinessAddress {
		public static final BusinessAddress $ = Keys.of(BusinessAddress.class);

		public ch.ech.ech0010.MailAddress.AddressNames organisation;
		public ch.ech.ech0010.MailAddress.AddressNames person;
		public final ch.ech.ech0010.AddressInformation addressInformation = new ch.ech.ech0010.AddressInformation();
	}
	public BusinessAddress businessAddress;
}