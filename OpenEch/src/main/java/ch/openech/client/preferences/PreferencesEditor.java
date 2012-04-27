package ch.openech.client.preferences;

import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.Action;

import ch.openech.client.ewk.OpenEchApplication;
import ch.openech.client.preferences.OpenEchPreferences.ApplicationSchemaData;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.swing.PreferencesHelper;
import ch.openech.mj.util.StringUtils;

public class PreferencesEditor extends Editor<OpenEchPreferences> {

	@Override
	protected FormVisual<OpenEchPreferences> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected OpenEchPreferences load() {
		return (OpenEchPreferences) context.getApplicationContext().getPreferences();
	}

	@Override
	protected void validate(OpenEchPreferences data, List<ValidationMessage> resultList) {
		if (StringUtils.isEmpty(data.applicationSchemaData.schema20) && StringUtils.isEmpty(data.applicationSchemaData.schema93) && StringUtils.isEmpty(data.applicationSchemaData.schema148)) {
			resultList.add(new ValidationMessage(ApplicationSchemaData.APPLICATION_SCHEMA_DATA.schema20, "Mindestens ein Schema ist erforderlich"));
		}
	}

	@Override
	protected boolean save(OpenEchPreferences object) {
		PreferencesHelper.save(Preferences.userNodeForPackage(OpenEchApplication.class), object);
		context.getApplicationContext().setPreferences(object);
		return true;
	}

	@Override
	public Action[] getActions() {
		// ignore demoAction, doesnt make sense in Preferences
		return new Action[]{cancelAction, saveAction};
	}
	
}
