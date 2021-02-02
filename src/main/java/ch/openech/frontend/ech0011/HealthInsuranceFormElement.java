package ch.openech.frontend.ech0011;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.SwitchComponent;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.Rendering;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.StringUtils;

import ch.ech.ech0010.MailAddress;
import ch.ech.ech0021.HealthInsuranceData;
import ch.ech.ech0021.HealthInsuranceData.Insurance;
import ch.ech.ech0072.CountryInformation;
import ch.openech.frontend.ech0010.AddressForm;
import ch.openech.frontend.form.EchForm;

public class HealthInsuranceFormElement extends FormLookupFormElement<HealthInsuranceData> {

	public HealthInsuranceFormElement(HealthInsuranceData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public Form<HealthInsuranceData> createForm() {
		Form<HealthInsuranceData> form = new Form<>(2);
		form.line(HealthInsuranceData.$.healthInsured);
		form.line(new InsuranceFormElement(HealthInsuranceData.$.insurance));
		form.line(HealthInsuranceData.$.healthInsuranceValidFrom);
		return form;
	}
	
	public static class InsuranceFormElement extends AbstractFormElement<Insurance> {
		private Insurance value;
		private Input<String> textField;
		private final SwitchComponent component;

		public InsuranceFormElement(Insurance key) {
			super(key);
			textField = Frontend.getInstance().createReadOnlyTextField();
			component = Frontend.getInstance().createSwitchComponent();
		}

		private void refresh() {
			ActionGroup actions = new ActionGroup(null);
			if (!EmptyObjects.isEmpty(value.insuranceAddress)) {
				textField.setValue(Rendering.toString(value.insuranceAddress));
				actions.add(new InsuranceAddressEditor());
				actions.add(new InsuranceClearAction());
			} else if (!StringUtils.isEmpty(value.insuranceName)) {
				textField.setValue(value.insuranceName);
				actions.add(new InsuranceNameEditor());
				actions.add(new InsuranceClearAction());
			} else {
				actions.add(new InsuranceNameEditor());
				actions.add(new InsuranceAddressEditor());
				textField.setValue(null);
			}
			component.show(Frontend.getInstance().createLookup(textField, actions));
		}

		@Override
		public void setValue(Insurance value) {
			this.value = value != null ? value : new Insurance();
			refresh();
		}

		protected void setValueInternal(Insurance object) {
			setValue(object);
			listener().changed(component);
		}

		@Override
		public Insurance getValue() {
			return value;
		}

		@Override
		public IComponent getComponent() {
			return component;
		}

		public class InsuranceClearAction extends Action {
			@Override
			public void run() {
				InsuranceFormElement.this.setValueInternal(null);
			}
		}

		public class InsuranceNameEditor extends SimpleEditor<Insurance> {
			@Override
			protected Form<Insurance> createForm() {
				Form<Insurance> form = new EchForm<>(Form.EDITABLE, 1);
				form.line(Insurance.$.insuranceName);
				return form;
			}

			@Override
			protected Insurance createObject() {
				Insurance insurance = new Insurance();
				insurance.insuranceName = InsuranceFormElement.this.getValue().insuranceName;
				return insurance;
			}

			@Override
			protected Insurance save(Insurance object) {
				InsuranceFormElement.this.setValueInternal(object);
				return object;
			}
		}

		public class InsuranceAddressEditor extends SimpleEditor<MailAddress> {
			@Override
			protected Form<MailAddress> createForm() {
                return new AddressForm(Form.EDITABLE, true, false, true);
			}

			@Override
			protected MailAddress createObject() {
				if (InsuranceFormElement.this.getValue().insuranceAddress != null) {
					return CloneHelper.clone(InsuranceFormElement.this.getValue().insuranceAddress);
				} else {
					MailAddress address = new MailAddress();
					CountryInformation swiss = Backend.read(CountryInformation.class, 8100);
					if (swiss != null) {
						address.addressInformation.country = swiss.getCountry();
					} else {
						Frontend.showError("Land Schweiz nicht vorhanden");
					}
					return address;
				}
			}

			@Override
			protected MailAddress save(MailAddress object) {
				Insurance insurance = new Insurance();
				insurance.insuranceAddress = object;
				InsuranceFormElement.this.setValueInternal(insurance);
				return object;
			}
		}

		public class ClearInsuranceAction extends Action {
			@Override
			public void run() {
				InsuranceFormElement.this.setValueInternal(new Insurance());
			}
		}

	}


}
