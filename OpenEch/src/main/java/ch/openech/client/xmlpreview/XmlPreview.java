package ch.openech.client.xmlpreview;

import java.util.List;

import ch.openech.client.ewk.event.XmlTextFormField;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.toolkit.ClientToolkit;

public class XmlPreview {

	public static void viewXml(PageContext context, List<String> xmls) {
		if (context instanceof java.awt.Component) {
			XmlSwingFrame xmlFrame = new XmlSwingFrame((java.awt.Component) context, xmls);
			xmlFrame.setVisible(true);
		} else if (context instanceof com.vaadin.ui.Component) {
			com.vaadin.ui.Component component = (com.vaadin.ui.Component) context;
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
			form.area(xmlTextFormField);
			xmlTextFormField.setObject(xmls);
			ClientToolkit.getToolkit().openDialog(context, form.getComponent(), "XML").openDialog();
		}
	}
	
	public static class XmlPreviewValue {
		public static final XmlPreviewValue XML_PREVIEW_VALUE = Constants.of(XmlPreviewValue.class);
		
		public List<String> xmls;
	}
	
}
