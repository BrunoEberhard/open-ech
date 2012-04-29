package ch.openech.client.ewk.event;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.server.EchServer;

public class XmlTextFormField extends ObjectFlowField<List<String>> {
	
	public XmlTextFormField(Object key) {
		super(key, false);
	}

	private class XmlValidateAction extends ResourceAction {
		public final String xml;
		
		public XmlValidateAction(String xml) {
			this.xml = xml;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = EchServer.validate(xml);
			ClientToolkit.getToolkit().showNotification(XmlTextFormField.this, message);
		}
	}

	private class CopyToClipboardAction extends ResourceAction {
		public final String xml;

		public CopyToClipboardAction(String xml) {
			this.xml = xml;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			StringSelection stringSelection = new StringSelection(xml);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}

	@Override
	protected IForm<List<String>> createFormPanel() {
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
			addObject(xml);
			addAction(new XmlValidateAction(original));
			addAction(new CopyToClipboardAction(original));
			addGap();
		}
	}

}