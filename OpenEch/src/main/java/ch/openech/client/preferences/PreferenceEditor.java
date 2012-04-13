package ch.openech.client.preferences;

import java.util.List;

import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.swing.PreferencesHelper;

public class PreferenceEditor extends Editor<PreferenceData> {

	@Override
	protected FormVisual<PreferenceData> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected PreferenceData newInstance() {
		PreferenceData data = new PreferenceData();
		PreferencesHelper.load(data);
		return data;
	}

	@Override
	protected void validate(PreferenceData object, List<ValidationMessage> resultList) {
		// not used
	}

	@Override
	protected boolean save(PreferenceData object) {
		try {
			PreferencesHelper.save(object);
			return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}

}
