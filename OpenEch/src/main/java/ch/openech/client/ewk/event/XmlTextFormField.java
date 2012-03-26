package ch.openech.client.ewk.event;

import java.awt.event.ActionEvent;

import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ContextLayout;
import ch.openech.mj.toolkit.IComponentDelegate;
import ch.openech.mj.toolkit.MultiLineTextField;
import ch.openech.server.EchServer;

public class XmlTextFormField implements FormField<String>, IComponentDelegate {
	private final MultiLineTextField textField;
	private final ContextLayout contextLayout;
	private final String xml;
	
	public XmlTextFormField(String xml) {
		this.xml = xml;
		textField = ClientToolkit.getToolkit().createMultiLineTextField();
		textField.addObject(xml);
		contextLayout = ClientToolkit.getToolkit().createContextLayout(textField);
		
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

	@Override
	public void setObject(String object) {
		//
	}

	@Override
	public String getName() {
		return "xml";
	}
}