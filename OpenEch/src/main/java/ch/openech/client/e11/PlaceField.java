package ch.openech.client.e11;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.client.e07.MunicipalityFreePanel;
import ch.openech.client.e08.CountryFreePanel;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;


public class PlaceField extends ObjectField<Place> implements DemoEnabled, Validatable, Indicator {
	private final ComboBox comboBoxMunicipality;
	private final TextField textMunicipality;
	private final SwitchLayout switchLayoutMunicipality;
	
	private final ComboBox comboBoxCountry;
	private final TextField textCountry;
	private final SwitchLayout switchLayoutCountry;
	private final TextField textForeignTown;
	private final HorizontalLayout horizontalLayoutForeign;
	
	private final SwitchLayout switchLayout;

	public PlaceField(Object key) {
		super(key);
		
		comboBoxMunicipality = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxMunicipality.setObjects(StaxEch0071.getInstance().getMunicipalityIdentifications());
		textMunicipality = ClientToolkit.getToolkit().createReadOnlyTextField();
		switchLayoutMunicipality = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayoutMunicipality.show(comboBoxMunicipality);
		
		comboBoxCountry = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxCountry.setObjects(StaxEch0072.getInstance().getCountryIdentifications());
		textCountry = ClientToolkit.getToolkit().createReadOnlyTextField();
		switchLayoutCountry = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayoutCountry.show(comboBoxCountry);
		
		textForeignTown = ClientToolkit.getToolkit().createReadOnlyTextField();

		horizontalLayoutForeign = ClientToolkit.getToolkit().createHorizontalLayout(switchLayoutCountry, textForeignTown);
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(switchLayoutMunicipality);
		
		createMenu();
	}

	@Override
	protected IComponent getComponent0() {
		return switchLayout;
	}

	private void createMenu() {
		addAction(new PlaceMunicipalitySelectAction());
		addAction(new PlaceMunicipalityFreeEntryEditor());
		addAction(new PlaceCountrySelectAction());
		addAction(new PlaceCountryFreeEntryEditor());
		addAction(new PlaceUnknownAction());
	}
	
	private void modeSelectMunicipality() {
		switchLayout.show(switchLayoutMunicipality);
		switchLayoutMunicipality.show(comboBoxMunicipality);
	}

	private void modeFreeMunicipality(MunicipalityIdentification municipalityIdentification) {
		textMunicipality.setText(municipalityIdentification.municipalityName);
		switchLayout.show(switchLayoutMunicipality);
		switchLayoutMunicipality.show(textMunicipality);
	}

	private void modeSelectCountry() {
		switchLayout.show(horizontalLayoutForeign);
		switchLayoutCountry.show(comboBoxCountry);
	}

	private void modeFreeCountry(CountryIdentification countryIdentification) {
		textCountry.setText(countryIdentification.countryNameShort); // TODO
		switchLayout.show(horizontalLayoutForeign);
		switchLayoutCountry.show(textCountry);
	}

	private void modeUnknown() {
		textMunicipality.setText("placeOfBirth".equals(getName()) ? "Unbekannt" : "-");
		switchLayout.show(horizontalLayoutForeign);
		switchLayoutCountry.show(textCountry);
	}
	
	private final class PlaceMunicipalitySelectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeSelectMunicipality();
			comboBoxMunicipality.requestFocus();
		}
	}

	public final class PlaceMunicipalityFreeEntryEditor extends ObjectFieldPartEditor<MunicipalityIdentification> {
		
		public PlaceMunicipalityFreeEntryEditor() {
			super();
		}
		
		@Override
		public FormVisual<MunicipalityIdentification> createForm() {
			return new MunicipalityFreePanel();
		}

		@Override
		protected MunicipalityIdentification getPart(Place object) {
			return object.municipalityIdentification;
		}

		@Override
		protected void setPart(Place object, MunicipalityIdentification municipalityIdentification) {
			object.setMunicipalityIdentification(municipalityIdentification);
		}
	}

	private final class PlaceCountrySelectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeSelectCountry();
			comboBoxCountry.requestFocus();
		}
	}

	public final class PlaceCountryFreeEntryEditor extends ObjectFieldPartEditor<CountryIdentification> {
		
		@Override
		public FormVisual<CountryIdentification> createForm() {
			return new CountryFreePanel();
		}

		@Override
		protected CountryIdentification getPart(Place object) {
			return object.countryIdentification;
		}

		@Override
		protected void setPart(Place object, CountryIdentification p) {
			object.setCountryIdentification(p);
		}
	}

	private final class PlaceUnknownAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			getObject().clear();
			display(getObject());
			fireChange();
		}
	}

	// 

	@Override
	public void setObject(Place value) {
		if (value == null) {
			value = new Place();
		}
		super.setObject(value);
	}
	
	@Override
	public void display(Place value) {
		if (value.isSwiss()) {
			MunicipalityIdentification municipalityIdentification = value.municipalityIdentification;
			// Zuerst die Selection löschen, damit bei einem nicht vorhandenem Element nicht
			// einfach die bestehende Selektion bestehen bleibt @see JComboBox.setSelectedItem
			int index = StaxEch0071.getInstance().getMunicipalityIdentifications().indexOf(municipalityIdentification);
			if (index >= 0) {
				comboBoxMunicipality.setSelectedObject(municipalityIdentification);
				modeSelectMunicipality();
			} else {
				modeFreeMunicipality(municipalityIdentification);
			}
		} else if (value.isForeign()) {
			CountryIdentification countryIdentification = value.countryIdentification;
			int index = StaxEch0072.getInstance().getCountryIdentifications().indexOf(countryIdentification);
			if (index >= 0) {
				comboBoxCountry.setSelectedObject(countryIdentification);
				modeSelectCountry();
			} else {
				modeFreeCountry(countryIdentification);
			}
		} else {
			modeUnknown();
		}

		textForeignTown.setText(value.foreignTown);
	}

	@Override
	public void fillWithDemoData() {
		Place place = new Place();
		if (Math.random() < 0.8) {
			int index = (int)(Math.random() * (double)StaxEch0071.getInstance().getMunicipalityIdentifications().size());
			place.setMunicipalityIdentification(StaxEch0071.getInstance().getMunicipalityIdentifications().get(index));
		} else {
			int index = (int)(Math.random() * (double)StaxEch0072.getInstance().getCountryIdentifications().size());
			place.setCountryIdentification(StaxEch0072.getInstance().getCountryIdentifications().get(index));
			place.foreignTown = NameGenerator.officialName() + "Town";
		}
		setObject(place);
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		// Es ist von dem Formular abhängig, ob ein PlaceField leer sein darf. Daher wird z.B.
		// bei BirthPage validiert und nicht hier
	}
	
	@Override
	protected Indicator[] getIndicatingComponents() {
		return new Indicator[]{comboBoxMunicipality, textMunicipality, comboBoxCountry, textCountry, textForeignTown};
	}
	
	@Override
	public FormVisual<Place> createFormPanel() {
		// unused
		return null;
	}

}
