package ch.openech.client.xmlpreview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ch.openech.server.EchServer;
public class XmlSwingFrame extends JDialog {
	private JTabbedPane tabbedPane;
	private String xml;
	
	public XmlSwingFrame(Component parent, List<String> xmlList) {
		super(SwingUtilities.getWindowAncestor(parent), "Vorschau XML Ausgabe", Dialog.ModalityType.DOCUMENT_MODAL);
		setLayout(new BorderLayout());
		
		if (xmlList.isEmpty()) {
			setEmtpyMessage();
		} else if (xmlList.size() == 1) {
			xml = xmlList.get(0);
			showSingleXML(xml);
		} else {
			showMultiXml(xmlList);
		}
		
		pack();
		setLocationRelativeTo(null);
	}

	protected JPanel createButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addValidate(panel);
		return panel;
	}
	
	private void addValidate(Container container) {
		JButton button = new JButton("Validate");
		container.add(button);
		button.addActionListener(new ValidateXmlAction());
	}
	
	private class ValidateXmlAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (tabbedPane != null) {
				JScrollPane visibleScrollPane = (JScrollPane)tabbedPane.getSelectedComponent();
				JTextArea textArea = (JTextArea)visibleScrollPane.getViewport().getView();
				xml = textArea.getText();
			}
			
			String message = EchServer.validate(xml);
			JOptionPane.showMessageDialog(XmlSwingFrame.this, message);
		}
	}
	
	private void setEmtpyMessage() {
		add(createArea("Kein XML erzeugt"), BorderLayout.CENTER);
	}
	
	private void showSingleXML(String xml) {
		add(createArea(xml), BorderLayout.CENTER);
		String message = EchServer.validate(xml);
		add(createValidationArea(message), BorderLayout.SOUTH);
	}

	private void showMultiXml(List<String> xmlList) {
		tabbedPane = new JTabbedPane();
		for (int i = 0; i<xmlList.size(); i++) {
			JComponent area = createArea(xmlList.get(i));
			String tabName = "XML " + (i+1);
			tabbedPane.addTab(tabName, area);
		}
		add(tabbedPane, BorderLayout.CENTER);
		add(createButtons(), BorderLayout.SOUTH);
	}
	
	private JComponent createArea(String xml) {
		JTextArea textArea = new JTextArea();
		textArea.setText(xml);
		textArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setMinimumSize(new Dimension(600, 400));
		scrollPane.setPreferredSize(new Dimension(800, 600));
		
		return scrollPane;
	}
	
	private JComponent createValidationArea(String text) {
		JTextArea textArea = new JTextArea();
		textArea.setText(text);
		textArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setMinimumSize(new Dimension(600, 40));
		scrollPane.setPreferredSize(new Dimension(800, 80));
		
		return scrollPane;
	}
	
}
