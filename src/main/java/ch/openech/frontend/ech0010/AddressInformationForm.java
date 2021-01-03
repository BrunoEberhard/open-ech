package ch.openech.frontend.ech0010;

import static ch.ech.ech0010.AddressInformation.$;

import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.util.StringUtils;

import ch.ech.ech0010.AddressInformation;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

public class AddressInformationForm extends Form<AddressInformation> {
	private AddressInformation address;
	
	public AddressInformationForm(boolean editable, boolean swiss) {
		super(editable, 4, 70);
		line($.addressLine1);
		line($.addressLine2);
		line(Form.GROW_FIRST_ELEMENT, $.street, $.houseNumber, $.dwellingNumber);
		if (!swiss) {
			line($.postOfficeBoxText, $.postOfficeBoxNumber);
			line($.country, $.swissZipCode, $.town);
		} else {
			line(new ChIso2FormElement($.country), $.swissZipCode, new TownFormElement($.town));
		}
		line($.locality);
		
		addDependecy($.swissZipCode, new TownUpdater(), $.town);
		addDependecy($.town, new ZipUpdater(), $.swissZipCode);
	}

	@Override
	public void setObject(AddressInformation address) {
		this.address = address;
		super.setObject(address);
	}

	public static class TownUpdater implements Form.PropertyUpdater<Integer, String, AddressInformation> {

		@Override
		public String update(Integer input, AddressInformation address) {
			if (address.isSwissOrLichtenstein() && input != null && input != 0) {
				int zip = input;
				List<Plz> plzList = PlzImport.getInstance().getPlzList().stream().filter(plz -> plz.postleitzahl == zip).collect(Collectors.toList());
				
				if (plzList.size() == 1) {
					return plzList.get(0).ortsbezeichnung;
				}
			}
			return address.town;
		}
	}
	
	public static class ZipUpdater implements Form.PropertyUpdater<String, Integer, AddressInformation> {

		@Override
		public Integer update(String input, AddressInformation address) {
			if (address.isSwissOrLichtenstein() && !StringUtils.isEmpty(input)) {
				List<Plz> plzList = PlzImport.getInstance().getPlzList().stream().filter(plz -> input.equals(plz.ortsbezeichnung)).collect(Collectors.toList());
				
				if (!plzList.isEmpty()) {
					return plzList.get(0).postleitzahl;
				}
			}
			return address.swissZipCode;
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
			int zip = address.swissZipCode != null ? address.swissZipCode : 0;

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
