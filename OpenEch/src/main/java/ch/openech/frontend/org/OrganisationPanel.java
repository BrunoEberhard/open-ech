package ch.openech.frontend.org;

import static  ch.openech.model.organisation.Organisation.*;
import ch.openech.frontend.e44.TechnicalIdsField;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.organisation.Organisation;
import  ch.openech.model.organisation.Organisation.EditMode;
import ch.openech.xml.write.EchSchema;

public class OrganisationPanel extends EchForm<Organisation> {

	private final EditMode mode;
	
	public OrganisationPanel(EditMode mode, EchSchema context) {
		super(context, EditMode.DISPLAY != mode, 4);
		this.mode = mode;
		
		reportedOrganisation();
		
		if (EditMode.FOUNDATION == mode || EditMode.DISPLAY == mode) {
			uidregInformation();
			commercialRegisterInformation();
			vatRegisterInformation();
		}
	}

	private void reportedOrganisation() {
		boolean identificationVisible = mode != EditMode.CHANGE_RESIDENCE_TYPE;
		if (identificationVisible) {
			organisation();
		}
		residence();
	}

	public void organisation() {
		line(ORGANISATION.organisationName);
		line(ORGANISATION.organisationLegalName, ORGANISATION.organisationAdditionalName, ORGANISATION.legalForm, ORGANISATION.languageOfCorrespondance);
		line(ORGANISATION.uid, new TechnicalIdsField(ORGANISATION.technicalIds, TechnicalIdsField.WITHOUT_EU_IDS, editable));

		line(ORGANISATION.uidBrancheText, ORGANISATION.nogaCode);
		if (EditMode.DISPLAY != mode) {
			line(ORGANISATION.foundationDate, ORGANISATION.liquidationDate);
		} else {
			// TODO das verteilt sich falsch
			line(ORGANISATION.foundationDate, ORGANISATION.liquidationEntryDate, ORGANISATION.liquidationDate);
		}
		line(ORGANISATION.foundationReason, ORGANISATION.liquidationReason);
	}

	private void residence() {
		line(ORGANISATION.typeOfResidenceOrganisation, ORGANISATION.reportingMunicipality);
		line(ORGANISATION.arrivalDate, ORGANISATION.departureDate);
		line(ORGANISATION.comesFrom, ORGANISATION.goesTo);
		if (EditMode.CHANGE_RESIDENCE_TYPE != mode) {
			line(ORGANISATION.businessAddress, ORGANISATION.contacts, new HeadquarterField(ORGANISATION.headquarter, echSchema, editable));
		} else {
			line(ORGANISATION.businessAddress, new HeadquarterField(ORGANISATION.headquarter, echSchema, editable));
		}
	}

	//
	
	private void uidregInformation() {
		addTitle("UID - Register");
		line(ORGANISATION.uidregStatusEnterpriseDetail, ORGANISATION.uidregPublicStatus, ORGANISATION.uidregOrganisationType, ORGANISATION.uidregLiquidationReason);
		line(ORGANISATION.uidregSourceUid);
	}

	private void commercialRegisterInformation() {
		addTitle("Handelsregister");
		line(ORGANISATION.commercialRegisterNameTranslation);
		line(ORGANISATION.commercialRegisterStatus,  ORGANISATION.commercialRegisterEntryStatus, ORGANISATION.commercialRegisterEntryDate, ORGANISATION.commercialRegisterLiquidationDate);
	}
	
	private void vatRegisterInformation() {
		addTitle("Mehrwertsteuerregister");
		line(ORGANISATION.vatStatus, ORGANISATION.vatEntryStatus, ORGANISATION.vatEntryDate, ORGANISATION.vatLiquidationDate);
	}
	
}
