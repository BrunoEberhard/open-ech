package ch.openech.client.preferences;

import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.IForm;

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
