package ch.openech.frontend.ech0010;

import static ch.ech.ech0010.MailAddress.$;

import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.util.StringUtils;

import ch.ech.ech0010.MailAddress;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

public class AddressForm extends Form<MailAddress> {
	private MailAddress address;
	
	public AddressForm(boolean editable, boolean swiss, boolean person, boolean organisation) {
		super(editable, 4);
		if (organisation) {
			line($.names.organisationName);
			line($.names.organisationNameAddOn1);
			line($.names.organisationNameAddOn2);
		}
		if (organisation || person) {
			line($.names.mrMrs, $.names.title);
			line($.names.title);
			line($.names.firstName, $.names.lastName);
		}
		line($.addressInformation.addressLine1);
		line($.addressInformation.addressLine2);
		line($.addressInformation.street, new HouseNumberFormElement($.addressInformation.houseNumber, editable));
		if (!swiss) {
			line($.addressInformation.postOfficeBoxText, $.addressInformation.postOfficeBoxNumber);
			line($.addressInformation.country, $.addressInformation.swissZipCode, $.addressInformation.town);
		} else {
			line(/* new ChIso2FormElement(), */ $.addressInformation.swissZipCode, new TownFormElement($.addressInformation.town));
		}
		line($.addressInformation.locality);
		
		addDependecy($.addressInformation.swissZipCode, new TownUpdater(), $.addressInformation.town);
		addDependecy($.addressInformation.town, new ZipUpdater(), $.addressInformation.swissZipCode);
	}

	@Override
	public void setObject(MailAddress address) {
		this.address = address;
		super.setObject(address);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 70;
	}

	public static class TownUpdater implements Form.PropertyUpdater<Integer, String, MailAddress> {

		@Override
		public String update(Integer input, MailAddress address) {
			if (address.addressInformation.isSwissOrLichtenstein() && input != null && input != 0) {
				int zip = input;
				List<Plz> plzList = PlzImport.getInstance().getPlzList().stream().filter(plz -> plz.postleitzahl == zip).collect(Collectors.toList());
				
				if (plzList.size() == 1) {
					return plzList.get(0).ortsbezeichnung;
				}
			}
			return address.addressInformation.town;
		}
	}
	
	public static class ZipUpdater implements Form.PropertyUpdater<String, Integer, MailAddress> {

		@Override
		public Integer update(String input, MailAddress address) {
			if (address.addressInformation.isSwissOrLichtenstein() && !StringUtils.isEmpty(input)) {
				List<Plz> plzList = PlzImport.getInstance().getPlzList().stream().filter(plz -> input.equals(plz.ortsbezeichnung)).collect(Collectors.toList());
				
				if (!plzList.isEmpty()) {
					return plzList.get(0).postleitzahl;
				}
			}
			return address.addressInformation.swissZipCode;
		}
	}

	public class TownFormElement extends AbstractFormElement<String> {
		private final Input<String> textField;

		protected TownFormElement(String key) {
			super(key);
			textField = Frontend.getInstance().createTextField(100, null, new PlzSearch(), listener());
		}

		@Override
		public void setValue(String value) {
			textField.setValue(value);
		}

		@Override
		public String getValue() {
			return textField.getValue();
		}

		@Override
		public IComponent getComponent() {
			return textField;
		}
	}

	public class PlzSearch implements Search<String> {

		@Override
		public List<String> search(String query) {
			int zip = address.addressInformation.swissZipCode != null ? address.addressInformation.swissZipCode : 0;

			List<String> plzs = PlzImport.getInstance().getPlzList().stream().filter(plz -> (plz.postleitzahl == zip && plz.ortsbezeichnung.startsWith(query)))
					.map(plz -> plz.ortsbezeichnung).collect(Collectors.toList());
			if (!plzs.isEmpty()) {
				return plzs;
			}

			plzs = PlzImport.getInstance().getPlzList().stream().filter(plz -> plz.ortsbezeichnung.startsWith(query)).map(plz -> plz.ortsbezeichnung)
					.collect(Collectors.toList());
			return plzs;
		}
		
	}

}
