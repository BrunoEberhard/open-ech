package ch.openech.client.xmlpreview;


import java.util.List;

import ch.openech.dm.EchSchemaValidation;

import com.vaadin.terminal.gwt.client.ui.AlignmentInfo.Bits;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
public class XmlVaadinFrame extends Window {
	private static final long serialVersionUID = 1L;
	
	private TabSheet tabSheet;
	private String xml;
	
	public XmlVaadinFrame(List<String> xmlList) {
		super("Vorschau XML");
		
		if (xmlList.isEmpty()) {
			setEmtpyMessage();
		} else if (xmlList.size() == 1) {
			xml = xmlList.get(0);
			showSingleXML(xml);
		} else {
			showMultiXml(xmlList);
		}
		
		setWidth("800px");
	}

	private void setEmtpyMessage() {
		GridLayout grid = new GridLayout(1, 1);
		Label label = new Label("Kein XML erzeugt");
		grid.addComponent(label);
		grid.setComponentAlignment(label, new Alignment(Bits.ALIGNMENT_VERTICAL_CENTER
				| Bits.ALIGNMENT_HORIZONTAL_CENTER));
		addComponent(grid);
	}
	
	private void showSingleXML(String xml) {
		VerticalLayout layout = createArea(xml);
		addComponent(layout);	
	}

	private void showMultiXml(List<String> xmlList) {
		tabSheet = new TabSheet();
		for (int i = 0; i<xmlList.size(); i++) {
			VerticalLayout layout = createArea(xmlList.get(i));
			String tabName = "XML " + (i+1);
			tabSheet.addTab(layout, tabName);
		}
		addComponent(tabSheet);
	}
	
	private VerticalLayout createArea(String xml) {
		VerticalLayout layout = new VerticalLayout();
		
		Label xmlLabel = new Label(xml);
		xmlLabel.setContentMode(Label.CONTENT_PREFORMATTED);
		layout.addComponent(xmlLabel);
		layout.setExpandRatio(xmlLabel, 1);
	        
		String message = EchSchemaValidation.validate(xml);
		Label messageLabel = new Label(message);
		messageLabel.setContentMode(Label.CONTENT_DEFAULT);
		layout.addComponent(messageLabel);
		layout.setExpandRatio(messageLabel, 0);
		
		return layout;
	}
	
}
