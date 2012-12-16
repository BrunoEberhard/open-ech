package ch.openech.client.preferences;

import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences.ApplicationSchemaData;
import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;

public class PreferencesEditor extends Editor<OpenEchPreferences> {
	private final ApplicationContext applicationContext;
	
	public PreferencesEditor(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected IForm<OpenEchPreferences> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected OpenEchPreferences load() {
		return (OpenEchPreferences) applicationContext.getPreferences();
	}

	@Override
	protected boolean save(OpenEchPreferences object) {
		applicationContext.savePreferences(object);
		return true;
	}
	
}
