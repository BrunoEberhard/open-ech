package ch.openech.client.e21;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Constants;
import ch.openech.xml.write.EchSchema;

// Eigentlich relationField
public class PartnerField extends ObjectFlowField<Relation> {
	private final EchSchema echSchema;
	
	public PartnerField(Relation key, EchSchema echSchema) {
		super(Constants.getProperty(key));
		this.echSchema = echSchema;
	}

	public class PartnerEditor extends ObjectFieldEditor {
		// Benötigt, damit dir richige Resource verwendet wird
	}
	
	@Override
	protected void show(Relation relation) {
		addHtml(relation.identificationToHtml());
	}

	@Override
	protected void showActions() {
		addAction(new PartnerEditor());
	}
	
	@Override
	public Relation getObject() {
		Relation relation = super.getObject();
		// TODO ist das wirklich nötig?
		if (!relation.isCareRelation()) relation.basedOnLaw = null;
		if (!relation.isParent()) relation.care = null;
		return relation;
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
		return new RelationPanel(echSchema);
	}
}
