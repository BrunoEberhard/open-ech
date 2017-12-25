package ch.openech.frontend.estate;

import static ch.openech.model.estate.Building.$;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.frontend.e44.NamedIdPanel;
import ch.openech.model.common.NamedId;
import ch.openech.model.estate.Building;

public class BuildingForm extends Form<Building> {

	public BuildingForm(boolean editable) {
		super(editable, 4);
		
		line($.EGID, $.identificationType);
		line($.street, $.houseNumber, $.zipCode, $.nameOfBuilding);
		line($.EGRID, $.cadasterAreaNumber, $.number, $.realestateType);
		line($.officialBuildingNo);
		
		line($.municipality, new LocalIDFormElement($.localID, editable));
	}
	
	
	// merge with TechnicalIdsFormElement ?
	private static class LocalIDFormElement extends ListFormElement<NamedId> {

		public LocalIDFormElement(List<NamedId> key, boolean editable) {
			super(Keys.getProperty(key), editable);
		}
		
		@Override
		protected void showEntry(NamedId entry) {
			if (isEditable()) {
				add(entry, new RemoveOtherIdAction(entry));
			} else {
				add(entry);
			}
		}
		
		@Override
		protected Action[] getActions() {
			return new Action[] { new AddListEntryEditor() };
		}

		@Override
		protected Form<NamedId> createForm(boolean edit) {
			return new NamedIdPanel();
		}
		
		private class RemoveOtherIdAction extends Action {
			private final NamedId id;
			
			public RemoveOtherIdAction(NamedId id) {
				this.id = id;
			}

			@Override
			public void action() {
				LocalIDFormElement.this.getValue().remove(id);
				setValue(getValue());
			}
		}
				
	}

}
