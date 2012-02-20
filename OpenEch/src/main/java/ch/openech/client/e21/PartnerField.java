package ch.openech.client.e21;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;
import ch.openech.xml.write.EchNamespaceContext;


// Eigentlich relationField
public class PartnerField extends ObjectField<Relation> {
	private final EchNamespaceContext echNamespaceContext;
	
	private MultiLineTextField text;

	public PartnerField(Object key, EchNamespaceContext echNamespaceContext) {
		super(key);
		this.echNamespaceContext = echNamespaceContext;
		
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		addAction(new PartnerEditor());
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	public class PartnerEditor extends ObjectFieldEditor {
		// TODO häh?
	}
	
	@Override
	protected void display(Relation relation) {
		text.setText(null);
		text.setText(relation.identificationToHtml());
//		
//		if (connectedNameField != null) {
//			String name = null;
//			if (relation.partner != null) {
//				name = relation.partner.officialName;
//			}
//			connectedNameField.setText(name);
//		}
	}
	
	@Override
	public FormVisual<Relation> createFormPanel() {
		return new RelationPanel(echNamespaceContext, false);
	}
}
