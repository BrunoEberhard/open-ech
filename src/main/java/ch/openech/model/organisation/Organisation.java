package  ch.openech.model.organisation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.EchFormats;
import ch.openech.model.Event;
import ch.openech.model.code.TypeOfResidenceOrganisation;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.organisation.OrganisationTypes.FoundationReason;
import ch.openech.model.organisation.OrganisationTypes.LiquidationReason;
import ch.openech.model.organisation.OrganisationTypes.OrganisationEntryStatus;
import ch.openech.model.organisation.OrganisationTypes.OrganisationStatus;
import ch.openech.model.organisation.OrganisationTypes.UidregLiquidationReason;
import ch.openech.model.organisation.OrganisationTypes.UidregOrganisationType;
import ch.openech.model.organisation.OrganisationTypes.UidregPublicStatus;
import ch.openech.model.organisation.OrganisationTypes.UidregStatusEnterpriseDetail;
import ch.openech.model.organisation.types.LegalForm;
import ch.openech.model.types.Language;
import ch.openech.xml.read.StaxEch;

public class Organisation implements Validation {

	public static final Organisation $ = Keys.of(Organisation.class);

	public static enum EditMode { DISPLAY, BASE_DELIVERY, MOVE_IN, FOUNDATION, CHANGE_RESIDENCE_TYPE, IN_LIQUIDATION, LIQUIDATION, CHANGE_REPORTING }
	
	public transient EditMode editMode;
	
	// Der eCH - Event, mit dem die aktuelle Version erstellt oder verändert wurde
	public Event event;
	
	public Object id;
	public int version;
	public boolean historized;

	// 97 : Identification
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@NotEmpty @Size(EchFormats.organisationName) @Searched
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	public LegalForm legalForm;
	
	// 98 : Daten
	
	@Size(2) // TODO
	public String uidBrancheText;
	@Size(6) // TODO
	public String nogaCode; // 00 - 999999
	
	@NotEmpty
	public final DatePartiallyKnown foundationDate = new DatePartiallyKnown();
	public FoundationReason foundationReason;
	
	public LocalDate liquidationEntryDate;
	public final DatePartiallyKnown liquidationDate = new DatePartiallyKnown();
	public LiquidationReason liquidationReason;
	
	public Language languageOfCorrespondance;
	
	// 108 : Informationen andere Register
	
	// uidregInformationType
	public UidregPublicStatus uidregPublicStatus;
	public UidregLiquidationReason uidregLiquidationReason;
	public UidregStatusEnterpriseDetail uidregStatusEnterpriseDetail;
	public UidregOrganisationType uidregOrganisationType;
	
	public final UidStructure uidregSourceUid = new UidStructure(); 
	
	// commercialRegisterInformation
	public OrganisationStatus commercialRegisterStatus;
	public OrganisationEntryStatus commercialRegisterEntryStatus;
	@Size(EchFormats.organisationName)
	public String commercialRegisterNameTranslation;
	public LocalDate commercialRegisterEntryDate, commercialRegisterLiquidationDate;
	
	// vatRegisterInformation
	public OrganisationStatus vatStatus;
	public OrganisationEntryStatus vatEntryStatus;
	public LocalDate vatEntryDate, vatLiquidationDate;
	
	// if reported (gemeldet)
	public TypeOfResidenceOrganisation typeOfResidenceOrganisation = TypeOfResidenceOrganisation.hasMainResidence;
	// Achtung: Im Gegensatz zu einer Person kann eine Organisation nur einen primary, secondary oder other Eintrag haben
	//          Die folgenden Felder sind dabei bei allen 3 Möglichkeiten *genau* gleich.
	@NotEmpty
	public MunicipalityIdentification reportingMunicipality;
	@NotEmpty
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
		if (property.getClazz() == LocalDate.class && value instanceof String) {
			value = DateTimeFormatter.ISO_DATE.parse((String) value);
		} else if (Enum.class.isAssignableFrom(property.getClazz()) && value instanceof String) {
			StaxEch.enuum((String) value, this, property);
			return;
		} 
		FlatProperties.set(this, propertyName, value);
	}
	
	//
	
	@Override
	public List<ValidationMessage> validate() {
		List<ValidationMessage> resultList = new ArrayList<>();
		if (editMode == EditMode.IN_LIQUIDATION || editMode == EditMode.LIQUIDATION) {
			EmptyValidator.validate(resultList, this, Organisation.$.liquidationReason);
			if (editMode == EditMode.IN_LIQUIDATION) {
				EmptyValidator.validate(resultList, this, Organisation.$.liquidationEntryDate);
			} else {
				EmptyValidator.validate(resultList, this, Organisation.$.liquidationDate);
			}
		} else if (editMode != null) {
			EmptyValidator.validate(resultList, this, Organisation.$.businessAddress);
			if (editMode == EditMode.CHANGE_REPORTING) {
				EmptyValidator.validate(resultList, this, Organisation.$.typeOfResidenceOrganisation);
			} else {
				EmptyValidator.validate(resultList, this, Organisation.$.arrivalDate);
			}
		}
		return resultList;
	}

	public String getId() {
		return String.valueOf(id);
	}
	
}
