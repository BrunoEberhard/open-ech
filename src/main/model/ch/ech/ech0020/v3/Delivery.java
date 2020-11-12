package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Delivery {
	public static final Delivery $ = Keys.of(Delivery.class);

	@NotEmpty
	public Header deliveryHeader;
	public static class BaseDelivery {
		public static final BaseDelivery $ = Keys.of(BaseDelivery.class);

		public List<EventBaseDelivery> messages;
	}
	public BaseDelivery baseDelivery;
	public static class KeyExchange {
		public static final KeyExchange $ = Keys.of(KeyExchange.class);

		@NotEmpty
		public List<EventKeyExchange> messages;
	}
	public KeyExchange keyExchange;
	public EventDataRequest dataRequest;
	public EventIdentificationConversion identificationConversion;
	public EventAnnounceDuplicate announceDuplicate;
	public EventDeletedInRegister deletedInRegister;
	public EventAdoption adoption;
	public EventChildRelationship childRelationship;
	public EventNaturalizeForeigner naturalizeForeigner;
	public EventNaturalizeSwiss naturalizeSwiss;
	public EventUndoCitizen undoCitizen;
	public EventUndoSwiss undoSwiss;
	public EventChangeOrigin changeOrigin;
	public EventBirth birth;
	public EventMarriage marriage;
	public EventPartnership partnership;
	public EventSeparation separation;
	public EventUndoSeparation undoSeparation;
	public EventDivorce divorce;
	public EventUndoMarriage undoMarriage;
	public EventUndoPartnership undoPartnership;
	public EventDeath death;
	public EventMissing missing;
	public EventUndoMissing undoMissing;
	public EventMaritalStatusPartner maritalStatusPartner;
	public EventChangeName changeName;
	public EventChangeSex changeSex;
	public EventMoveIn moveIn;
	public EventMove move;
	public EventContact contact;
	public EventMoveOut moveOut;
	public EventChangeResidenceType changeResidenceType;
	public EventChangeReligion changeReligion;
	public EventChangeOccupation changeOccupation;
	public EventGuardianMeasure guardianMeasure;
	public EventUndoGuardian undoGuardian;
	public EventChangeGuardian changeGuardian;
	public EventChangeNationality changeNationality;
	public EventEntryResidencePermit changeResidencePermit;
	public EventDataLock dataLock;
	public EventPaperLock paperLock;
	public EventCare care;
	public EventChangeArmedForces changeArmedForces;
	public EventChangeCivilDefense changeCivilDefense;
	public EventChangeFireService changeFireService;
	public EventChangeHealthInsurance changeHealthInsurance;
	public EventChangeMatrimonialInheritanceArrangement changeMatrimonialInheritanceArrangement;
	public EventCorrectGuardianRelationship correctGuardianRelationship;
	public EventCorrectParentalRelationship correctParentalRelationship;
	public EventCorrectReporting correctReporting;
	public EventCorrectOccupation correctOccupation;
	public EventCorrectIdentification correctIdentification;
	public EventCorrectName correctName;
	public EventCorrectNationality correctNationality;
	public EventCorrectContact correctContact;
	public EventCorrectReligion correctReligion;
	public EventCorrectPlaceOfOrigin correctPlaceOfOrigin;
	public EventCorrectResidencePermit correctResidencePermit;
	public EventCorrectMaritalInfo correctMaritalInfo;
	public EventCorrectBirthInfo correctBirthInfo;
	public EventCorrectDeathData correctDeathData;
	public EventCorrectPersonAdditionalData correctPersonAdditionalData;
	public EventCorrectPoliticalRightData correctPoliticalRightData;
	public EventCorrectDataLock correctDataLock;
	public EventCorrectPaperLock correctPaperLock;
	public final String version = "3.0";
}