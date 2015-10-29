package ch.openech.frontend.preferences;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;

import ch.openech.transaction.EchPersistence;

public class PreferencesEditor extends Editor<OpenEchPreferences, Void> {
	
	public PreferencesEditor() {
	}

	@Override
	protected Form<OpenEchPreferences> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected OpenEchPreferences createObject() {
		return EchPersistence.getPreferences();
	}

	@Override
	protected Void save(OpenEchPreferences preferences) {
		EchPersistence.savePreferences(preferences);
		return null;
	}
	
}
