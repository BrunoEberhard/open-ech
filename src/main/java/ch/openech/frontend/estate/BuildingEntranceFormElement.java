package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.estate.Building.BuildingEntrance;

public class BuildingEntranceFormElement extends ListFormElement<BuildingEntrance> {

	public BuildingEntranceFormElement(List<BuildingEntrance> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected void showEntry(BuildingEntrance entry) {
		if (isEditable()) {
			add(entry, new ListEntryEditor(entry), new RemoveEntryAction(entry));
		} else {
			add(entry);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddListEntryEditor() };
	}

	@Override
	protected Form<BuildingEntrance> createForm(boolean edit) {
		return new BuildingEntrancePanel();
	}
	
	public static class BuildingEntrancePanel extends Form<BuildingEntrance> {
		
		public BuildingEntrancePanel() {
			super(2);
			line(BuildingEntrance.$.EGAID, BuildingEntrance.$.EDID);
			line(BuildingEntrance.$.buildingEntranceNo, BuildingEntrance.$.status);
			// coordinates
			line(BuildingEntrance.$.localId.IdCategory, BuildingEntrance.$.localId.Id);
			line(BuildingEntrance.$.isMainAddress, BuildingEntrance.$.isOfficial);
			line(BuildingEntrance.$.locality);
			// Street
		}

	}

}