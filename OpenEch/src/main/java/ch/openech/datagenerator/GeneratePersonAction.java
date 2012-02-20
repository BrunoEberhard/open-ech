package ch.openech.datagenerator;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.swing.FrameManager;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;


public class GeneratePersonAction extends ResourceAction {

	private EchNamespaceContext echNamespaceContext;
	
	public GeneratePersonAction(EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		generate();
	}
	
	public void generate() {
		String input = (String)JOptionPane.showInputDialog(null, "Anzahl Personen: ",  "Eingabe", JOptionPane.INFORMATION_MESSAGE, null, null, 100);
		if (StringUtils.isBlank(input)) return;
		
		try {
			int count = Integer.parseInt(input);
			if (count > 0) {
				DataGenerator.generateData(echNamespaceContext, count, FrameManager.getInstance());
			} else {
				showErrorMessage(input);
			}
		} catch (NumberFormatException x) {
			showErrorMessage(input);
		}
	}
	
	private void showErrorMessage(String input) {
		JOptionPane.showMessageDialog(null, "Ungültige Eingabe: " + input, "Generierung nicht möglich", JOptionPane.ERROR_MESSAGE);
	}

}
