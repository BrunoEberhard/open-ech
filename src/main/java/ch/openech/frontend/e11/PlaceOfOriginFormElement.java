package ch.openech.frontend.e11;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.types.ReasonOfAcquisition;

public class PlaceOfOriginFormElement extends ListFormElement<PlaceOfOrigin> implements Mocking {
	public static final boolean WITHOUT_ADD_ON = false;
	private final boolean withAddOn;
	private boolean swiss = true;
	
	public PlaceOfOriginFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
		this.withAddOn = true;
	}
	
	/* Bei Geburt können keine AddOns zu den PlaceOfOrigins mitgegeben werden.
	 * Das macht auch Sinn, denn die Heimatorte kommen alle per Abstammung und
	 * per Geburtstag zu der geborenen Person
	 */
	public PlaceOfOriginFormElement(List<PlaceOfOrigin> key, boolean withAddOn, boolean editable) {
		super(Keys.getProperty(key), editable);
		this.withAddOn = withAddOn;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (!enabled && getValue() != null && getValue().size() > 0) {
			getValue().clear();
			handleChange();
		} 
	}

	@Override
	public void mock() {
		getValue().clear();
		do {
			getValue().add(DataGenerator.placeOfOrigin());
		} while (Math.random() < .4);
		handleChange();
	}

	@Override
	protected PlaceOfOrigin createEntry() {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		// TODO Preference in PlaceOfOriginField
//		PageContext context = PageContextHelper.findContext(visual);
//		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
//		placeOfOrigin.canton.cantonAbbreviation = preferences.preferencesDefaultsData.canton.cantonAbbreviation;
		placeOfOrigin.reasonOfAcquisition = ReasonOfAcquisition.Abstammung;
		return placeOfOrigin;
	}

	@Override
	public Form<PlaceOfOrigin> createForm(boolean edit) {
		return new OriginPanel(withAddOn, withAddOn);
	}

}
