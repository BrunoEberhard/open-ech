package ch.openech.dm.organisation;

import ch.openech.dm.Event;
import ch.openech.dm.code.TypeOfResidenceOrganisation;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.value.PropertyAccessor;
import ch.openech.mj.edit.value.Required;

public class Organisation {

	public static final Organisation ORGANISATION = Constants.of(Organisation.class);
	
	public int id;
	
	// Der eCH - Event, mit dem die aktuelle Version erstellt oder verändert wurde
	public Event event;
	
	// 97 : Identification
	@FormatName("uidStructure")
	public String uid; // ADM000000001 - CHE999999999
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@FormatName("organisationName")
	public String organisationName, organisationLegalName, organisationAdditionalName;
	public String legalForm;
	
	// 98 : Daten
	
	public String uidBrancheText;
	public String nogaCode; // 00 - 999999
	
	@Date(partialAllowed = true) @Required
	public String foundationDate;
	public String foundationReason;
	
	@Date(partialAllowed = true)
	public String liquidationDate;
	public String liquidationReason;
	
	@FormatName("language")
	public String languageOfCorrespondance;
	
	// 108 : Informationen andere Register
	
	// uidregInformationType
	public String uidregStatusEnterpriseDetail; // 1-7
	public String uidregPublicStatus; // 0-1
	public String uidregOrganisationType; // 1-12
	public String uidregLiquidationReason; // 1-8
	
	@FormatName("uidStructure")
	public String uidregSourceUid; // ADM000000001 - CHE999999999
	
	// commercialRegisterInformation
	public String commercialRegisterStatus; // 1-3
	public String commercialRegisterEntryStatus; // 1-2
	@FormatName("organisationName")
	public String commercialRegisterNameTranslation;
	public String commercialRegisterEntryDate;
	public String commercialRegisterLiquidationDate;
	
	// vatRegisterInformation
	public String vatStatus; // 1-3
	public String vatEntryStatus; // 1-2
	@Date
	public String vatEntryDate, vatLiquidationDate;
	
	// if reported (gemeldet)
	public String typeOfResidenceOrganisation = "1";
	// Achtung: Im Gegensatz zu einer Person kann eine Organisation nur einen primary, secondary oder other Eintrag haben
	//          Die folgenden Felder sind dabei bei allen 3 Möglichkeiten *genau* gleich.
	public MunicipalityIdentification reportingMunicipality;
	@Date @Required
	public String arrivalDate;
	public Place comesFrom;
	public DwellingAddress businessAddress;
	@Date
	public String departureDate;
	public Place goesTo;
	
	// secondaryResidence
	public Organisation headquarterOrganisation;
	public MunicipalityIdentification headquarterMunicipality;
	
	// secondaryResidence / otherResidence
	public Address headquarterAddress; // swiss or foreign

	//
	
	public Object get(String propertyName) {
		return PropertyAccessor.get(this, propertyName);
	}

	public void set(String propertyName, Object value) {
		PropertyAccessor.set(this, propertyName, value);
	}
	
	public boolean hasMainResidence() {
		return TypeOfResidenceOrganisation.Hauptsitz.getKey().equals(typeOfResidenceOrganisation);
	}

	public boolean hasSecondaryResidence() {
		return TypeOfResidenceOrganisation.Nebensitz.getKey().equals(typeOfResidenceOrganisation);
	}

	public boolean hasOtherResidence() {
		return TypeOfResidenceOrganisation.Anderersitz.getKey().equals(typeOfResidenceOrganisation);
	}

}
