package ch.openech.frontend.org;

import static  ch.openech.model.organisation.Organisation.*;
import ch.openech.frontend.e44.TechnicalIdsFormElement;
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
		line($.organisationName);
		line($.organisationLegalName, $.organisationAdditionalName, $.legalForm, $.languageOfCorrespondance);
		line($.uid, new TechnicalIdsFormElement($.technicalIds, TechnicalIdsFormElement.WITHOUT_EU_IDS, editable));

		line($.uidBrancheText, $.nogaCode);
		if (EditMode.DISPLAY != mode) {
			line($.foundationDate, $.liquidationDate);
		} else {
			// TODO das verteilt sich falsch
			line($.foundationDate, $.liquidationEntryDate, $.liquidationDate);
		}
		line($.foundationReason, $.liquidationReason);
	}

	private void residence() {
		line($.typeOfResidenceOrganisation, $.reportingMunicipality);
		line($.arrivalDate, $.departureDate);
		line($.comesFrom, $.goesTo);
		if (EditMode.CHANGE_RESIDENCE_TYPE != mode) {
			line($.businessAddress, $.contacts, new HeadquarterFormElement($.headquarter, echSchema, editable));
		} else {
			line($.businessAddress, new HeadquarterFormElement($.headquarter, echSchema, editable));
		}
	}

	//
	
	private void uidregInformation() {
		addTitle("UID - Register");
		line($.uidregStatusEnterpriseDetail, $.uidregPublicStatus, $.uidregOrganisationType, $.uidregLiquidationReason);
		line($.uidregSourceUid);
	}

	private void commercialRegisterInformation() {
		addTitle("Handelsregister");
		line($.commercialRegisterNameTranslation);
		line($.commercialRegisterStatus,  $.commercialRegisterEntryStatus, $.commercialRegisterEntryDate, $.commercialRegisterLiquidationDate);
	}
	
	private void vatRegisterInformation() {
		addTitle("Mehrwertsteuerregister");
		line($.vatStatus, $.vatEntryStatus, $.vatEntryDate, $.vatLiquidationDate);
	}
	
}
