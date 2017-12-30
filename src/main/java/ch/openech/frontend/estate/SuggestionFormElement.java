package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.element.AbstractFormElement;

import ch.openech.model.EchFormats;

public class SuggestionFormElement extends AbstractFormElement<String> {

	private final Input<String> textField;
	private final List<String> suggestionList;
	
	public SuggestionFormElement(String key, List<String> suggestionList, boolean editable) {
		super(key);
		this.suggestionList = suggestionList;
		if (editable) {
			textField = Frontend.getInstance().createTextField(EchFormats.applicationType, null, new FixedSuggestion(), listener());
		} else {
			textField = Frontend.getInstance().createReadOnlyTextField();
		}
	}
	
	@Override
	public void setValue(String value) {
		textField.setValue(value);
	}

	@Override
	public String getValue() {
		return textField.getValue();
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	private class FixedSuggestion implements Search<String> {

		@Override
		public List<String> search(String query) {
			return suggestionList;
		}
	}
}
