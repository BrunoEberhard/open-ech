package ch.openech.frontend.e44;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.person.Relation;

public class SecondPersonFormElement extends ObjectFormElement<Relation> {

	public SecondPersonFormElement(Relation key) {
		this(Keys.getProperty(key));
	}

	public SecondPersonFormElement(PropertyInterface property) {
		super(property);
	}
	
	public boolean hasPartner() {
		Relation relation = getValue();
		return relation != null && relation.partner != null;
	}

	@Override
	protected Form<Relation> createForm() {
		return null;
	}

	@Override
	protected void show(Relation relation) {
		if (!hasPartner()) {
			add("Kein Partner / Keine Partnerin vorhanden");
		} else {
			add(relation);
		}
	}

}
