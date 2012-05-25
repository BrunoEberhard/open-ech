package ch.openech.client.e21;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.xml.write.EchSchema;

// Eigentlich relationField
public class PartnerField extends ObjectFlowField<Relation> {
	private final EchSchema echSchema;
	
	public PartnerField(Object key, EchSchema echSchema) {
		super(key);
		this.echSchema = echSchema;
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
	public IForm<Relation> createFormPanel() {
		return new RelationPanel(echSchema, false);
	}
}
