package ch.openech.client.e21;

import ch.openech.dm.person.NameOfParent;
import ch.openech.dm.person.NameOfParents;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.model.Keys;

public class NameOfParentsField extends ObjectFlowField<NameOfParents> {
	
	public NameOfParentsField(NameOfParents key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected IForm<NameOfParents> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(NameOfParents object) {
		if (!object.father.isEmpty()) {
			addText("Vater");
			addText(object.father.display());
			addGap();
		}
		if (!object.mother.isEmpty()) {
			addText("Mutter");
			addText(object.mother.display());
		}
	}
	
	@Override
	protected void showActions() {
        addAction(new NameOfParentEditor(true), "EditNameOfFather");
        addAction(new NameOfParentEditor(false), "EditNameOfMother");
	}

	public class NameOfParentEditor extends ObjectFieldPartEditor<NameOfParent> {
		private final boolean father;
		
		public NameOfParentEditor(boolean father) {
			this.father = father;
		}

		@Override
		public IForm<NameOfParent> createForm() {
			Form<NameOfParent> form = new Form<NameOfParent>();
			form.line(NameOfParent.KEYS.firstName);
			form.line(NameOfParent.KEYS.officialName);
			form.line(NameOfParent.KEYS.officialProof);
			return form;
		}

		@Override
		protected NameOfParent getPart(NameOfParents object) {
			return father ? object.father : object.mother;
		}

		@Override
		protected void setPart(NameOfParents object, NameOfParent nameOfParent) {
			CloneHelper.deepCopy(nameOfParent, father ? object.father : object.mother);
		}
    }
	
	
}
