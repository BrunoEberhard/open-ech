package ch.openech.client.e21;

import java.util.List;

import ch.openech.client.page.PersonViewPage;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;
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
		public IForm<Relation> createForm() {
			return new RelationPanel(echNamespaceContext);
		}
	}
	
	private class RemoveRelationAction extends ResourceAction {
		private final Relation relation;
		
		private RemoveRelationAction(Relation relation) {
			this.relation = relation;
		}
		
		@Override
		public void action(IComponent context) {
			getObject().remove(relation);
			fireObjectChange();
		}
	}
	
	@Override
	public IForm<List<Relation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(List<Relation> objects) {
		if (isEditable()) {
			for (Relation relation : objects) {
				addHtml(relation.toHtml());
				addAction(new RemoveRelationAction(relation));
				addGap();
			}
			addAction(new AddRelationEditor());
		} else {
			for (Relation relation : objects) {
				addHtml(relation.toHtml());
				addLink("Person anzeigen", PageLink.link(PersonViewPage.class, echNamespaceContext.getVersion(), relation.partner.getId()));
				addGap();
			}
		}
	}

}
