package ch.openech.frontend.xmlpreview;

import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.IDialog;
import org.minimalj.model.Keys;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.ewk.event.XmlTextFormFormElement;

public class XmlPreview {

	private IDialog dialog;
	
	public XmlPreview(List<String> xmls) {
        Form<XmlPreviewValue> form = new Form<XmlPreviewValue>(Form.READ_ONLY) {
			@Override
			protected int getColumnWidthPercentage() {
				return 400;
			}
		};
		XmlTextFormFormElement xmlTextFormField = new XmlTextFormFormElement(XmlPreviewValue.$.xmls);
		form.line(xmlTextFormField);
		xmlTextFormField.setValue(xmls);
		
		Action closeAction = new Action(Resources.getString("CancelAction")) {
			@Override
			public void action() {
				dialog.closeDialog();
			}
		};
		dialog = Frontend.showDialog("XML", form.getContent(), null, closeAction, closeAction);
	}
	
	public static class XmlPreviewValue {
		public static final XmlPreviewValue $ = Keys.of(XmlPreviewValue.class);
		
		public List<String> xmls;
	}
	
}
