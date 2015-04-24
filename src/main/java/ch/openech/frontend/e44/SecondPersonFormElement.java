package ch.openech.frontend.e44;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;

import  ch.openech.model.person.Relation;

public class SecondPersonFormElement extends ObjectPanelFormElement<Relation> {

	public SecondPersonFormElement(Relation key) {
		this(Keys.getProperty(key));
	}

	public SecondPersonFormElement(PropertyInterface property) {
		super(property);
	}
	
	public boolean hasPartner() {
		Relation relation = getObject();
		return relation != null && relation.partner != null;
	}

	@Override
	protected Form<Relation> createFormPanel() {
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
