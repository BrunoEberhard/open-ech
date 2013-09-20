package ch.openech.client.ewk;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import ch.openech.mj.swing.SwingResourceAction;


public class HelpAction extends SwingResourceAction {
	private static final long serialVersionUID = 1L;
	
	private static JFrame helpFrame;

	public void actionPerformed(ActionEvent actionEvent) {
		try {
			if (helpFrame == null) {
				JEditorPane pane = new JEditorPane();
				pane.setEditable(false);
				pane.setContentType("text/html");
				pane.setPage(HelpAction.class.getResource("resources/anleitung.html"));
				helpFrame = new JFrame("Hilfe");
				helpFrame.setSize(500, 600);
				helpFrame.setLocationRelativeTo(null);
				helpFrame.setContentPane(new JScrollPane(pane));
			}
			helpFrame.setVisible(true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Hilfe kann nicht angezeigt werden.");
			e.printStackTrace();
		}
	}

}
