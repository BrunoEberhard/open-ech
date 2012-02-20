package ch.openech.client.ewk.event;

import java.awt.event.ActionEvent;

import ch.openech.mj.edit.fields.TextFormField;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ContextLayout;
import ch.openech.server.EchServer;

public class XmlTextFormField extends TextFormField {
	private final ContextLayout contextLayout;
	private final String xml;
	
	public XmlTextFormField(String xml) {
		super("xml", null);
		this.xml = xml;
		contextLayout = ClientToolkit.getToolkit().createContextLayout(this);
		contextLayout.setActions(new XmlValidateAction());
	}
	
	@Override
	public Object getComponent() {
		return contextLayout;
	}

	private class XmlValidateAction extends ResourceAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = EchServer.validate(xml);
			ClientToolkit.getToolkit().showNotification(XmlTextFormField.this, message);
		}
	}
}