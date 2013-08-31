package ch.openech.client.xmlpreview;

import java.util.List;

import ch.openech.client.ewk.event.XmlTextFormField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.model.Keys;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;

public class XmlPreview {

	public static void viewXml(IComponent source, List<String> xmls) {
		if (source instanceof java.awt.Component) {
			XmlSwingFrame xmlFrame = new XmlSwingFrame((java.awt.Component) source, xmls);
			xmlFrame.setVisible(true);
		} else if (source instanceof com.vaadin.ui.Component) {
			com.vaadin.ui.Component component = (com.vaadin.ui.Component) source;
			XmlVaadinFrame xmlFrame = new XmlVaadinFrame(xmls);
			component.getWindow().addWindow(xmlFrame);
			xmlFrame.setPositionY(0);
			xmlFrame.setVisible(true);
		} else {
			Form<XmlPreviewValue> form = new Form<XmlPreviewValue>(false) {
				@Override
				protected int getColumnWidthPercentage() {
					return 400;
				}
			};
			XmlTextFormField xmlTextFormField = new XmlTextFormField(XmlPreviewValue.XML_PREVIEW_VALUE.xmls);
			form.line(xmlTextFormField);
			xmlTextFormField.setObject(xmls);
			ClientToolkit.getToolkit().createDialog(source, "XML", form.getComponent()).openDialog();
		}
	}
	
	public static class XmlPreviewValue {
		public static final XmlPreviewValue XML_PREVIEW_VALUE = Keys.of(XmlPreviewValue.class);
		
		public List<String> xmls;
	}
	
}
