package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.client.ewk.PersonViewPage;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualList;
import ch.openech.mj.toolkit.VisualList.ClickListener;
import ch.openech.xml.write.EchNamespaceContext;

public class RelationField extends ObjectField<List<Relation>> {
	private final boolean withNameOfParents;
	private final EchNamespaceContext echNamespaceContext;
	private final VisualList listRelation;
	
	public RelationField(Object key, EchNamespaceContext echNamespaceContext, boolean withNameOfParents, boolean editable) {
		super(key);
		
		this.withNameOfParents = withNameOfParents;
		this.echNamespaceContext = echNamespaceContext;
		
		listRelation = ClientToolkit.getToolkit().createVisualList();
		
		if (editable) {
			createMenu();
		} else {
			listRelation.setClickListener(new RelationClickListener());
		}
	}

	@Override
	protected IComponent getComponent0() {
		return listRelation;
	}

	private void createMenu() {
		addAction(new AddRelationEditor());
		addAction(new RemoveRelationAction());
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
		@Override
		public void actionPerformed(ActionEvent e) {
//			int[] selectedIndices = listRelation.getSelectedIndices();
//			if (selectedIndices.length > 0) {
//				List<Relation> selectedValues = new ArrayList<Relation>(selectedIndices.length);
//				for (int index : selectedIndices) {
//					selectedValues.add(getObject().get(index));
//				}
//				for (Relation relation : selectedValues) {
//					getObject().remove(relation);
//				}
//				fireChange();
//			}
			if (listRelation.getSelectedIndex() >= 0) {
				getObject().remove(listRelation.getSelectedIndex());
				fireObjectChange();
			}
		}
	}
	
	private class RelationClickListener implements ClickListener {
		@Override
		public void clicked() {
			Relation relation = (Relation) listRelation.getSelectedObject();
			if (relation != null) {
	    		if (relation.partner != null) {
	    			PageContext pageContext = ClientToolkit.getToolkit().findPageContext(listRelation);
	    			pageContext.show(Page.link(PersonViewPage.class, echNamespaceContext.getVersion(), relation.partner.getId()));
	    		}
			}
		}
	}
	
	@Override
	public FormVisual<List<Relation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(List<Relation> objects) {
		listRelation.setObjects(objects);
	}

}
