package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.common.NamedMetaData;

public class NamedMetaDataFormElement extends ListFormElement<NamedMetaData> {

	public NamedMetaDataFormElement(List<NamedMetaData> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected void showEntry(NamedMetaData entry) {
		if (isEditable()) {
			add(entry, new RemoveEntryAction(entry));
		} else {
			add(entry);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddListEntryEditor() };
	}

	@Override
	protected Form<NamedMetaData> createForm(boolean edit) {
		return new NamedMetaDataPanel();
	}
	
	public static class NamedMetaDataPanel extends Form<NamedMetaData> {
		
		public NamedMetaDataPanel() {
			line(NamedMetaData.$.metaDataName);
			line(NamedMetaData.$.metaDataValue);
		}

	}


}