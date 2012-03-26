package ch.openech.client.e10;

import static ch.openech.dm.common.CountryZipTown.COUNTRY_ZIP_TOWN;

import java.util.List;

import ch.openech.dm.common.CountryZipTown;
import ch.openech.dm.common.ZipTown;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.util.StringUtils;


public class CountryZipTownField extends AbstractEditField<CountryZipTown> implements DemoEnabled, Validatable, Indicator {
	private CountryField countryField;
	private ZipTownField zipTownField;
	private HorizontalLayout horizontalLayout;
	
	public CountryZipTownField(Object key) {
		super(key, true);
		
		countryField = new CountryField();
		zipTownField = new ZipTownField();
		countryField.setConnectedZipTownField(zipTownField);
		listenTo(countryField);
		listenTo(zipTownField);
		
		horizontalLayout = ClientToolkit.getToolkit().createHorizontalLayout(countryField, zipTownField);
	}
	
	@Override
	public Object getComponent() {
		return decorateWithContextActions(horizontalLayout);
	}
	
	@Override
	public CountryZipTown getObject() {
		CountryZipTown countryZipTown = (CountryZipTown) zipTownField.getObject();
		countryZipTown.country = countryField.getObject();
		return countryZipTown;
	}

	@Override
	public void setObject(CountryZipTown fromObject) {
		if (fromObject == null) {
			fromObject = new CountryZipTown();
		}
		countryField.setObject(fromObject.country);
		zipTownField.setObject(fromObject);
	}

	@Override
	public void fillWithDemoData() {
//		Place place = new Place();
//		if (Math.random() < 0.8) {
//			int index = (int)(Math.random() * (double)municipalityIdentificationModel.getSize());
//			((MunicipalityIdentification) municipalityIdentificationModel.getElementAt(index)).copyTo(place.municipalityIdentification);
//		} else {
//			int index = (int)(Math.random() * (double)countryIdentificationModel.getSize());
//			((CountryIdentification) countryIdentificationModel.getElementAt(index)).copyTo(place.countryIdentification);
//			place.foreignTown = NameGenerator.officialName() + "Town";
//		}
//		setObject(place);
		countryField.setObject("CH");
		zipTownField.fillWithDemoData();
	}
	
	@Override
	public void validate(List<ValidationMessage> resultList) {
		String country = countryField.getObject();
		if (StringUtils.isBlank(country)) {
			resultList.add(new ValidationMessage(COUNTRY_ZIP_TOWN.country, "Eingabe fehlt"));
		}
		
		// TODO
		ZipTown town = zipTownField.getObject();
		if (StringUtils.isBlank(town.town)) {
			resultList.add(new ValidationMessage(COUNTRY_ZIP_TOWN.town, "Eingabe fehlt"));
		}
	}
	
	@Override
	protected Indicator[] getIndicatingComponents() {
		return new Indicator[]{countryField, zipTownField};
	}
	
}
