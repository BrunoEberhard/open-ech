package ch.openech.frontend.e11;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.frontend.toolkit.Action;
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
	
	public class AddOriginEditor extends AddListEntryEditor {
		@Override
		public Form<PlaceOfOrigin> createForm() {
			return new OriginPanel(withAddOn, withAddOn);
		}

		@Override
		protected PlaceOfOrigin createObject() {
			PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
			// TODO Preference in PlaceOfOriginField
//			PageContext context = PageContextHelper.findContext(visual);
//			OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
//			placeOfOrigin.canton.cantonAbbreviation = preferences.preferencesDefaultsData.canton.cantonAbbreviation;
			placeOfOrigin.reasonOfAcquisition = ReasonOfAcquisition.Abstammung;
			return placeOfOrigin;
		}

		@Override
		protected void addEntry(PlaceOfOrigin p) {
			PlaceOfOriginFormElement.this.getValue().add(p);
		}
	}

	public class EditOriginAction extends EditListEntryAction {
		private EditOriginAction(PlaceOfOrigin placeOfOrigin) {
			super(placeOfOrigin);
		}

		@Override
		public Form<PlaceOfOrigin> createForm() {
			return new OriginPanel(withAddOn, withAddOn);
		}
		
		@Override
		protected void editEntry(PlaceOfOrigin placeOfOrigin, PlaceOfOrigin p) {
			List<PlaceOfOrigin> placeOfOrigins = PlaceOfOriginFormElement.this.getValue();
			placeOfOrigins.set(placeOfOrigins.indexOf(placeOfOrigin), p);
		}
	}

	private class RemoveOriginAction extends Action {
		private final PlaceOfOrigin placeOfOrigin;
		
		private RemoveOriginAction(PlaceOfOrigin placeOfOrigin) {
			this.placeOfOrigin = placeOfOrigin;
		}
		
		@Override
		public void action() {
			getValue().remove(placeOfOrigin);
			handleChange();
		}
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
	public Form<List<PlaceOfOrigin>> createFormPanel() {
		// unused
		return null;
	}

	@Override
	protected void showEntry(PlaceOfOrigin placeOfOrigin) {
		add(placeOfOrigin,
			new EditOriginAction(placeOfOrigin),
			new RemoveOriginAction(placeOfOrigin)
		);
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddOriginEditor() };
	}

}
