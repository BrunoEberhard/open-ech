package ch.openech.client.e11;

import java.util.List;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.types.ReasonOfAcquisition;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class PlaceOfOriginField extends ObjectFlowField<List<PlaceOfOrigin>> implements DemoEnabled {
	public static final boolean WITHOUT_ADD_ON = false;
	private final boolean withAddOn;
	private boolean swiss = true;
	
	public PlaceOfOriginField(PropertyInterface property, boolean editable) {
		super(property, editable);
		this.withAddOn = true;
	}
	
	/* Bei Geburt können keine AddOns zu den PlaceOfOrigins mitgegeben werden.
	 * Das macht auch Sinn, denn die Heimatorte kommen alle per Abstammung und
	 * per Geburtstag zu der geborenen Person
	 */
	public PlaceOfOriginField(List<PlaceOfOrigin> key, boolean withAddOn, boolean editable) {
		super(Keys.getProperty(key), editable);
		this.withAddOn = withAddOn;
	}
	
	public class AddOriginEditor extends ObjectFieldPartEditor<PlaceOfOrigin> {
		@Override
		public IForm<PlaceOfOrigin> createForm() {
			return new OriginPanel(withAddOn, withAddOn);
		}

		@Override
		protected PlaceOfOrigin getPart(List<PlaceOfOrigin> object) {
			PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
			// TODO Preference in PlaceOfOriginField
//			PageContext context = PageContextHelper.findContext(visual);
//			OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
//			placeOfOrigin.cantonAbbreviation.canton = preferences.preferencesDefaultsData.cantonAbbreviation.canton;
			placeOfOrigin.reasonOfAcquisition = ReasonOfAcquisition.Abstammung;
			return placeOfOrigin;
		}

		@Override
		protected void setPart(List<PlaceOfOrigin> object, PlaceOfOrigin p) {
			object.add(p);
		}
	}

	public class EditOriginEditor extends ObjectFieldPartEditor<PlaceOfOrigin> {
		private final PlaceOfOrigin placeOfOrigin;
		
		private EditOriginEditor(PlaceOfOrigin placeOfOrigin) {
			this.placeOfOrigin = placeOfOrigin;
		}

		@Override
		public IForm<PlaceOfOrigin> createForm() {
			return new OriginPanel(withAddOn, withAddOn);
		}

		@Override
		protected PlaceOfOrigin getPart(List<PlaceOfOrigin> object) {
			return placeOfOrigin;
		}

		@Override
		protected void setPart(List<PlaceOfOrigin> object, PlaceOfOrigin p) {
			object.set(object.indexOf(placeOfOrigin), p);
			fireObjectChange();
		}
	}

	private class RemoveOriginAction extends ResourceAction {
		private final PlaceOfOrigin placeOfOrigin;
		
		private RemoveOriginAction(PlaceOfOrigin placeOfOrigin) {
			this.placeOfOrigin = placeOfOrigin;
		}
		
		@Override
		public void action(IComponent context) {
			getObject().remove(placeOfOrigin);
			fireObjectChange();
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (!enabled && getObject() != null && getObject().size() > 0) {
			getObject().clear();
			fireObjectChange();
		} 
	}

	@Override
	public void fillWithDemoData() {
		getObject().clear();
		do {
			getObject().add(DataGenerator.placeOfOrigin());
		} while (Math.random() < .4);
		fireObjectChange();
	}

	@Override
	public IForm<List<PlaceOfOrigin>> createFormPanel() {
		// unused
		return null;
	}

	@Override
	protected void show(List<PlaceOfOrigin> objects) {
		for (PlaceOfOrigin placeOfOrigin : objects) {
			addText(placeOfOrigin.displayHtml());
			if (isEditable()) {
				addAction(new EditOriginEditor(placeOfOrigin));
				addAction(new RemoveOriginAction(placeOfOrigin));
				addGap();
			}
		}
		if (isEditable()) {
			addAction(new AddOriginEditor());
		}
	}
}
