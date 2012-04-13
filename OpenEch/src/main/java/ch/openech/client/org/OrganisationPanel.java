package ch.openech.client.org;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;
import ch.openech.client.e44.TechnicalIdsField;
import ch.openech.client.e97.OrganisationUidField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.xml.write.EchNamespaceContext;

public class OrganisationPanel extends EchFormPanel<Organisation> {

	public enum OrganisationPanelType {
		DISPLAY, BASE_DELIVERY, MOVE_IN, FOUNDATION, // CHANGE_RESIDENCE_TYPE, CORRECT_PERSON, CORRECT_IDENTIFICATION, CORRECT_NAME
	};

	private final OrganisationPanelType type;
	
	public OrganisationPanel(OrganisationPanelType type, EchNamespaceContext context) {
		super(context, OrganisationPanelType.DISPLAY != type, 4);
		this.type = type;
		
		reportedOrganisation();
		
		if (OrganisationPanelType.FOUNDATION == type || OrganisationPanelType.DISPLAY == type) {
			uidregInformation();
			commercialRegisterInformation();
			vatRegisterInformation();
		}
	}

	private void reportedOrganisation() {
		organisation();
		residence();
	}

	public void organisation() {
		line(ORGANISATION.organisationName);
		line(ORGANISATION.organisationLegalName, ORGANISATION.organisationAdditionalName, ORGANISATION.legalForm, ORGANISATION.languageOfCorrespondance);
		line(new OrganisationUidField(ORGANISATION.uid, editable), new TechnicalIdsField(ORGANISATION.technicalIds, TechnicalIdsField.WITHOUT_EU_IDS, editable));

		line(ORGANISATION.uidBrancheText, ORGANISATION.nogaCode);
		line(new DateField(ORGANISATION.foundationDate, DateField.PARTIAL_ALLOWED, editable), new DateField(ORGANISATION.liquidationDate, DateField.PARTIAL_ALLOWED, editable));
		line(ORGANISATION.foundationReason, ORGANISATION.liquidationReason);
	}

	private void residence() {
		area(ORGANISATION.typeOfResidenceOrganisation, ORGANISATION.reportingMunicipality);
		line(ORGANISATION.arrivalDate, ORGANISATION.departureDate);
		line(ORGANISATION.comesFrom, ORGANISATION.goesTo);
		area(ORGANISATION.businessAddress, new HeadquarterField(ORGANISATION.headquarterOrganisation, editable));
	}

	//
	
	private void uidregInformation() {
		addTitle("UID - Register");
		line(ORGANISATION.uidregStatusEnterpriseDetail, ORGANISATION.uidregPublicStatus, ORGANISATION.uidregOrganisationType, ORGANISATION.uidregLiquidationReason);
		line(new OrganisationUidField(ORGANISATION.uidregSourceUid, editable));
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
