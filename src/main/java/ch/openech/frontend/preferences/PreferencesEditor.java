package ch.openech.frontend.preferences;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;

import ch.openech.transaction.EchRepository;

public class PreferencesEditor extends Editor<OpenEchPreferences, Void> {
	
	public PreferencesEditor() {
	}

	@Override
	protected Form<OpenEchPreferences> createForm() {
		return new PreferenceForm();
	}
	
	@Override
	protected OpenEchPreferences createObject() {
		return EchRepository.getPreferences();
	}

	@Override
	protected Void save(OpenEchPreferences preferences) {
		EchRepository.savePreferences(preferences);
		return null;
	}
	
}
