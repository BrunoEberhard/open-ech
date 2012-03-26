package ch.openech.client.e11;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.xml.write.EchNamespaceContext;

public class DwellingAddressField extends MultiLineObjectField<DwellingAddress> implements DemoEnabled {
	private final EchNamespaceContext namespaceContext;
	
	public DwellingAddressField(Object key, EchNamespaceContext namespaceContext, boolean editable) {
		super(key, editable);
		this.namespaceContext = namespaceContext;
	}
	
	@Override
	protected void display(DwellingAddress dwellingAddress) {
		if (dwellingAddress != null) {
			addHtml(dwellingAddress.toHtml());
		}
		if (isEditable()) {
			addAction(new EditorDialogAction(new ObjectFieldEditor()));
			addAction(new RemoveObjectAction());
		}
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
