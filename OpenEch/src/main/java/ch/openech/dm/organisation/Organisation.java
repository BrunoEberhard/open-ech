package ch.openech.dm.organisation;

import ch.openech.dm.EchFormats;
import ch.openech.dm.Event;
import ch.openech.dm.code.TypeOfResidenceOrganisation;
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
	
	// Der eCH - Event, mit dem die aktuelle Version erstellt oder verändert wurde
	public Event event;
	
	// 97 : Identification
	@FormatName(EchFormats.uidStructure)
	public String uid; // ADM000000001 - CHE999999999
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@FormatName(EchFormats.organisationName)
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
	
	@FormatName(EchFormats.uidStructure)
	public String uidregSourceUid; // ADM000000001 - CHE999999999
	
	// commercialRegisterInformation
	@Required
	public String commercialRegisterStatus; // 1-3
	public String commercialRegisterEntryStatus; // 1-2
	@FormatName(EchFormats.organisationName)
	public String commercialRegisterNameTranslation;
	@Date
	public String commercialRegisterEntryDate, commercialRegisterLiquidationDate;
	
	// vatRegisterInformation
	@Required
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
	
	// secondaryResidence / otherResidence
	public Organisation headquarterOrganisation;

	//
	
	public Object get(String propertyName) {
		return PropertyAccessor.get(this, propertyName);
	}

	public void set(String propertyName, Object value) {
		PropertyAccessor.set(this, propertyName, value);
	}
	
	//
	
	public String getId() {
		if (technicalIds.localId.isOpenEch()) {
			return technicalIds.localId.personId;
		} else {
			return null;
		}
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
