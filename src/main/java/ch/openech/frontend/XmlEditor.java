package ch.openech.frontend;

import java.util.List;
import java.util.logging.Logger;

import org.minimalj.application.DevMode;
import org.minimalj.frontend.edit.Editor;
import org.minimalj.frontend.toolkit.Action;

import ch.openech.frontend.xmlpreview.XmlPreview;
import ch.openech.xml.write.EchSchema;


public abstract class XmlEditor<T> extends Editor<T> {
	public static final Logger logger = Logger.getLogger(XmlEditor.class.getName());
	protected final EchSchema echSchema;
	
	private final XmlAction xmlAction;

	public XmlEditor(EchSchema echSchema) {
		this.echSchema = echSchema;
		this.xmlAction = new XmlAction();
	}

	protected int getFormColumns() {
		return 1;
	}

	public abstract List<String> getXml(T object) throws Exception;
	
	public void generateSedexOutput(T object) throws Exception {
		// to overwrite
	}

	@Override
	public Action[] getActions() {
		if (DevMode.isActive()) {
			return new Action[]{demoAction, xmlAction, cancelAction, saveAction};
		} else {
			return new Action[]{cancelAction, saveAction};
		}
	}

	private class XmlAction extends Action {
		
		@Override
		public void action() {
			try {
				List<String> xmls = getXml(getObject());
				XmlPreview.viewXml(xmls);
			} catch (Exception x) {
				throw new RuntimeException("XML Preview fehlgeschlagen", x);
			}
		}
		
//		@Override
//		public void setValidationMessages(List<ValidationMessage> validationMessages) {
//			enabled = validationMessages.isEmpty();
//			updateXmlStatus();
//			if (changeListener != null) {
//				changeListener.change();
//			}
//		}	
//		
//		private void updateXmlStatus() {
//			if (!isEnabled()) return;
//			try {
//				List<String> xmls = getXml(getObject());
//				String result = "ok";
//				for (String string : xmls) {
//					result = EchSchemaValidation.validate(string);
//					if (!EchSchemaValidation.OK.equals(result)) {
//						break;
//					}
//				}
//				setDescription(result);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//				description = "XML Validierung mit Exception fehlgeschlagen";
//			}
//		}
	}


}
