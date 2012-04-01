package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.xml.write.EchNamespaceContext;

public class RelationField extends MultiLineObjectField<List<Relation>> {
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
	
//	private class RelationClickListener implements ClickListener {
//		@Override
//		public void clicked() {
//			Relation relation = (Relation) list.getSelectedObject();
//			if (relation != null) {
//	    		if (relation.partner != null) {
//	    			PageContext pageContext = ClientToolkit.getToolkit().findPageContext(list);
//	    			pageContext.show(Page.link(PersonViewPage.class, echNamespaceContext.getVersion(), relation.partner.getId()));
//	    		}
//			}
//		}
//	}
	
	@Override
	public FormVisual<List<Relation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(List<Relation> objects) {
		for (Relation relation : objects) {
			addHtml(relation.toHtml());
			addAction(new RemoveRelationAction(relation));
			addGap();
		}
		if (isEditable()) {
			addAction(new EditorDialogAction(new AddRelationEditor()));
		}
	}

}
