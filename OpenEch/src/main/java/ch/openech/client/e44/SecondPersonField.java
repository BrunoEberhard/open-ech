package ch.openech.client.e44;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;

public class SecondPersonField extends ObjectFlowField<Relation> {

	public SecondPersonField(Relation key) {
		this(Keys.getProperty(key));
	}

	public SecondPersonField(PropertyInterface property) {
		super(property);
	}
	
	public boolean hasPartner() {
		Relation relation = getObject();
		return relation != null && relation.partner != null;
	}

	@Override
	protected IForm<Relation> createFormPanel() {
		return null;
	}

	@Override
	protected void show(Relation relation) {
		if (!hasPartner()) {
			addText("Kein Partner / Keine Partnerin vorhanden");
		} else {
			addText(relation.partner.toHtml());
		}
	}

}
