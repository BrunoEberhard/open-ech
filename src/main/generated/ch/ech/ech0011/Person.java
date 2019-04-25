package ch.ech.ech0011;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fluttercode.datafactory.impl.DataFactory;
import org.minimalj.backend.Backend;
import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtil;
import org.minimalj.model.annotation.Materialized;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.repository.query.By;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0007.SwissMunicipality;
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
import ch.ech.ech0044.Sex;
import ch.ech.ech0071.Municipality;
import ch.openech.frontend.ech0011.ReligionFormElement;
import ch.openech.xml.YesNo;

//handmade
public class Person implements Mocking {
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
	// TODO das sollte abhängig von der Nationalität gesetzt sein
	public ResidencePermitData residencePermit = new ResidencePermitData();
	
	// PersonAddOn
	// ===========
	// Wird als eigenständige Entität nicht generiert. Ab ech0020 v3 sind diese
	// Felder in der Person integriert. Bei v2 mussten sie noch über ech0021
	// speziell übermittelt werden.
	
	public final PersonAdditionalData personAdditionalData = new PersonAdditionalData();
	public final PoliticalRightData politicalRightData = new PoliticalRightData();
	public final BirthAddonData birthAddonData = new BirthAddonData();
	public final DataLock dataLock = new DataLock();
	public final PaperLock paperLock = new PaperLock();
	public JobData jobData;
	public MaritalRelationship maritalRelationship;
	public List<ParentalRelationship> parentalRelationship;
	public List<GuardianRelationship> guardianRelationship;
	public final ArmedForcesData armedForcesData = new ArmedForcesData();
	public final CivilDefenseData civilDefenseData = new CivilDefenseData();
	public final FireServiceData fireServiceData = new FireServiceData();
	public final HealthInsuranceData healthInsuranceData = new HealthInsuranceData();
	public final MatrimonialInheritanceArrangementData matrimonialInheritanceArrangementData = new MatrimonialInheritanceArrangementData();

	// ReportedPerson
	// ==============

	public ResidenceData residenceData;

	public SwissMunicipality mainResidence;
	public List<SwissMunicipality> secondaryResidence;
	
	@Materialized
	@Size(60)
	public String getStreet() {
		if (residenceData != null) {
			return residenceData.dwellingAddress.address.street;
		} else {
			return null;
		}
	}

	@Materialized
	@Size(40)
	public String getTown() {
		if (residenceData != null) {
			return residenceData.dwellingAddress.address.town;
		} else {
			return null;
		}
	}

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

	public LockData getLockData() {
		boolean emptyDataLock = dataLock.dataLock == ch.ech.ech0021.DataLock._0;
		boolean emptyPaperLock = paperLock.paperLock == null || paperLock.paperLock == false;
		if (emptyDataLock && emptyPaperLock) {
			return null;
		} else {
			LockData d = new LockData();
			if (dataLock != null) {
				d.dataLock = dataLock.dataLock;
				d.dataLockValidFrom = dataLock.dataLockValidFrom;
				d.dataLockValidTill = dataLock.dataLockValidTill;
			} else {
				d.dataLock = ch.ech.ech0021.DataLock._0;
			}
			if (paperLock != null) {
				d.paperLock = Boolean.TRUE.equals(paperLock.paperLock) ? YesNo._1 : YesNo._0;
				d.paperLockValidFrom = paperLock.paperLockValidFrom;
				d.paperLockValidTill = paperLock.paperLockValidTill;
			} else {
				d.paperLock = YesNo._0;
			}
			return d;
		}
	}

	public void setDataLock(LockData d) {
		if (d == null) {
			d = EmptyObjects.getEmptyObject(LockData.class);
		}
		paperLock.paperLock = YesNo._1.equals(d.paperLock);
		paperLock.paperLockValidFrom = d.paperLockValidFrom;
		paperLock.paperLockValidTill = d.paperLockValidTill;
		dataLock.dataLock = d.dataLock;
		dataLock.dataLockValidFrom = d.dataLockValidFrom;
		dataLock.dataLockValidTill = d.dataLockValidTill;
	}

	@Override
	public void mock() {
		DataFactory df = new DataFactory();
		Random r = new Random();
		nameData.firstName = df.getFirstName();
		nameData.officialName = df.getLastName();
		birthData.dateOfBirth.value = LocalDate.now().minusDays(r.nextInt(10000)).toString();
		List<Municipality> municipalities = Backend.find(Municipality.class, By.all());
		Municipality municipality = municipalities.get(r.nextInt(municipalities.size()));
		birthData.placeOfBirth.swissTown = ViewUtil.view(municipality, new SwissMunicipality());
		birthData.sex = Sex._1;
		religionData.religion = String
				.valueOf(ReligionFormElement.RELIGION_VALUES[r.nextInt(ReligionFormElement.RELIGION_VALUES.length)]);

		personIdentification = new PersonIdentification();
		personIdentification.officialName = nameData.officialName;
		personIdentification.firstName = nameData.firstName;
		personIdentification.sex = birthData.sex;
		personIdentification.localPersonId.namedIdCategory = "OpenEch";
		personIdentification.localPersonId.namedId = "test";
		personIdentification.dateOfBirth.value = birthData.dateOfBirth.value;
	}

	public void render(StringBuilder s) {
		boolean empty = true;
		if (!StringUtils.isEmpty(nameData.firstName)) {
			s.append(nameData.firstName);
			empty = false;
		}
		if (!StringUtils.isEmpty(nameData.officialName)) {
			if (!empty) {
				s.append(' ');
			}
			s.append(nameData.officialName);
			empty = false;
		}
		if (!empty && residenceData != null) {
			String town = residenceData.dwellingAddress.address.town;
			if (!StringUtils.isEmpty(town)) {
				s.append(", ").append(town);
			}
		}
		if (!empty) {
			s.append('\n');
		}
	}
}