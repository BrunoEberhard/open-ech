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
		if (!object.father.isEmpty()) {
			add("Vater");
			add(object.father, new EditorAction(new NameOfParentEditor(true), "EditNameOfFather"));
		};
		if (!object.mother.isEmpty()) {
			add("Mutter");
			add(object.mother, new EditorAction(new NameOfParentEditor(true), "EditNameOfMother"));
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
			return Editor.SAVE_SUCCESSFUL;
		}
    }
	
	
}
