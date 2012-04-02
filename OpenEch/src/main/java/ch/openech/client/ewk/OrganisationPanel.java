package ch.openech.client.ewk;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;
import ch.openech.client.e44.TechnicalIdsField;
import ch.openech.client.e97.OrganisationUidField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.fields.DateField;

public class OrganisationPanel extends EchFormPanel<Organisation> {

	public OrganisationPanel() {
		super(2);
		
		line(ORGANISATION.organisationName);
		line(ORGANISATION.organisationLegalName);
		line(ORGANISATION.organisationAdditionalName);
		line(ORGANISATION.uid, ORGANISATION.legalForm);
		line(new TechnicalIdsField(ORGANISATION.technicalIds, TechnicalIdsField.WITHOUT_EU_IDS, editable), ORGANISATION.languageOfCorrespondance);
		line(ORGANISATION.reportingMunicipality);
		line(ORGANISATION.comesFrom, ORGANISATION.goesTo);
		
		line(ORGANISATION.uidBrancheText, ORGANISATION.nogaCode);
		// required, !required
		line(new DateField(ORGANISATION.foundationDate, DateField.PARTIAL_ALLOWED), new DateField(ORGANISATION.liquidationDate, DateField.PARTIAL_ALLOWED));
		line(ORGANISATION.foundationReason, ORGANISATION.liquidationReason);

		addTitle("UID - Register");
		line(ORGANISATION.uidregStatusEnterpriseDetail, ORGANISATION.uidregPublicStatus);
		line(ORGANISATION.uidregOrganisationType, ORGANISATION.uidregLiquidationReason);
		line(new OrganisationUidField(ORGANISATION.uidregSourceUid));
		
		addTitle("Handelsregister");
		line(ORGANISATION.commercialRegisterStatus,  ORGANISATION.commercialRegisterEntryStatus);
		line(ORGANISATION.commercialRegisterNameTranslation);
		// !required, !required
		line(new DateField(ORGANISATION.commercialRegisterEntryDate), new DateField(ORGANISATION.commercialRegisterLiquidationDate));

		addTitle("Mehrwertsteuerregister");
		line(ORGANISATION.vatStatus, ORGANISATION.vatEntryStatus);
		line(ORGANISATION.vatEntryDate, ORGANISATION.vatLiquidationDate);
		
		area(ORGANISATION.businessAddress);
	}
	
}
