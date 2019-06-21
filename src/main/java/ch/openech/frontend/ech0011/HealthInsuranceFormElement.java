package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormLookupFormElement;

import ch.ech.ech0021.HealthInsuranceData;

public class HealthInsuranceFormElement extends FormLookupFormElement<HealthInsuranceData> {

	public HealthInsuranceFormElement(HealthInsuranceData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public Form<HealthInsuranceData> createForm() {
		Form<HealthInsuranceData> form = new Form<>(2);
		form.line(HealthInsuranceData.$.healthInsured);
		// form.line(new
		// IdentificationFormElement(HealthInsuranceData.$.insurance.insuranceName,
		// false));
		form.line(HealthInsuranceData.$.healthInsuranceValidFrom);
		return form;
	}
	
//	public static class InsuranceFormElement extends AbstractFormElement<Insurance> {
//		private Insurance value;
//		private Input<String> textField;
//		private final SwitchComponent component;
//
//		public InsuranceFormElement(Insurance key) {
//			super(key);
//			textField = Frontend.getInstance().createReadOnlyTextField();
//			component = Frontend.getInstance().createSwitchComponent();
//		}
//		
//		private void refresh() {
//			Input<String> input = Frontend.getInstance().createReadOnlyTextField();
//			ActionGroup actions = new ActionGroup(null);
//			actions.add(new PersonLookupAction());
//			actions.add(new PersonNameEditor());
//			if (organisation) {
//				actions.add(new OrganisationLookupAction());
//			}
//			if (value.person != null) {
//				input.setValue(Rendering.toString(value));
//				actions.add(new IdentificationClearAction());
//			} else if (value.organisation != null) {
//				input.setValue(Rendering.toString(value.organisation));
//				actions.add(new IdentificationClearAction());
//			} else {
//				input.setValue(null);
//			}
//			component.show(Frontend.getInstance().createLookup(input, actions));
//		}
//		
//		@Override
//		public void setValue(Insurance object) {
//			this.value = value != null ?  value : new Insurance();
//		}
//
//		@Override
//		public Insurance getValue() {
//			return value;
//		}
//
//		@Override
//		public IComponent getComponent() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//	}

}
