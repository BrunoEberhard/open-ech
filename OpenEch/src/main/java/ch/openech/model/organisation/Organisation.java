package  ch.openech.model.organisation;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.joda.time.LocalDate;
import org.joda.time.ReadablePartial;
import org.joda.time.format.ISODateTimeFormat;
import org.minimalj.model.Codes;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.EchFormats;
import ch.openech.model.Event;
import ch.openech.model.code.TypeOfResidenceOrganisation;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.types.Language;
import ch.openech.xml.read.StaxEch;

public class Organisation implements Validation {

	public static final Organisation ORGANISATION = Keys.of(Organisation.class);

	public static enum EditMode { DISPLAY, BASE_DELIVERY, MOVE_IN, FOUNDATION, CHANGE_RESIDENCE_TYPE, IN_LIQUIDATION, LIQUIDATION, CHANGE_REPORTING }
	
	static {
		Codes.addCodes(ResourceBundle.getBundle("ch.openech.model.organisation.types.ech_organisation"));
	}

	public transient EditMode editMode;
	
	// Der eCH - Event, mit dem die aktuelle Version erstellt oder verändert wurde
	public Event event;
	
	public long id;
	public int version;
	
	// 97 : Identification
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName) @Searched
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	@Code
	public String legalForm;
	
	// 98 : Daten
	
	@Size(2) // TODO
	public String uidBrancheText;
	@Size(6) // TODO
	public String nogaCode; // 00 - 999999
	
	@Required
	public ReadablePartial foundationDate;
	@Code
	public String foundationReason;
	
	public LocalDate liquidationEntryDate;
	public ReadablePartial liquidationDate;
	@Code
	public String liquidationReason;
	
	public Language languageOfCorrespondance;
	
	// 108 : Informationen andere Register
	
	// uidregInformationType
	@Code
	public String uidregStatusEnterpriseDetail, uidregPublicStatus, uidregOrganisationType, uidregLiquidationReason;
	
	public final UidStructure uidregSourceUid = new UidStructure(); 
	
	// commercialRegisterInformation
	@Code
	public String commercialRegisterStatus, commercialRegisterEntryStatus;
	@Size(EchFormats.organisationName)
	public String commercialRegisterNameTranslation;
	public LocalDate commercialRegisterEntryDate, commercialRegisterLiquidationDate;
	
	// vatRegisterInformation
	@Code
	public String vatStatus, vatEntryStatus;
	public LocalDate vatEntryDate, vatLiquidationDate;
	
	// if reported (gemeldet)
	public TypeOfResidenceOrganisation typeOfResidenceOrganisation = TypeOfResidenceOrganisation.hasMainResidence;
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
	@Enabled("!isMainResidence")
	public Headquarter headquarter;

	public final List<ContactEntry> contacts = new ArrayList<ContactEntry>();

	//

	public boolean isMainResidence() {
		return typeOfResidenceOrganisation == TypeOfResidenceOrganisation.hasMainResidence;
	}
	
	//
	
	public Object get(String propertyName) {
		return FlatProperties.getValue(this, propertyName);
	}

	public void set(String propertyName, Object value) {
		PropertyInterface property = FlatProperties.getProperties(this.getClass()).get(propertyName);
		if (property.getFieldClazz() == LocalDate.class && value instanceof String) {
			value = ISODateTimeFormat.date().parseLocalDate((String) value);
		} else if (Enum.class.isAssignableFrom(property.getFieldClazz()) && value instanceof String) {
			StaxEch.enuum((String) value, this, property);
			return;
		} 
		FlatProperties.set(this, propertyName, value);
	}
	
	//
	
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

	public String getId() {
		return String.valueOf(id);
	}
	
}