package ch.openech.client.e11;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.swing.PreferencesHelper;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualList;

public class PlaceOfOriginField extends ObjectField<List<PlaceOfOrigin>> implements Validatable, DependingOnFieldAbove<Nationality>, DemoEnabled {
	public static final boolean WITHOUT_ADD_ON = false;
	private final boolean withAddOn;
	private final VisualList list;
	private boolean swiss = true;
	
	public PlaceOfOriginField(Object key, boolean editable) {
		this(key, true, editable);
	}
	
	/* Bei Geburt können keine AddOns zu den PlaceOfOrigins mitgegeben werden.
	 * Das macht auch Sinn, denn die Heimatorte kommen alle per Abstammung und
	 * per Geburtstag zu der geborenen Person
	 */
	public PlaceOfOriginField(Object key, boolean withAddOn, boolean editable) {
		super(key);
		this.withAddOn = withAddOn;
		
		list = ClientToolkit.getToolkit().createVisualList();
		if (editable) {
			createMenu();
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return list;
	}

	private void createMenu() {
		addAction(new AddOriginEditor());
		addAction(new EditOriginEditor());
		addAction(new RemoveOriginAction());
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
		private int index;
		
		@Override
		public FormVisual<PlaceOfOrigin> createForm() {
			return new OriginPanel(withAddOn, withAddOn);
		}

		@Override
		protected PlaceOfOrigin getPart(List<PlaceOfOrigin> object) {
			index = list.getSelectedIndex();
			if (index >= 0) {
				return object.get(index);
			} else {
				throw new IllegalStateException();
			}
		}

		@Override
		protected void setPart(List<PlaceOfOrigin> object, PlaceOfOrigin p) {
			object.set(list.getSelectedIndex(), p);
			fireObjectChange();
		}
	}

	private class RemoveOriginAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object selectedObject = list.getSelectedObject();
			if (selectedObject != null) {
				getObject().remove(selectedObject);
				fireObjectChange();
			}
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
		list.setEnabled(swiss);
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
		list.setObjects(objects);
	}

}
