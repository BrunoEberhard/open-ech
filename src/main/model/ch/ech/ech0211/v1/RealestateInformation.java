package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.469260400")
public class RealestateInformation {
	public static final RealestateInformation $ = Keys.of(RealestateInformation.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0129.Realestate realestate;
	public final ch.ech.ech0007.SwissMunicipality municipality = new ch.ech.ech0007.SwissMunicipality();
	public List<BuildingInformation> buildingInformation;
	public ch.ech.ech0129.PlaceName placeName;
	public static class Owner {
		public static final Owner $ = Keys.of(Owner.class);

		public static class OwnerIdentification {
			public static final OwnerIdentification $ = Keys.of(OwnerIdentification.class);

			public ch.ech.ech0044.PersonIdentification personIdentification;
			public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
		}
		public OwnerIdentification ownerIdentification;
		public final ch.ech.ech0010.MailAddress ownerAdress = new ch.ech.ech0010.MailAddress();
	}
	public List<Owner> owner;
}