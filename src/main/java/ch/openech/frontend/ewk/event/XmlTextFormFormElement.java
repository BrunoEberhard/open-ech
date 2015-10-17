package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.EchSchemaValidation;

public class XmlTextFormFormElement extends ListFormElement<String> {

	public XmlTextFormFormElement(List<String> key) {
		super(Keys.getProperty(key), true);
	}
	
	private class XmlValidateAction extends Action {
		public final String xml;
		
		public XmlValidateAction(String xml) {
			this.xml = xml;
		}
		
		@Override
		public void action() {
			String message = EchSchemaValidation.validate(xml);
			Frontend.showMessage(message);
		}
	}

	@Override
	protected Form<List<String>> createFormPanel() {
		// not udes
		return null;
	}

	@Override
	protected void showEntry(String entry) {
		addTextArea(entry, new XmlValidateAction(entry));
	}

}
