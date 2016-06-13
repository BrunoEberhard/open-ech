package ch.openech.frontend.org;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtil;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.By;

import ch.openech.frontend.page.OrganisationPage;
import ch.openech.frontend.page.OrganisationSearchPage;
import ch.openech.model.organisation.Headquarter;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.xml.write.EchSchema;

public class HeadquarterFormElement extends ObjectFormElement<Headquarter> {

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
	protected Form<Headquarter> createForm() {
		return null;
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new SelectHeadquarterAction() };
		// TODO
		// addAction(new SelectHeadquarterMunicipalityAction());
		// addAction(new SelectHeadquarterAddressAction());
	}

	@Override
	protected void show(Headquarter headquarter) {
		if (isEditable()) {
			if (headquarter.identification != null) {
				add(headquarter.identification.organisationName);
			}
			if (headquarter.reportingMunicipality != null) {
				add(headquarter.reportingMunicipality.municipalityName);
			}
			if (headquarter.businessAddress != null) {
				add(headquarter.businessAddress);
			}
		} else {
			if (headquarter.identification != null) {
				add(headquarter.identification.organisationName, new OrganisationPage(echSchema, headquarter.identification.id));
			}
			if (headquarter.reportingMunicipality != null) {
				add(headquarter.reportingMunicipality.municipalityName);
			}
			if (headquarter.businessAddress != null) {
				add(headquarter.businessAddress);
			}
		}
	}
	
	public final class SelectHeadquarterAction extends SearchDialogAction<Organisation> {
		
		public SelectHeadquarterAction() {
			super(OrganisationSearchPage.FIELDS);
		}

		@Override
		protected void save(Organisation organisation) {
			if (getValue() == null) {
				setValue(new Headquarter());
			}
			getValue().identification = ViewUtil.view(organisation, new OrganisationIdentification());
			handleChange();
		}

		@Override
		public List<Organisation> search(String query) {
			return Backend.read(Organisation.class, By.search(query), 100);
		}
	}

}
