package ch.openech.frontend.e21;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.resources.Resources;

import ch.openech.model.person.NameOfParent;
import ch.openech.model.person.NameOfParents;

public class NameOfParentsFormElement extends ObjectFormElement<NameOfParents> {
	
	public NameOfParentsFormElement(NameOfParents key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected Form<NameOfParents> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(NameOfParents object) {
		if (isEditable()) {
			add("Vater");
			if (!object.father.isEmpty()) {
				add(object.father, new NameOfParentEditor(true));
			} else {
				add(new NameOfParentEditor(true));
			}
			add("Mutter");
			if (!object.mother.isEmpty()) {
				add(object.mother, new NameOfParentEditor(false));
			} else {
				add(new NameOfParentEditor(false));
			}
		} else {
			if (!object.father.isEmpty()) {
				add("Vater");
				add(object.father);
			}
			if (!object.mother.isEmpty()) {
				add("Mutter");
				add(object.mother);
			}
		}
	}
	
	public class NameOfParentEditor extends Editor<NameOfParent, Void> {
		private final boolean father;
		
		public NameOfParentEditor(boolean father) {
			super(Resources.getString(father ? "EditNameOfFather" : "EditNameOfMother"));
			this.father = father;
		}

		@Override
		public Form<NameOfParent> createForm() {
			Form<NameOfParent> form = new Form<NameOfParent>();
			form.line(NameOfParent.$.firstName);
			form.line(NameOfParent.$.officialName);
			form.line(NameOfParent.$.officialProof);
			return form;
		}

		@Override
		protected NameOfParent createObject() {
			return father ? getValue().father : getValue().mother;
		}

		@Override
		protected Void save(NameOfParent nameOfParent) {
			CloneHelper.deepCopy(nameOfParent, father ? getValue().father : getValue().mother);
			return null;
		}

		@Override
		protected void finished(Void result) {
			handleChange();
		}		
    }
	
	
}
