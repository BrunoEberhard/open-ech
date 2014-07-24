package ch.openech.frontend.xmlpreview;

import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.model.Keys;

import ch.openech.frontend.ewk.event.XmlTextFormField;

public class XmlPreview {

	public static void viewXml(List<String> xmls) {
//		if (ClientToolkit.getToolkit() instanceof SwingClientToolkit) {
//			SwingClientToolkit swingClientToolkit = (SwingClientToolkit) ClientToolkit.getToolkit();
//			XmlSwingFrame xmlFrame = new XmlSwingFrame(swingClientToolkit.get, xmls);
//			xmlFrame.setVisible(true);
//		} else if (context instanceof com.vaadin.ui.Component) {
//			com.vaadin.ui.Component component = (com.vaadin.ui.Component) context;
//			XmlVaadinFrame xmlFrame = new XmlVaadinFrame(xmls);
//			Window window = component.getWindow();
//			while (window.getParent() != null) {
//				window = window.getParent();
//			}
//			window.addWindow(xmlFrame);
//			xmlFrame.setPositionY(0);
//			xmlFrame.setVisible(true);
//		} else {
			Form<XmlPreviewValue> form = new Form<XmlPreviewValue>(false) {
				@Override
				protected int getColumnWidthPercentage() {
					return 400;
				}
			};
			XmlTextFormField xmlTextFormField = new XmlTextFormField(XmlPreviewValue.XML_PREVIEW_VALUE.xmls);
			form.line(xmlTextFormField);
			xmlTextFormField.setObject(xmls);
			ClientToolkit.getToolkit().createDialog("XML", form.getContent()).openDialog();
//		}
	}
	
	public static class XmlPreviewValue {
		public static final XmlPreviewValue XML_PREVIEW_VALUE = Keys.of(XmlPreviewValue.class);
		
		public List<String> xmls;
	}
	
}
