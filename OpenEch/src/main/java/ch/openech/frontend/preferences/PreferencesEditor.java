package ch.openech.frontend.preferences;

import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.edit.Editor;
import org.minimalj.frontend.edit.form.IForm;

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
	protected Object save(OpenEchPreferences object) {
		applicationContext.savePreferences(object);
		return SAVE_SUCCESSFUL;
	}
	
}
