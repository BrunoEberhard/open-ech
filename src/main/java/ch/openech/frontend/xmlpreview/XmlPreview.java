package ch.openech.frontend.xmlpreview;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.model.Keys;

import ch.openech.frontend.ewk.event.XmlTextFormFormElement;

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
			XmlTextFormFormElement xmlTextFormField = new XmlTextFormFormElement(XmlPreviewValue.$.xmls);
			form.line(xmlTextFormField);
			xmlTextFormField.setValue(xmls);
			ClientToolkit.getToolkit().showDialog("XML", form.getContent(), null, null);
//		}
	}
	
	public static class XmlPreviewValue {
		public static final XmlPreviewValue $ = Keys.of(XmlPreviewValue.class);
		
		public List<String> xmls;
	}
	
}
