package ch.openech.frontend.ewk.event;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.Keys;

import ch.openech.model.EchSchemaValidation;

public class XmlTextFormFormElement extends ObjectPanelFormElement<List<String>> {

	public XmlTextFormFormElement(List<String> key) {
		super(Keys.getProperty(key), false);
	}
	
	private class XmlValidateAction extends Action {
		public final String xml;
		
		public XmlValidateAction(String xml) {
			this.xml = xml;
		}
		
		@Override
		public void action() {
			String message = EchSchemaValidation.validate(xml);
			ClientToolkit.getToolkit().showMessage(message);
		}
	}

	private class CopyToClipboardAction extends Action {
		public final String xml;

		public CopyToClipboardAction(String xml) {
			this.xml = xml;
		}

		@Override
		public void action() {
			StringSelection stringSelection = new StringSelection(xml);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}

	@Override
	protected Form<List<String>> createFormPanel() {
		// not udes
		return null;
	}

	@Override
	protected void show(List<String> object) {
		for (String xml : object) {
			String original = xml;
			// Nene, multiline durch einzeller listen ersetzen?
			xml = xml.replace("&", "&amp;");
			xml = xml.replace("<", "&lt;");
			xml = xml.replace(">", "&gt;");
			xml = xml.replace("\n", "<br>");
			xml = xml.replace(" ", "&nbsp;");
			xml = "<html><code>" + xml + "</code></html>";
			addText(xml);
			addAction(new XmlValidateAction(original));
			addAction(new CopyToClipboardAction(original));
			addGap();
		}
	}

}
