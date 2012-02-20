package ch.openech.client.e44;

import java.util.List;

import javax.swing.Action;
import javax.swing.event.ChangeListener;

import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.VisualTable;
import ch.openech.server.EchServer;

public class PersonSearchForm extends AbstractFormVisual<Person> {

	public static final String SEARCH_FIELD = "searchField";
	
	private final TextEditField textField;
	private final VisualTable<Person> table;
	private ChangeListener changeListener;
	private Action saveAction;
	
	public PersonSearchForm() {
		super();
		
		textField = new TextEditField("search", 100);
		table = ClientToolkit.getToolkit().createVisualTable(Person.class, SearchPersonPage.FIELD_NAMES);
//		table.setClickListener(new PersonTableClickListener());

		line(Person.PERSON.personIdentification.officialName);
		area(table);
	}
	
	@Override
	public Object getComponent() {
		return getComponent();
	}
	
	@Override
	public void setSaveAction(Action saveAction) {
		this.saveAction = saveAction;
	}

	protected void createTable() {
//		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				if (changeListener != null) changeListener.stateChanged(new ChangeEvent(PersonSearchForm.this));
//			}
//		});
//		
//		table.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
//					saveAction.actionPerformed(new ActionEvent(PersonSearchForm.this, 0, null));
//				}
//			}
//		});
//		
//		return new JScrollPane(table);
	}
	
//	private void executeSearch() {
//		String text = textField.getText();
//		int personCount = EchServer.getInstance().getPersistence().person().count(text);
//		if (personCount > 200) {
//			JOptionPane.showMessageDialog(null, "Zu viele Suchresultate (" + personCount + "). Bitte genauer eingrenzen.");
//			return;
//		}
//		table.search(text);
//	}
	
	public void refresh() {
		String text = textField.getObject();
		List<Person> resultList = EchServer.getInstance().getPersistence().person().find(text);
		table.setObjects(resultList);
	}
	
	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (getObject() == null) {
			resultList.add(new ValidationMessage(null, "Keine Person ausgewählt"));
		}
	}

	@Override
	public void setChangeListener(ChangeListener changeListener) {
		this.changeListener = changeListener;
	}

	@Override
	public void setValidationMessages(List<ValidationMessage> validationMessages) {
		// not used
	}

	@Override
	public void setObject(Person object) {
		// not used
	}

	@Override
	public Person getObject() {
		return table.getSelectedObject();
	}

	@Override
	public boolean isResizable() {
		return true;
	}

}
