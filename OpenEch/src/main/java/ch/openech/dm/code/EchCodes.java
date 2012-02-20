package ch.openech.dm.code;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ch.openech.mj.db.model.Code;
import ch.openech.mj.db.model.InternalCode;


public class EchCodes {

	private static final ResourceBundle common = ResourceBundle.getBundle(EchCodes.class.getPackage().getName() + ".ech_common");
	private static final ResourceBundle person = ResourceBundle.getBundle(EchCodes.class.getPackage().getName() + ".ech_person");
	private static final ResourceBundle organisation = ResourceBundle.getBundle(EchCodes.class.getPackage().getName() + ".ech_organisation");

	// Person
	
	public static final InternalCode sex = new InternalCode(Sex.class);
	public static final InternalCode mrMrs = new InternalCode(MrMrs.class);
	public static final InternalCode maritalStatus = new InternalCode(MaritalStatus.class);
	public static final InternalCode typeOfResidence = new InternalCode(TypeOfResidence.class);
	public static final InternalCode typeOfRelationship = new InternalCode(TypeOfRelationship.class);
	public static final InternalCode typeOfRelationshipInverted = new InternalCode(TypeOfRelationshipInverted.class);

	public static final Code language = new Code(person, "language");
	public static final Code religion = new Code(person, "religion");
	public static final Code nationalityStatus = new Code(person, "nationalityStatus");
	public static final Code partnerShipAbolition = new Code(person, "partnerShipAbolition");
	public static final Code separation = new Code(person, "separation");
	public static final Code typeOfHousehold = new Code(person, "typeOfHousehold");

	public static final Code reasonOfAcquisition = new Code(person, "reasonOfAcquisition");
	public static final Code federalRegister = new Code(person, "federalRegister");
	public static final Code kindOfEmployment = new Code(person, "kindOfEmployment");
	
	public static final Code residencePermit = new Code(person, "residencePermit");
	public static final Code residencePermitDetailed = new Code(person, "residencePermitDetailed");
	public static final Code basedOnLaw = new Code(person, "basedOnLaw");
	public static final Code basedOnLaw3 = new Code(person, "basedOnLaw3");
	public static final Code dataLock = new Code(person, "dataLock");
	public static final Code paperLock = new Code(person, "paperLock");

	public static final Code guardianTypeOfRelationship = new Code(typeOfRelationship, "7", "8", "9");
	public static final Code careTypeOfRelationship = new Code(typeOfRelationship, "3", "4", "5", "6");
	
	// Organisation
	
	public static final Code legalForm = new Code(organisation, "legalForm");

	public static final Code foundationReason = new Code(organisation, "foundationReason");
	public static final Code liquidationReason = new Code(organisation, "liquidationReason");
	
	public static final Code uidregStatusEnterpriseDetail = new Code(organisation, "uidregStatusEnterpriseDetail");
	public static final Code uidregPublicStatus = new Code(organisation, "uidregPublicStatus");
	public static final Code uidregOrganisation = new Code(organisation, "uidregOrganisation");
//	public static final Code uidregLiquidationReason = new Code(organisation, "uidregLiquidationReason");
	public static final Code commercialRegisterStatus = new Code(organisation, "commercialRegisterStatus");
	public static final Code commercialRegisterEntryStatus = new Code(organisation, "commercialRegisterEntryStatus");
	public static final Code vatStatus = new Code(organisation, "vatStatus");
	public static final Code vatEntryStatus = new Code(organisation, "vatEntryStatus");

	// Common
	
	public static final Code yesNo = new Code(common, "yesNo");

	public static final Code addressCategory = new Code(common, "addressCategory");
	public static final Code phoneCategory = new Code(common, "phoneCategory");
	public static final Code emailCategory = new Code(common, "emailCategory");
	public static final Code internetCategory = new Code(common, "internetCategory");

	public static Map<String, Code> getCodes() {
		Map<String, Code> result = new HashMap<String, Code>();
		for (Field field : EchCodes.class.getFields()) {
			if (Code.class.isAssignableFrom(field.getType())) {
				try {
					result.put(field.getName(), (Code) field.get(null));
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}
	
	/*
	public static final Code TYPE_OF_RESIDENCE = new Code(ch.openech.dm.StringConstants.TYPE_OF_RESIDENCE);
	public static final Code CARE = new Code(ch.openech.dm.StringConstants.CARE);
	public static final Code CANCELATION_REASON = new Code(ch.openech.dm.StringConstants.CANCELATION_REASON);
	
	


	// Contact
	
	public static CodeWithOtherField addressCategory() {
		return new CodeWithOtherField("category", "otherCategory", "addressCategory", 100);
	}

	public static CodeWithOtherField phoneCategory() {
		return new CodeWithOtherField("category", "otherCategory", "phoneCategory", 100);
	}

	public static CodeWithOtherField emailCategory() {
		return new CodeWithOtherField("category", "otherCategory", "emailCategory", 100);
	}

	public static CodeWithOtherField internetCategory() {
		return new CodeWithOtherField("category", "otherCategory", "internetCategory", 100);
	}
	
	*/
	
}
