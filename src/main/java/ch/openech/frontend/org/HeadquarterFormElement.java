package ch.openech.frontend.org;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtil;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.OrganisationPage;
import ch.openech.frontend.page.OrganisationSearchPage;
import ch.openech.model.organisation.Headquarter;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.xml.write.EchSchema;

public class HeadquarterFormElement extends ObjectPanelFormElement<Headquarter> {

	private final EchSchema echSchema;
	
	public HeadquarterFormElement(Headquarter key, EchSchema echSchema, boolean editable) {
		this(Keys.getProperty(key), echSchema, editable);
	}
	
	public HeadquarterFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}

	@Override
	public void setValue(Headquarter object) {
		super.setValue(object != null ? object : new Headquarter());
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
				addAction(new OrganisationPage(echSchema, headquarter.identification.technicalIds.localId.personId), headquarter.identification.organisationName);
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
			super(OrganisationSearchPage.FIELD_NAMES);
		}

		@Override
		protected void save(Organisation organisation) {
			if (getValue() == null) {
				setValue(new Headquarter());
			}
			getValue().identification = ViewUtil.view(organisation, new OrganisationIdentification());
			fireObjectChange();
		}

		@Override
		public List<Organisation> search(String searchText) {
			return Backend.getInstance().read(Organisation.class, Criteria.search(searchText), 100);
		}
	}

}
