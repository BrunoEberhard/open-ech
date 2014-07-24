package ch.openech.frontend.ewk;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.page.AbstractPage;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.RefreshablePage;
import org.minimalj.frontend.toolkit.ClientToolkit.IContent;

import ch.openech.model.SedexMessageInformation;
import ch.openech.model.person.Person;
import ch.openech.transaction.EchPersistence;
import ch.openech.xml.write.EchSchema;

public class SedexInputPage extends AbstractPage implements RefreshablePage {

	private final EchSchema echNamespaceContext;
	private JPanel panel;
	private SedexInputTable table;

	public SedexInputPage(String echVersion) {
		this(EchSchema.getNamespaceContext(20, echVersion));
	}
	
	public SedexInputPage(EchSchema echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}

	@Override
	public IContent getContent() {
		if (panel == null) {
			createPanel();
		}
		refresh();
		return null;
		// TODO
//		return panel;
	}
	
	@Override
	public String getTitle() {
		return "Sedex Input";
	}

	@Override
	public ActionGroup getMenu() {
		return null;
	}

	private void createPanel() {
		panel = new JPanel(new BorderLayout());
		table = new SedexInputTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (e.getClickCount() >= 2) {
						int row = table.rowAtPoint(e.getPoint());
						showRow(row);
					}
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\n' && table.getSelectedRow() >= 0) {
					showRow(table.getSelectedRow());
					e.consume();
				}
			}
		});
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	private void showRow(int row) {
		try {
			if (row >= 0 && row < table.getRowCount()) {
				SedexMessageInformation envelope = (SedexMessageInformation)table.get(row);

				if ("moveOut".equals(envelope.type)) {
					showMoveInPage(envelope);
				} else if ("moveIn".equals(envelope.type)) {
					showMoveOutPage(envelope);
				} else if ("death".equals(envelope.type)) {
					showDeathPage(envelope);
				} else {
					JOptionPane.showMessageDialog(null, "Diese Nachricht kann nicht bearbeitet werden.");
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	private void showMoveInPage(SedexMessageInformation envelope) {
		// TODO
		
//		String message = "Bei der Nachricht handelt es sich um einen Wegzug.\n\nSoll der entsprechende Zuzug erfasst werden?";
//		int answer = showOptionDialog(message);
//		if (answer == JOptionPane.YES_OPTION) {
//			Person person = SedexPersonLoader.readPerson(envelope);
//			MoveInWizard moveInPage = new MoveInWizard(echNamespaceContext, person);
//			show(new EditorPage(moveInPage));
//		}
	}

	private void showMoveOutPage(SedexMessageInformation envelope) throws SQLException {
		// TODO

//		Person person = searchPerson(envelope);
//		if (person != null) {
//			String message = "Bei der Nachricht handelt es sich um einen Zuzug.\n\nSoll der entsprechende Wegzug erfasst werden?";
//			int answer = showOptionDialog(message);
//			if (answer == JOptionPane.YES_OPTION) {
//				
//				show(new PersonViewPage(getPageContext(), echNamespaceContext.getVersion(), person.getId()));
//			}
//		} else {
//			JOptionPane.showMessageDialog(null, "Person mit entsprechender Identifikation nicht vorhanden");
//		}
	}

	private void showDeathPage(SedexMessageInformation envelope) throws SQLException {
		// TODO
		
//		Person person = searchPerson(envelope);
//		if (person != null) {
//			String message = "Bei der Nachricht handelt es sich um einen Todesfall.\n\nSoll der entsprechende Fall erfasst werden?";
//			int answer = showOptionDialog(message);
//			if (answer == JOptionPane.YES_OPTION) {
//				PersonViewPage page = new PersonViewPage(echNamespaceContext, person.getId());
//				show(page);
//				// TODO showDeathPage öffnen des Tod - Dialoges richtig kapseln
//				Editor<?> editor = new ChangeWithSecondPersonEvent.DeathEvent(echNamespaceContext);
//				FormVisual<?> form = editor.startEditor();
//				ClientToolkit.getToolkit().openDialog(getPanel(), form, "TODO");
//			}
//		} else {
//			JOptionPane.showMessageDialog(null, "Person mit entsprechender Identifikation nicht vorhanden");
//		}
	}
	
	private int showOptionDialog(String message) {
		int answer = JOptionPane.showOptionDialog(table, message, "Verarbeitung Nachricht", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		return answer;
	}

	private Person searchPerson(SedexMessageInformation envelope) throws SQLException {
		return EchPersistence.getByIdentification(Backend.getInstance(), envelope.personIdentification);
	}
	
	@Override
	public void refresh() {
		table.refresh();
	}

}
