package ch.openech.client.ewk;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.client.xmlpreview.XmlPreview;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;
import ch.openech.xml.write.EchNamespaceContext;


public abstract class XmlEditor<T> extends Editor<T> {
	public static final Logger logger = Logger.getLogger(PersonEventEditor.class.getName());
	private final EchNamespaceContext echNamespaceContext;
	private final XmlAction xmlAction;
	
	public XmlEditor(EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
		this.xmlAction = new XmlAction();
		setIndicator(xmlAction);
	}
	
	protected EchNamespaceContext getEchNamespaceContext() {
		return echNamespaceContext;
	}

	protected int getFormColumns() {
		return 1;
	}

	@Override
	public void validate(T object, List<ValidationMessage> resultList) {
		// to be overwritten
	}

	public abstract List<String> getXml(T object) throws Exception;
	
	public void generateSedexOutput(T object) throws Exception {
		// to overwrite
	}

	// TODO momentan hört der XML Button nicht auf die Änderungen in den Preferences
//	@Override
//	public EditorPanel<T> createEditorPanel() {
//		EditorPanel<T> panel = super.createEditorPanel();
//		xmlAction.addEditorControlPreferenceChangeListener(panel);
//		return panel;
//	}

	@Override
	public Action[] getActions() {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		if (preferences.devMode()) {
			return new Action[]{demoAction(), xmlAction, cancelAction(), saveAction()};
		} else {
			return new Action[]{cancelAction(), saveAction()};
		}
	}

	@Override
	public boolean save(T object) {
		List<String> xmls;
		try {
			xmls = getXml(object);
			send(xmls);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Konnte XML nicht erstellen", e);
		}
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
	
	private class XmlAction extends AbstractAction implements Indicator {
		public XmlAction() {
			super("Vorschau XML");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				List<String> xmls = getXml(getObject());
				XmlPreview.viewXml(context, xmls);
			} catch (Exception x) {
				throw new RuntimeException("XML Preview fehlgeschlagen", x);
			}
		}
		
		@Override
		public void setValidationMessages(List<ValidationMessage> validationMessages) {
			xmlAction.setEnabled(validationMessages.isEmpty());
			updateXmlStatus();
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
				putValue("foreground", (ok ? Color.BLACK : Color.RED));
				putValue(AbstractAction.SHORT_DESCRIPTION, result);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				putValue("foreground",Color.BLUE);
				putValue(AbstractAction.SHORT_DESCRIPTION, e1.getMessage());
			}
		}
	}
	
//		private class EditorControlPreferenceChangeListener extends ComponentAdapter implements PreferenceChangeListener {
//
//		    @Override
//			public void componentShown(ComponentEvent e) {
//		    	context.getPreferences().addPreferenceChangeListener(this);
//		    }
//		    
//		    @Override
//			public void componentHidden(ComponentEvent e) {
//		    	context.getPreferences().removePreferenceChangeListener(this);
//		    }
//		    
//			@Override
//			public void preferenceChange(PreferenceChangeEvent evt) {
//				updateXmlButtonVisibility();
//			}
//		}

}
