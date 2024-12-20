package ch.ech.ech0011;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.fluttercode.datafactory.impl.DataFactory;
import org.minimalj.backend.Backend;
import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtils;
import org.minimalj.model.annotation.Materialized;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.repository.query.By;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0021.ArmedForcesData;
import ch.ech.ech0021.BirthAddonData;
import ch.ech.ech0021.CivilDefenseData;
import ch.ech.ech0021.FireServiceData;
import ch.ech.ech0021.GuardianRelationship;
import ch.ech.ech0021.HealthInsuranceData;
import ch.ech.ech0021.JobData;
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

//handmade

// Ziel: ech11 als Basis für DB verwenden. Daher so aufgeräumt
// wie möglich. ech20 kann mal neue Version bekommen, die Entitäten
// müssen nicht perfekt, sondern so originalgetreu wie möglich sein.

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
	// Folgende Felder sind nicht für die XML Serialisierung bestimmt.
	// Sie werden nur in der DB verwendet.

	@NotEmpty
	public ResidenceType residenceType = ResidenceType.PRIMARY;
	public ResidenceData residenceData; // enthält SwissMunicipality
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


	@Override
	public void mock() {
		DataFactory df = new DataFactory();
		Random r = new Random();
		nameData.firstName = df.getFirstName();
		nameData.officialName = df.getLastName();
		birthData.dateOfBirth.value = LocalDate.now().minusDays(r.nextInt(10000)).toString();
		List<Municipality> municipalities = Backend.find(Municipality.class, By.all());
		Municipality municipality = municipalities.get(r.nextInt(municipalities.size()));
		birthData.placeOfBirth.swissTown = ViewUtils.view(municipality, new SwissMunicipality());
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