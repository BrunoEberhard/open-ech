package ch.ech.ech0011;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0008.Country;
import ch.ech.ech0011.NationalityData.CountryInfo;
import ch.ech.ech0021.ArmedForcesData;
import ch.ech.ech0021.BirthAddonData;
import ch.ech.ech0021.CivilDefenseData;
import ch.ech.ech0021.FireServiceData;
import ch.ech.ech0021.GuardianRelationship;
import ch.ech.ech0021.HealthInsuranceData;
import ch.ech.ech0021.JobData;
import ch.ech.ech0021.LockData;
import ch.ech.ech0021.MaritalRelationship;
import ch.ech.ech0021.MatrimonialInheritanceArrangementData;
import ch.ech.ech0021.ParentalRelationship;
import ch.ech.ech0021.PersonAdditionalData;
import ch.ech.ech0021.PlaceOfOriginAddon;
import ch.ech.ech0021.PoliticalRightData;
import ch.ech.ech0044.PersonIdentification;

//handmade
// Die PersonAddon - Felder können auch gleich hier untergebracht werden
public class Person {
	public static final Person $ = Keys.of(Person.class);

	public Object id;
	@NotEmpty
	public PersonIdentification personIdentification;
	public final NameData nameData = new NameData();
	public final BirthData birthData = new BirthData();
	public final ReligionData religionData = new ReligionData();
	public final MaritalData maritalData = new MaritalData();
	public final NationalityData nationalityData = new NationalityData();
	public DeathData deathData;
	public ContactData contactData;
	public List<PlaceOfOriginAddon> placeOfOrigin; // PlaceOfOriginAddon, nicht nur PlaceOfOrigin
	public ResidencePermitData residencePermit;
	
	// PersonAddOn
	
	public final PersonAdditionalData personAdditionalData = new PersonAdditionalData();
	public PoliticalRightData politicalRightData;
	public BirthAddonData birthAddonData;
	public LockData lockData;
	public JobData jobData;
	public MaritalRelationship maritalRelationship;
	public List<ParentalRelationship> parentalRelationship;
	public List<GuardianRelationship> guardianRelationship;
	public ArmedForcesData armedForcesData;
	public CivilDefenseData civilDefenseData;
	public FireServiceData fireServiceData;
	public HealthInsuranceData healthInsuranceData;
	public MatrimonialInheritanceArrangementData matrimonialInheritanceArrangementData;
	
	//

	public String getLanguageOfCorrespondance() {
		return personAdditionalData.languageOfCorrespondance;
	}

	public void setLanguageOfCorrespondance(String languageOfCorrespondance) {
		personAdditionalData.languageOfCorrespondance = languageOfCorrespondance;
	}

	public Boolean getRestrictedVotingAndElectionRightFederation() {
		return politicalRightData.restrictedVotingAndElectionRightFederation;
	}

	public void setRestrictedVotingAndElectionRightFederation(Boolean restrictedVotingAndElectionRightFederation) {
		politicalRightData.restrictedVotingAndElectionRightFederation = restrictedVotingAndElectionRightFederation;
	}

	// for old ech 11 versions
	
	private final Coredata coredata = new Coredata();
	@Size(100)
	public String alliancePartnershipName;

	public Coredata getCoredata() {
		return coredata;
	}

	public class Coredata {
		public String getOriginalName() {
			return nameData.originalName;
		}
		
		public void setOriginalName(String originalName) {
			nameData.originalName = originalName;
		}

		public String getAlliancePartnershipName() {
			return alliancePartnershipName;
		}

		public void setAlliancePartnershipName(String name) {
			alliancePartnershipName = name;
		}

		public String getAliasName() {
			return nameData.aliasName;
		}

		public void setAliasName(String aliasName) {
			nameData.aliasName = aliasName;
		}

		public String getOtherName() {
			return nameData.otherName;
		}

		public void setOtherName(String otherName) {
			nameData.otherName = otherName;
		}

		public String getCallName() {
			return nameData.callName;
		}

		public void setCallName(String callName) {
			nameData.callName = callName;
		}

		public Birthplace getPlaceOfBirth() {
			return placeOfBirth;
		}
		
		public LocalDate getDateOfDeath() {
			return deathData != null ? deathData.deathPeriod.dateFrom : null;
		}
	
		public void setDateOfDeath(LocalDate date) {
			if (date == null) {
				deathData = null;
			} else {
				if (deathData == null) {
					deathData = new DeathData();
				}
				deathData.deathPeriod.dateFrom = date;
			}
		}

		public MaritalData getMaritalData() {
			return maritalData;
		}
		
		public Nationality getNationality() {
			return nationality;
		}
		
		public ContactData getContact() {
			return contactData;
		}
		
		public String getLanguageOfCorrespondance() {
			return personAdditionalData.languageOfCorrespondance;
		}

		public void setLanguageOfCorrespondance(String languageOfCorrespondance) {
			personAdditionalData.languageOfCorrespondance = languageOfCorrespondance;
		}
		
		public String getReligion() {
			return religionData.religion;
		}
		
		public void setReligion(String religion) {
			religionData.religion = religion;
		}
	}
	
	private final Birthplace placeOfBirth = new Birthplace();
	
	public class Birthplace {

		private final SwissTown swissTown = new SwissTown();
		private final ForeignCountry foreignCountry = new ForeignCountry();

		public class SwissTown {

			private final Country swiss = new Country();

			public ch.ech.ech0008.Country getCountry() {
				return swiss;
			}

			public ch.ech.ech0007.SwissMunicipality getMunicipality() {
				return birthData.placeOfBirth.swissTown;
			}
		}

		public class ForeignCountry {

			public Country getCountry() {
				return birthData.placeOfBirth.foreignCountry.country;
			}

			public void setCountry(Country country) {
				CloneHelper.deepCopy(country, birthData.placeOfBirth.foreignCountry.country);
			}
			
			public String getForeignBirthTown() {
				return birthData.placeOfBirth.foreignCountry.town;
			}

			public void setForeignBirthTown(String foreignBirthTown) {
				birthData.placeOfBirth.foreignCountry.town = foreignBirthTown;
			}
		}

		public Unknown getUnknown() {
			return birthData.placeOfBirth.unknown;
		}

		public SwissTown getSwissTown() {
			return swissTown;
		}

		public ForeignCountry getForeignCountry() {
			return foreignCountry;
		}
	}
	
	private final Nationality nationality = new Nationality();
	
	public class Nationality {
		
		public NationalityStatus getNationalityStatus() {
			return nationalityData.nationalityStatus;
		}
		public void setNationalityStatus(NationalityStatus nationalityStatus) {
			nationalityData.nationalityStatus = nationalityStatus;
		}
		
		public Country getCountry() {
			if (nationalityData.countryInfo != null && !nationalityData.countryInfo.isEmpty()) {
				return nationalityData.countryInfo.get(0).country;
			} else {
				return null;
			}
		}
		
		public void setCountry(Country country) {
			if (country == null) {
				nationalityData.countryInfo = null;
			} else {
				nationalityData.countryInfo = new ArrayList<>();
				CountryInfo countryInfo = new CountryInfo();
				CloneHelper.deepCopy(country, countryInfo.country);
				nationalityData.countryInfo.add(countryInfo);
			}
		}
	}
}