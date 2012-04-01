package ch.openech.client.e21;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.xml.write.EchNamespaceContext;

// Eigentlich relationField
public class PartnerField extends ObjectFlowField<Relation> {
	private final EchNamespaceContext echNamespaceContext;
	
	public PartnerField(Object key, EchNamespaceContext echNamespaceContext) {
		super(key);
		this.echNamespaceContext = echNamespaceContext;
	}
	
	public class PartnerEditor extends ObjectFieldEditor {
		// TODO häh?
	}
	
	@Override
	protected void show(Relation relation) {
		addHtml(relation.identificationToHtml());
	}

	@Override
	protected void showActions() {
		addAction(new PartnerEditor());
	}
	
//		
//		if (connectedNameField != null) {
//			String name = null;
//			if (relation.partner != null) {
//				name = relation.partner.officialName;
//			}
//			connectedNameField.setText(name);
//		}
	@Override
	public FormVisual<Relation> createFormPanel() {
		return new RelationPanel(echNamespaceContext, false);
	}
}
