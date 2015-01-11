package ch.openech.frontend.ewk;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.minimalj.model.properties.FlatProperties;

import  ch.openech.model.Envelope;
import ch.openech.sedex.SedexDirectoryScanner;

public class SedexInputTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	private List<Envelope> envelopes = new ArrayList<Envelope>();
	private AbstractTableModel tableModel;

	private static String[] COLUMNS = {"messageId", "senderId", "eventDate", "type", "officialName", "firstName", "dateOfBirth"};

	private static String[] NAMES = { "Id", "Sender", "Ereignisdatum", "Ereignis", "Name", "Vorname", "Geburtsdatum"};

	public SedexInputTable() {
		super();

		tableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int row, int column) {
				Object value = FlatProperties.getValue(envelopes.get(row), COLUMNS[column]);
				return value;
			}

			@Override
			public int getColumnCount() {
				return NAMES.length;
			}

			@Override
			public int getRowCount() {
				return envelopes.size();
			}

			@Override
			public String getColumnName(int column) {
				return NAMES[column];
			}
		};

		setModel(tableModel);
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setAutoCreateRowSorter(true);
		getRowSorter().setSortKeys(getDefaultSortKeys());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setRowSelectionAllowed(true);
		setFillsViewportHeight(true);
		setContextMenu();
	}
	
	private List<SortKey> getDefaultSortKeys() {
		List<SortKey> keys = new ArrayList<SortKey>();
		keys.add(new SortKey(3, SortOrder.DESCENDING));
		return keys;
	}
	
	public Envelope get(int row) {
		return envelopes.get(row);
	}

	public void refresh() {
		String directory = null; // TODO PreferencesHelper.preferences().get("sedexInput", null);
		if (directory == null) {
			JOptionPane.showMessageDialog(null, "Sedex Input Verzeichnis nicht gesetzt");
			return;
		}
		File file = new File(directory);
		if (!file.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Ungültiges Sedex Input Verzeichnis");
			return;
		}
		envelopes = SedexDirectoryScanner.scan(file);
		setModel(new DefaultTableModel());
		setModel(tableModel);
	}
	
	private void setContextMenu() {
		final JPopupMenu menu = new JPopupMenu();
		menu.add(new JMenuItem("Mach was"));
		addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent event) {
				  System.out.println(event);
				  if (event.isPopupTrigger()) {
					  menu.setLocation(event.getX(), event.getY());
					  menu.pack();
					  menu.setVisible(true);
				  }
			  }
		});
	}

}
