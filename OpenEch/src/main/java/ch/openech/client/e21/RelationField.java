package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.client.ewk.PersonViewPage;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.xml.write.EchNamespaceContext;

public class RelationField extends ObjectFlowField<List<Relation>> {
	private final boolean withNameOfParents;
	private final EchNamespaceContext echNamespaceContext;
	
	public RelationField(Object key, EchNamespaceContext echNamespaceContext, boolean withNameOfParents, boolean editable) {
		super(key, editable);
		
		this.withNameOfParents = withNameOfParents;
		this.echNamespaceContext = echNamespaceContext;
	}

	public class AddRelationEditor extends ObjectFieldPartEditor<Relation> {

		@Override
		protected Relation getPart(List<Relation> object) {
			return new Relation();
		}

		@Override
		protected void setPart(List<Relation> object, Relation p) {
			object.add(p);
		}
		
		@Override
		public FormVisual<Relation> createForm() {
			return new RelationPanel(echNamespaceContext, withNameOfParents);
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
			PageContext pageContext = ClientToolkit.getToolkit().findPageContext(e.getSource());
			pageContext.show(Page.link(PersonViewPage.class, echNamespaceContext.getVersion(), relation.partner.getId()));
		}
	}
	
	@Override
	public FormVisual<List<Relation>> createFormPanel() {
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
