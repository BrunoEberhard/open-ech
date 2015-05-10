package ch.openech.frontend.e21;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.EditorAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.util.CloneHelper;

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
		add("Vater");
		if (!object.father.isEmpty()) {
			add(object.father, new EditorAction(new NameOfParentEditor(true), "EditNameOfFather"));
		} else {
			add(new EditorAction(new NameOfParentEditor(true), "EditNameOfFather"));
		}
		add("Mutter");
		if (!object.mother.isEmpty()) {
			add(object.mother, new EditorAction(new NameOfParentEditor(false), "EditNameOfMother"));
		} else {
			add(new EditorAction(new NameOfParentEditor(false), "EditNameOfMother"));
		}
	}
	
	public class NameOfParentEditor extends Editor<NameOfParent> {
		private final boolean father;
		
		public NameOfParentEditor(boolean father) {
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
		protected NameOfParent load() {
			return father ? getValue().father : getValue().mother;
		}

		@Override
		protected Object save(NameOfParent nameOfParent) {
			CloneHelper.deepCopy(nameOfParent, father ? getValue().father : getValue().mother);
			handleChange();
			return Editor.SAVE_SUCCESSFUL;
		}
    }
	
	
}
