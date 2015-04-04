package ch.openech.frontend.e21;

import java.util.List;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.Keys;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Relation;
import ch.openech.xml.write.EchSchema;

public class RelationField extends ObjectFlowField<List<Relation>> {
	private final EchSchema echNamespaceContext;
	
	public RelationField(List<Relation> key, EchSchema echNamespaceContext, boolean editable) {
		super(Keys.getProperty(key), editable);
		this.echNamespaceContext = echNamespaceContext;
	}

	public class AddRelationEditor extends ObjectFieldPartEditor<Relation> {

		@Override
		protected Relation getPart(List<Relation> object) {
			return new Relation();
		}

		@Override
		protected void setPart(List<Relation> object, Relation relation) {
			// TODO ist das nötig?
			if (!relation.isCareRelation()) relation.basedOnLaw = null;
			if (!relation.isParent()) relation.care = null;

			object.add(relation);
		}
		
		@Override
		public Form<Relation> createForm() {
			return new RelationPanel(echNamespaceContext);
		}
	}
	
	private class RemoveRelationAction extends Action {
		private final Relation relation;
		
		private RemoveRelationAction(Relation relation) {
			this.relation = relation;
		}
		
		@Override
		public void action() {
			getObject().remove(relation);
			fireObjectChange();
		}
	}
	
	@Override
	public Form<List<Relation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(List<Relation> objects) {
		if (isEditable()) {
			for (Relation relation : objects) {
				addText(relation.toHtml());
				addAction(new RemoveRelationAction(relation));
				addGap();
			}
			addAction(new AddRelationEditor());
		} else {
			for (Relation relation : objects) {
				addText(relation.toHtml());
				addAction(new PersonPage(echNamespaceContext, relation.partner.id), "Person anzeigen");
				addGap();
			}
		}
	}

}
