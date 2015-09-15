package ch.openech.frontend.xmlpreview;

import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.form.Form;
import org.minimalj.model.Keys;

import ch.openech.frontend.ewk.event.XmlTextFormFormElement;

public class XmlPreview {

	public static void viewXml(List<String> xmls) {
//		if (Frontend.getInstance() instanceof SwingFrontend) {
//			SwingFrontend swingFrontend = (SwingFrontend) Frontend.getInstance();
//			XmlSwingFrame xmlFrame = new XmlSwingFrame(swingFrontend.get, xmls);
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
			Frontend.getBrowser().showDialog("XML", form.getContent(), null, null);
//		}
	}
	
	public static class XmlPreviewValue {
		public static final XmlPreviewValue $ = Keys.of(XmlPreviewValue.class);
		
		public List<String> xmls;
	}
	
}
