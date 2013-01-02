package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.client.page.PersonViewPage;
import ch.openech.dm.person.Relation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.PageContextHelper;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.xml.write.EchSchema;

public class RelationField extends ObjectFlowField<List<Relation>> {
	private final EchSchema echNamespaceContext;
	
	public RelationField(List<Relation> key, EchSchema echNamespaceContext, boolean editable) {
		super(Constants.getProperty(key), editable);
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
		public void actionPerformed(ActionEvent e) {
			getObject().remove(relation);
			fireObjectChange();
		}
	}
	
	private class RelationViewAction extends ResourceAction {
		private final Relation relation;
		
		private RelationViewAction(Relation relation) {
			this.relation = relation;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			PageContext context = PageContextHelper.findContext(e.getSource());
			context.show(Page.link(PersonViewPage.class, echNamespaceContext.getVersion(), relation.partner.getId()));
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
			addAction(new EditorDialogAction(new AddRelationEditor()));
		} else {
			for (Relation relation : objects) {
				addHtml(relation.toHtml());
				addAction(new RelationViewAction(relation));
				addGap();
			}
		}
	}

}
