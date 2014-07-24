package ch.openech.frontend.org;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.SearchDialogAction;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.model.ViewUtil;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.OrganisationViewPage;
import ch.openech.frontend.page.SearchOrganisationPage;
import ch.openech.model.organisation.Headquarter;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
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
	protected Form<Headquarter> createFormPanel() {
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
			super(SearchOrganisationPage.FIELD_NAMES);
		}

		@Override
		protected void save(Organisation organisation) {
			if (getObject() == null) {
				setObject(new Headquarter());
			}
			getObject().identification = ViewUtil.view(organisation, new OrganisationIdentification());
			fireObjectChange();
		}

		@Override
		public List<Organisation> search(String searchText) {
			return Backend.getInstance().read(Organisation.class, Criteria.search(searchText), 100);
		}
	}

}
