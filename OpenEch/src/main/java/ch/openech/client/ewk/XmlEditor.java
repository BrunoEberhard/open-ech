package ch.openech.client.ewk;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.client.ewk.event.XmlTextFormField;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.swing.PreferencesHelper;
import ch.openech.mj.toolkit.ClientToolkit;
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
		return new Action[]{xmlAction, cancelAction, saveAction};
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
	
	@Override
	protected void indicate(List<ValidationMessage> validationResult) {
		super.indicate(validationResult);
		xmlAction.setEnabled(validationResult.isEmpty());
		updateXmlStatus();
	}
	
	
	private void updateXmlStatus() {
		if (!xmlAction.isEnabled()) return;
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
			xmlAction.putValue("foreground", (ok ? Color.BLACK : Color.RED));
			xmlAction.putValue(AbstractAction.SHORT_DESCRIPTION, result);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			xmlAction.putValue("foreground",Color.BLUE);
			xmlAction.putValue(AbstractAction.SHORT_DESCRIPTION, e1.getMessage());
		}
	}
	
	// public for access on xmls
	public class XmlAction extends AbstractAction {
		public List<String> xmls;
		
		public XmlAction() {
			super("Vorschau XML");
		}
		
		public void addEditorControlPreferenceChangeListener(Component component) {
			component.addComponentListener(new EditorControlPreferenceChangeListener());
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			showXML();
		}
		
		private void updateXmlButtonVisibility() {
			boolean visible = PreferencesHelper.preferences().getBoolean("showXml", false);
			putValue("visible", visible);
		}
		
		private void showXML() {
			try {
				xmls = getXml(getObject());
			} catch (Exception x) {
				throw new RuntimeException("XML Generierung fehlgeschlagen", x);
			}
			
			AbstractFormVisual<XmlAction> form = new AbstractFormVisual(XmlAction.class, null, false) {
				@Override
				protected int getColumnWidthPercentage() {
					return 1000;
				}
			};
			form.area(new XmlTextFormField("xmls"));
			form.setObject(this);
			ClientToolkit.getToolkit().openDialog(null, form, "XML").openDialog();
		}

		private class EditorControlPreferenceChangeListener extends ComponentAdapter implements PreferenceChangeListener {

		    public void componentShown(ComponentEvent e) {
		    	PreferencesHelper.preferences().addPreferenceChangeListener(this);
		    }
		    
		    public void componentHidden(ComponentEvent e) {
		    	PreferencesHelper.preferences().removePreferenceChangeListener(this);
		    }
		    
			@Override
			public void preferenceChange(PreferenceChangeEvent evt) {
				updateXmlButtonVisibility();
			}
		}
	}

}
