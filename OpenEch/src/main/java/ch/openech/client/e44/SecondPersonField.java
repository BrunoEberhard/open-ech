package ch.openech.client.e44;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;

public class SecondPersonField extends ObjectField<Relation> {
	private MultiLineTextField text;
	
	public SecondPersonField(String name) {
		super(name);
		
		text = ClientToolkit.getToolkit().createMultiLineTextField();
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
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
			text.setText("Kein Partner / Keine Partnerin vorhanden");
		} else {
			text.setText(relation.partner.toHtml());
		}
	}

}
