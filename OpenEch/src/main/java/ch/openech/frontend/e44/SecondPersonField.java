package ch.openech.frontend.e44;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;

import  ch.openech.model.person.Relation;

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
