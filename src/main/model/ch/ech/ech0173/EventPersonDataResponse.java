package ch.ech.ech0173;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.488")
public class EventPersonDataResponse {
	public static final EventPersonDataResponse $ = Keys.of(EventPersonDataResponse.class);

	public Object id;
	@NotEmpty
	public PersonDataResult personDataResult;
	public static class PersonDataIndividual {
		public static final PersonDataIndividual $ = Keys.of(PersonDataIndividual.class);

		public ch.ech.ech0044.PersonIdentification personidentification;
		public static class BirthData {
			public static final BirthData $ = Keys.of(BirthData.class);

			public ch.openech.xml.DatePartiallyKnown dateOfBirth;
			public ch.ech.ech0011.GeneralPlace placeOfBirth;
		}
		public BirthData birthData;
		public static class DeathData {
			public static final DeathData $ = Keys.of(DeathData.class);

			public ch.ech.ech0011.DeathPeriod deathPeriod;
			public ch.ech.ech0011.GeneralPlace placeOfDeath;
		}
		public DeathData deathData;
		public static class NameData {
			public static final NameData $ = Keys.of(NameData.class);

			@NotEmpty
			@Size(100)
			public String officialName;
			@NotEmpty
			@Size(100)
			public String firstName;
			@Size(100)
			public String originalName;
			@Size(100)
			public String allianceName;
			@Size(100)
			public String aliasName;
			@Size(100)
			public String otherName;
			@Size(100)
			public String callName;
			public ch.ech.ech0011.ForeignerName nameOnForeignPassport;
			public ch.ech.ech0011.ForeignerName declaredForeignName;
			public LocalDate nameValidFrom;
		}
		public NameData nameData;
		public ch.ech.ech0011.MaritalData maritalData;
		public ch.ech.ech0011.ReligionData religionData;
		public ch.ech.ech0011.NationalityData nationalityData;
		public ch.ech.ech0011.ResidencePermitData residencePermitData;
		public ch.ech.ech0021.MaritalRelationship maritalRelationship;
		public List<ch.ech.ech0021.ParentalRelationship> parentalRelationship;
		public List<ch.ech.ech0021.GuardianRelationship> guardianRelationship;
		public static class OriginAddon {
			public static final OriginAddon $ = Keys.of(OriginAddon.class);

			public ch.ech.ech0011.PlaceOfOrigin placeOfOrigin;
			public ch.ech.ech0021.PlaceOfOriginAddonData placeOfOriginAddon;
		}
		public List<OriginAddon> originAddon;

		public enum KindOfEmployment { _0, _1, _2, _3, _4;		}
		public KindOfEmployment kindOfEmployment;
		public ch.openech.xml.YesNo paperLock;
		public ch.ech.ech0021.DataLock dataLock;
		@Size(2)
		public String languageOfCorrespondance;
	}
	public PersonDataIndividual personDataIndividual;
	public Organisation personDataOrganisation;
	public static class MainResidenceData {
		public static final MainResidenceData $ = Keys.of(MainResidenceData.class);

		public ch.ech.ech0007.SwissMunicipality mainResidenceMunicipality;
		public ch.ech.ech0011.DwellingAddress dwellingAddress;
		public static class BusinessAddress {
			public static final BusinessAddress $ = Keys.of(BusinessAddress.class);

			@Size(9)
			public Integer EGID;
			@Size(3)
			public Integer EWID;
			public final ch.ech.ech0010.AddressInformation address = new ch.ech.ech0010.AddressInformation();
			public LocalDate movingDate;
		}
		public BusinessAddress businessAddress;
		public ch.ech.ech0010.AddressInformation mailAddress;
		public ch.ech.ech0011.Destination comesFrom;
		public LocalDate arrivalDate;
		public ch.ech.ech0011.Destination goesTo;
		public LocalDate departureDate;
	}
	public MainResidenceData mainResidenceData;
	public static class SecondaryResidenceData {
		public static final SecondaryResidenceData $ = Keys.of(SecondaryResidenceData.class);

		public ch.ech.ech0007.SwissMunicipality secondaryResidenceMunicipality;
		public ch.ech.ech0011.DwellingAddress dwellingAddress;
		public static class BusinessAddress_ {
			public static final BusinessAddress_ $ = Keys.of(BusinessAddress_.class);

			@Size(9)
			public Integer EGID;
			@Size(3)
			public Integer EWID;
			public final ch.ech.ech0010.AddressInformation address = new ch.ech.ech0010.AddressInformation();
			public LocalDate movingDate;
		}
		public BusinessAddress_ businessAddress;
		public ch.ech.ech0010.AddressInformation mailAddress;
		public ch.ech.ech0011.Destination comesFrom;
		public LocalDate arrivalDate;
		public ch.ech.ech0011.Destination goesTo;
		public LocalDate departureDate;
	}
	public List<SecondaryResidenceData> secondaryResidenceData;
	public byte[] extension;
}