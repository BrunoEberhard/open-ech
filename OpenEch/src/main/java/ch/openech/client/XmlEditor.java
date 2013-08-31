package ch.openech.client;

import java.util.List;
import java.util.logging.Logger;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.client.xmlpreview.XmlPreview;
import ch.openech.mj.application.DevMode;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;
import ch.openech.xml.write.EchSchema;


public abstract class XmlEditor<T> extends Editor<T> {
	public static final Logger logger = Logger.getLogger(PersonEventEditor.class.getName());
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

	@Override
	public Object save(T object) throws Exception {
		List<String> xmls = getXml(object);
		send(xmls);
		return SAVE_SUCCESSFUL;
	}

	public static boolean send(final List<String> xmls) {
		for (String xml : xmls) {
			if (!send(xml)) {
				return false;
			}
		}
		// TODO
//		if (SedexOutputGenerator.sedexOutputDirectoryAvailable()) {
//			try {
//				generateSedexOutput();
//			} catch (Exception e) {
//				throw new RuntimeException("Fehler bei Sedex Meldung erstellen", e);
//			}
//		}
		return true;
	}
	
	public static boolean send(final String xml) {
		ServerCallResult result = EchServer.getInstance().process(xml);
		if (result.exception != null) {
			throw new RuntimeException("Speichern Fehlgeschlagen: " + result.exception.getMessage(), result.exception);
		}
		return true;
	}
	
	private class XmlAction extends ResourceAction implements Indicator {
		
		private boolean enabled = true;
		
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
		}	
		
		@Override
		public boolean isEnabled() {
			return enabled;
		}

		private void updateXmlStatus() {
			if (!isEnabled()) return;
			try {
				List<String> xmls = getXml(getObject());
				boolean ok = true;
				String result = "ok";
				for (String string : xmls) {
					result = EchServer.validate(string);
					if (!"ok".equals(result)) {
						ok = false;
						break;
					}
				}
//				putValue("foreground", (ok ? Color.BLACK : Color.RED));
//				putValue(AbstractAction.SHORT_DESCRIPTION, result);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
//				putValue("foreground",Color.BLUE);
//				putValue(AbstractAction.SHORT_DESCRIPTION, e1.getMessage());
			}
		}
	}


}
