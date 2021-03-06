package ch.openech.frontend.ewk.event;

import java.util.Collections;
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
		public void run() {
			String message = EchSchemaValidation.validate(xml);
			Frontend.showMessage(message);
		}
	}

	@Override
	protected Form<String> createForm() {
		// not used
		return null;
	}

	@Override
	protected List<Action> getActions(String entry) {
		return Collections.singletonList(new XmlValidateAction(entry));
	}
}
