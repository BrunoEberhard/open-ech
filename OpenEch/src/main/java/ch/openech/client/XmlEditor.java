package ch.openech.client;

import java.util.List;
import java.util.logging.Logger;

import ch.openech.client.xmlpreview.XmlPreview;
import ch.openech.dm.EchSchemaValidation;
import ch.openech.mj.application.DevMode;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;
import ch.openech.xml.write.EchSchema;


public abstract class XmlEditor<T> extends Editor<T> {
	public static final Logger logger = Logger.getLogger(XmlEditor.class.getName());
	protected final EchSchema echSchema;
	
	private final XmlAction xmlAction;

	public XmlEditor(EchSchema echSchema) {
//		super(pageContext);
		this.echSchema = echSchema;
		this.xmlAction = new XmlAction();
		setIndicator(xmlAction);
	}

	protected int getFormColumns() {
		return 1;
	}

	public abstract List<String> getXml(T object) throws Exception;
	
	public void generateSedexOutput(T object) throws Exception {
		// to overwrite
	}

	@Override
	public IAction[] getActions() {
		if (DevMode.isActive()) {
			return new IAction[]{demoAction, xmlAction, cancelAction, saveAction};
		} else {
			return new IAction[]{cancelAction, saveAction};
		}
	}

	private class XmlAction extends ResourceAction implements Indicator {
		
		private boolean enabled = true;
		private ActionChangeListener changeListener;
		private String description;
		
		@Override
		public void action(IComponent context) {
			try {
				List<String> xmls = getXml(getObject());
				XmlPreview.viewXml(context, xmls);
			} catch (Exception x) {
				throw new RuntimeException("XML Preview fehlgeschlagen", x);
			}
		}
		
		@Override
		public void setValidationMessages(List<ValidationMessage> validationMessages) {
			enabled = validationMessages.isEmpty();
			updateXmlStatus();
			if (changeListener != null) {
				changeListener.change();
			}
		}	
		
		@Override
		public boolean isEnabled() {
			return enabled;
		}

		@Override
		public void setChangeListener(ActionChangeListener changeListener) {
			this.changeListener = changeListener;
		}
		
		@Override
		public String getDescription() {
			return description;
		}

		private void updateXmlStatus() {
			if (!isEnabled()) return;
			try {
				List<String> xmls = getXml(getObject());
				String result = "ok";
				for (String string : xmls) {
					result = EchSchemaValidation.validate(string);
					if (!EchSchemaValidation.OK.equals(result)) {
						break;
					}
				}
				description = result;
			} catch (Exception e1) {
				e1.printStackTrace();
				description = "XML Validierung mit Exception fehlgeschlagen";
			}
		}
	}


}
