package ch.openech.client.e44;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.FormVisual;

public class SecondPersonField extends ObjectFlowField<Relation> {
	
	public SecondPersonField(String name) {
		super(name);
	}
	
	public boolean hasPartner() {
		Relation relation = getObject();
		return relation != null && relation.partner != null;
	}

	@Override
	protected FormVisual<Relation> createFormPanel() {
		return null;
	}

	@Override
	protected void show(Relation relation) {
		if (!hasPartner()) {
			addObject("Kein Partner / Keine Partnerin vorhanden");
		} else {
			addHtml(relation.partner.toHtml());
		}
	}

}
