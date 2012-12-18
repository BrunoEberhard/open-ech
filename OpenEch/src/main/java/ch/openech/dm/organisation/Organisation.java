package ch.openech.dm.organisation;

import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.dm.EchFormats;
import ch.openech.dm.Event;
import ch.openech.dm.code.TypeOfResidenceOrganisation;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.dm.contact.Contact;
import ch.openech.dm.organisation.types.UidregStatusEnterpriseDetail;
import ch.openech.dm.types.Language;
import ch.openech.mj.db.model.ColumnProperties;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.EmptyValidator;
import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.model.annotation.PartialDate;
import ch.openech.mj.model.annotation.Size;

public class Organisation implements Validation {

	public static final Organisation ORGANISATION = Constants.of(Organisation.class);
	
	public static enum EditMode { DISPLAY, BASE_DELIVERY, MOVE_IN, FOUNDATION, CHANGE_RESIDENCE_TYPE, IN_LIQUIDATION, LIQUIDATION, CHANGE_REPORTING }
	
	public EditMode editMode;
	
	// Der eCH - Event, mit dem die aktuelle Version erstellt oder verändert wurde
	public Event event;
	
	// 97 : Identification
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	@Size(2) // TODO
	public String legalForm;
	
	// 98 : Daten
	
	@Size(2) // TODO
	public String uidBrancheText;
	@Size(6) // TODO
	public String nogaCode; // 00 - 999999
	
	@Required @PartialDate
	public LocalDate foundationDate;
	@Size(2) // TODO
	public String foundationReason;
	
	public LocalDate liquidationEntryDate;
	@PartialDate
	public LocalDate liquidationDate;
	@Size(2) // TODO
	public String liquidationReason;
	
	public Language languageOfCorrespondance;
	
	// 108 : Informationen andere Register
	
	// uidregInformationType
	public UidregStatusEnterpriseDetail uidregStatusEnterpriseDetail;
	@Size(2) // TODO
	public String uidregPublicStatus; // 0-1
	@Size(2) // TODO
	public String uidregOrganisationType; // 1-12
	@Size(2) // TODO
	public String uidregLiquidationReason; // 1-8
	
	public final UidStructure uidregSourceUid = new UidStructure(); 
	
	// commercialRegisterInformation
	@Size(2) // TODO
	public String commercialRegisterStatus; // 1-3
	@Size(2) // TODO
	public String commercialRegisterEntryStatus; // 1-2
	@Size(EchFormats.organisationName)
	public String commercialRegisterNameTranslation;
	public LocalDate commercialRegisterEntryDate, commercialRegisterLiquidationDate;
	
	// vatRegisterInformation
	@Size(2) // TODO
	public String vatStatus; // 1-3
	@Size(2) // TODO
	public String vatEntryStatus; // 1-2
	public LocalDate vatEntryDate, vatLiquidationDate;
	
	// if reported (gemeldet)
	public TypeOfResidenceOrganisation typeOfResidenceOrganisation = TypeOfResidenceOrganisation.Hauptsitz;
	// Achtung: Im Gegensatz zu einer Person kann eine Organisation nur einen primary, secondary oder other Eintrag haben
	//          Die folgenden Felder sind dabei bei allen 3 Möglichkeiten *genau* gleich.
	@Required
	public MunicipalityIdentification reportingMunicipality;
	@Required
	public LocalDate arrivalDate;
	public Place comesFrom;
	public DwellingAddress businessAddress;
	public LocalDate departureDate;
	public Place goesTo;
	
	// secondaryResidence / otherResidence
	public Organisation headquarterOrganisation;

	public Contact contact;
	
	//
	
	public Object get(String propertyName) {
		return ColumnProperties.getValue(this, propertyName);
	}

	public void set(String propertyName, Object value) {
		ColumnProperties.setValue(this, propertyName, value);
	}
	
	//
	
	public String getId() {
		if (technicalIds.localId.openEch()) {
			return technicalIds.localId.personId;
		} else {
			return null;
		}
	}
	
	public boolean hasMainResidence() {
		return TypeOfResidenceOrganisation.Hauptsitz == typeOfResidenceOrganisation;
	}

	public boolean hasSecondaryResidence() {
		return TypeOfResidenceOrganisation.Nebensitz == typeOfResidenceOrganisation;
	}

	public boolean hasOtherResidence() {
		return TypeOfResidenceOrganisation.Anderersitz == typeOfResidenceOrganisation;
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (editMode == EditMode.IN_LIQUIDATION || editMode == EditMode.LIQUIDATION) {
			EmptyValidator.validate(resultList, this, Organisation.ORGANISATION.liquidationReason);
			if (editMode == EditMode.IN_LIQUIDATION) {
				EmptyValidator.validate(resultList, this, Organisation.ORGANISATION.liquidationEntryDate);
			} else {
				EmptyValidator.validate(resultList, this, Organisation.ORGANISATION.liquidationDate);
			}
		} else if (editMode != null) {
			EmptyValidator.validate(resultList, this, Organisation.ORGANISATION.businessAddress);
			if (editMode == EditMode.CHANGE_REPORTING) {
				EmptyValidator.validate(resultList, this, Organisation.ORGANISATION.typeOfResidenceOrganisation);
			} else {
				EmptyValidator.validate(resultList, this, Organisation.ORGANISATION.arrivalDate);
			}
		}
	}
	
	

}
