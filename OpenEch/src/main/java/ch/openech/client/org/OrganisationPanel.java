package ch.openech.client.org;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;
import ch.openech.client.e44.TechnicalIdsField;
import ch.openech.client.e97.OrganisationUidField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.organisation.Organisation;
import ch.openech.xml.write.EchNamespaceContext;

public class OrganisationPanel extends EchFormPanel<Organisation> {

	public enum OrganisationPanelType {
		DISPLAY, BASE_DELIVERY, MOVE_IN, FOUNDATION, CHANGE_RESIDENCE_TYPE, // CORRECT_PERSON, CORRECT_IDENTIFICATION, CORRECT_NAME
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
		boolean identificationVisible = type != OrganisationPanelType.CHANGE_RESIDENCE_TYPE;
		if (identificationVisible) {
			organisation();
		}
		residence();
	}

	public void organisation() {
		line(ORGANISATION.organisationName);
		line(ORGANISATION.organisationLegalName, ORGANISATION.organisationAdditionalName, ORGANISATION.legalForm, ORGANISATION.languageOfCorrespondance);
		line(new OrganisationUidField(ORGANISATION.uid, editable), new TechnicalIdsField(ORGANISATION.technicalIds, TechnicalIdsField.WITHOUT_EU_IDS, editable));

		line(ORGANISATION.uidBrancheText, ORGANISATION.nogaCode);
		if (OrganisationPanelType.DISPLAY != type) {
			line(ORGANISATION.foundationDate, ORGANISATION.liquidationDate);
		} else {
			// TODO das verteilt sich falsch
			line(ORGANISATION.foundationDate, ORGANISATION.liquidationEntryDate, ORGANISATION.liquidationDate);
		}
		line(ORGANISATION.foundationReason, ORGANISATION.liquidationReason);
	}

	private void residence() {
		area(ORGANISATION.typeOfResidenceOrganisation, ORGANISATION.reportingMunicipality);
		line(ORGANISATION.arrivalDate, ORGANISATION.departureDate);
		line(ORGANISATION.comesFrom, ORGANISATION.goesTo);
		if (OrganisationPanelType.CHANGE_RESIDENCE_TYPE != type) {
			area(ORGANISATION.businessAddress, ORGANISATION.contact, new HeadquarterField(ORGANISATION.headquarterOrganisation, editable));
		} else {
			area(ORGANISATION.businessAddress, new HeadquarterField(ORGANISATION.headquarterOrganisation, editable));
		}
		if (editable) {
			setRequired(ORGANISATION.arrivalDate);
			setRequired(ORGANISATION.reportingMunicipality);
			setRequired(ORGANISATION.businessAddress);
		}
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
		if (OrganisationPanelType.DISPLAY != type) {		
			setRequired(ORGANISATION.commercialRegisterStatus);
		}
	}
	
	private void vatRegisterInformation() {
		addTitle("Mehrwertsteuerregister");
		line(ORGANISATION.vatStatus, ORGANISATION.vatEntryStatus, ORGANISATION.vatEntryDate, ORGANISATION.vatLiquidationDate);
		if (OrganisationPanelType.DISPLAY != type) {		
			setRequired(ORGANISATION.vatStatus);
		}
	}
	
}
