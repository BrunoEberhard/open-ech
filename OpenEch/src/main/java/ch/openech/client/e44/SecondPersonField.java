package ch.openech.client.e44;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;

public class SecondPersonField extends MultiLineObjectField<Relation> {
	
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
	protected void display(Relation relation) {
		if (!hasPartner()) {
			addObject("Kein Partner / Keine Partnerin vorhanden");
		} else {
			addHtml(relation.partner.toHtml());
		}
	}

}
