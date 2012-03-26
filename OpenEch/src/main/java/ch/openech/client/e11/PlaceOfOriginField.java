package ch.openech.client.e11;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.swing.PreferencesHelper;

public class PlaceOfOriginField extends MultiLineObjectField<List<PlaceOfOrigin>> implements Validatable, DependingOnFieldAbove<Nationality>, DemoEnabled {
	public static final boolean WITHOUT_ADD_ON = false;
	private final boolean withAddOn;
	private boolean swiss = true;
	
	public PlaceOfOriginField(Object key, boolean editable) {
		this(key, true, editable);
	}
	
	/* Bei Geburt können keine AddOns zu den PlaceOfOrigins mitgegeben werden.
	 * Das macht auch Sinn, denn die Heimatorte kommen alle per Abstammung und
	 * per Geburtstag zu der geborenen Person
	 */
	public PlaceOfOriginField(Object key, boolean withAddOn, boolean editable) {
		super(key, editable);
		this.withAddOn = withAddOn;
	}
	
	public class AddOriginEditor extends ObjectFieldPartEditor<PlaceOfOrigin> {
		@Override
		public FormVisual<PlaceOfOrigin> createForm() {
			return new OriginPanel(withAddOn, withAddOn);
		}

		@Override
		protected PlaceOfOrigin getPart(List<PlaceOfOrigin> object) {
			PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
			// TODO diese konstante sollte woanders herkommen
			placeOfOrigin.canton = PreferencesHelper.preferences().get(PlaceOfOrigin.PLACE_OF_ORIGIN.canton, null);
			placeOfOrigin.reasonOfAcquisition = "1";
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
		public FormVisual<PlaceOfOrigin> createForm() {
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
		public void actionPerformed(ActionEvent e) {
			getObject().remove(placeOfOrigin);
			fireObjectChange();
		}
	}
	
	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (swiss) {
			if (getObject().size() == 0) {
				resultList.add(new ValidationMessage(getName(), "Heimtort fehlt"));
			}
		}
	}

	@Override
	public String getNameOfDependedField() {
		return "nationality";
	}

	@Override
	public void setDependedField(EditField<Nationality> field) {
		swiss = true;
		if (field != null) {
			Nationality nationality = field.getObject();
			swiss = nationality.isSwiss();
		}
		getVisual().setEnabled(swiss);
		boolean changed = false;
		if (!swiss) {
			if (getObject() != null && getObject().size() > 0) {
				getObject().clear();
				changed = true;
			}
		}
		if (changed) {
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
	public FormVisual<List<PlaceOfOrigin>> createFormPanel() {
		// unused
		return null;
	}

	@Override
	protected void display(List<PlaceOfOrigin> objects) {
		clearVisual();
		for (PlaceOfOrigin placeOfOrigin : objects) {
			addObject(placeOfOrigin.displayHtml());
			if (isEditable()) {
				addAction(new EditorDialogAction(new EditOriginEditor(placeOfOrigin)));
				addAction(new RemoveOriginAction(placeOfOrigin));
				addGap();
			}
		}
		if (isEditable()) {
			addAction(new EditorDialogAction(new AddOriginEditor()));
		}
	}

}
