package ch.openech.client.org;

import ch.openech.client.page.OrganisationViewPage;
import ch.openech.client.page.SearchOrganisationPage;
import ch.openech.dm.organisation.Headquarter;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.search.FulltextIndexSearch;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;

public class HeadquarterField extends ObjectFlowField<Headquarter> {

	private final EchSchema echSchema;
	
	public HeadquarterField(Headquarter key, EchSchema echSchema, boolean editable) {
		this(Keys.getProperty(key), echSchema, editable);
	}
	
	public HeadquarterField(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}

	@Override
	public void setObject(Headquarter object) {
		super.setObject(object != null ? object : new Headquarter());
	}

	@Override
	protected IForm<Headquarter> createFormPanel() {
		return null;
	}

	@Override
	protected void show(Headquarter headquarter) {
		if (isEditable()) {
			if (headquarter.identification != null) {
				addText(headquarter.identification.organisationName);
			}
			addAction(new SelectHeadquarterAction());
			if (headquarter.reportingMunicipality != null) {
				addText(headquarter.reportingMunicipality.municipalityName);
			}
			// TODO
			// addAction(new SelectHeadquarterMunicipalityAction());
			if (headquarter.businessAddress != null) {
				addText(headquarter.businessAddress.toHtml());
			}
			// TODO
			// addAction(new SelectHeadquarterAddressAction());
			
		} else {
			if (headquarter.identification != null) {
				addLink(headquarter.identification.organisationName, PageLink.link(OrganisationViewPage.class, echSchema.getVersion(), headquarter.identification.technicalIds.localId.personId));
			}
			if (headquarter.reportingMunicipality != null) {
				addText(headquarter.reportingMunicipality.municipalityName);
			}
			if (headquarter.businessAddress != null) {
				addText(headquarter.businessAddress.toHtml());
			}
		}
	}
	
	public final class SelectHeadquarterAction extends SearchDialogAction<Organisation> {
		
		public SelectHeadquarterAction() {
			super(getComponent(), new FulltextIndexSearch<>(EchServer.getInstance().getPersistence().organisationIndex()), SearchOrganisationPage.FIELD_NAMES);
		}

		@Override
		protected void save(Organisation organisation) {
			if (getObject() == null) {
				setObject(new Headquarter());
			}
			getObject().identification = organisation.identification;
			fireObjectChange();
		}
	}

}
