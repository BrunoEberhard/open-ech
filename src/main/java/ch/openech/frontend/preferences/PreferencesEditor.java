package ch.openech.frontend.preferences;

import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;

public class PreferencesEditor extends Editor<OpenEchPreferences, Void> {
	private final ApplicationContext applicationContext;
	
	public PreferencesEditor(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Form<OpenEchPreferences> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected OpenEchPreferences createObject() {
		return (OpenEchPreferences) applicationContext.getPreferences();
	}

	@Override
	protected Void save(OpenEchPreferences object) {
		applicationContext.savePreferences(object);
		return null;
	}
	
}
