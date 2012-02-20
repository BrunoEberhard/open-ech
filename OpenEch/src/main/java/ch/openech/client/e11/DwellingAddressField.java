package ch.openech.client.e11;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;
import ch.openech.xml.write.EchNamespaceContext;

public class DwellingAddressField extends ObjectField<DwellingAddress> implements DemoEnabled {
	private final EchNamespaceContext namespaceContext;
	private final MultiLineTextField text;
	
	public DwellingAddressField(Object key, EchNamespaceContext namespaceContext, boolean editable) {
		super(key);
		this.namespaceContext = namespaceContext;
		
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		// // add(new SizedScrollPane(text, 7, 12));
		if (editable) {
			addAction(new ObjectFieldEditor());
			addAction(new RemoveObjectAction());
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	protected void display(DwellingAddress dwellingAddress) {
		StringBuilder s = new StringBuilder();
		if (dwellingAddress != null) {
			dwellingAddress.toHtml(s);
		}
		text.setText(s.toString());
	}
	
	@Override
	public FormVisual<DwellingAddress> createFormPanel() {
		return new DwellingAddressPanel(namespaceContext);
	}

	//

	@Override
	public void fillWithDemoData() {
		setObject(DataGenerator.dwellingAddress());
	}
	
}
