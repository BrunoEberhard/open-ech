package ch.openech.frontend.preferences;

import org.minimalj.application.Preferences;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;

public class PreferencesEditor extends Editor<OpenEchPreferences, Void> {
	
	public PreferencesEditor() {
	}

	@Override
	protected Form<OpenEchPreferences> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected OpenEchPreferences createObject() {
		return Preferences.getPreferences(OpenEchPreferences.class);
	}

	@Override
	protected Void save(OpenEchPreferences preferences) {
		Preferences.savePreferences(preferences);
		return null;
	}
	
}
