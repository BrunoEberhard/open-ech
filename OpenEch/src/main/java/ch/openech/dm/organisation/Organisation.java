package ch.openech.dm.organisation;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.DatePartially;
import ch.openech.dm.EchFormats;
import ch.openech.dm.Event;
import ch.openech.dm.code.TypeOfResidenceOrganisation;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.edit.value.PropertyAccessor;
import ch.openech.mj.edit.value.Required;

public class Organisation {

	public static final Organisation ORGANISATION = Constants.of(Organisation.class);
	
	// Der eCH - Event, mit dem die aktuelle Version erstellt oder verändert wurde
	public Event event;
	
	// 97 : Identification
	@Is(EchFormats.uidStructure)
	public String uid; // ADM000000001 - CHE999999999
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Is(EchFormats.organisationName)
	public String organisationName; 
	@Is(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	public String legalForm;
	
	// 98 : Daten
	
	public String uidBrancheText;
	public String nogaCode; // 00 - 999999
	
	@Is(DatePartially) @Required
	public String foundationDate;
	public String foundationReason;
	
	@Is(Date)
	public String liquidationEntryDate;
	@Is(DatePartially)
	public String liquidationDate;
	public String liquidationReason;
	
	@Is("language")
	public String languageOfCorrespondance;
	
	// 108 : Informationen andere Register
	
	// uidregInformationType
	public String uidregStatusEnterpriseDetail; // 1-7
	public String uidregPublicStatus; // 0-1
	public String uidregOrganisationType; // 1-12
	public String uidregLiquidationReason; // 1-8
	
	@Is(EchFormats.uidStructure)
	public String uidregSourceUid; // ADM000000001 - CHE999999999
	
	// commercialRegisterInformation
	public String commercialRegisterStatus; // 1-3
	public String commercialRegisterEntryStatus; // 1-2
	@Is(EchFormats.organisationName)
	public String commercialRegisterNameTranslation;
	@Is(Date)
	public String commercialRegisterEntryDate, commercialRegisterLiquidationDate;
	
	// vatRegisterInformation
	public String vatStatus; // 1-3
	public String vatEntryStatus; // 1-2
	@Is(Date)
	public String vatEntryDate, vatLiquidationDate;
	
	// if reported (gemeldet)
	public String typeOfResidenceOrganisation = "1";
	// Achtung: Im Gegensatz zu einer Person kann eine Organisation nur einen primary, secondary oder other Eintrag haben
	//          Die folgenden Felder sind dabei bei allen 3 Möglichkeiten *genau* gleich.
	public MunicipalityIdentification reportingMunicipality;
	@Is(Date) @Required
	public String arrivalDate;
	public Place comesFrom;
	public DwellingAddress businessAddress;
	@Is(Date)
	public String departureDate;
	public Place goesTo;
	
	// secondaryResidence / otherResidence
	public Organisation headquarterOrganisation;

	public Contact contact;
	
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
