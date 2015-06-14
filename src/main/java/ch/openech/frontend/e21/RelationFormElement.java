package ch.openech.frontend.e21;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.Keys;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Relation;
import ch.openech.xml.write.EchSchema;

public class RelationFormElement extends ListFormElement<Relation> {
	private final EchSchema echNamespaceContext;

	public RelationFormElement(List<Relation> key, EchSchema echNamespaceContext, boolean editable) {
		super(Keys.getProperty(key), editable);
		this.echNamespaceContext = echNamespaceContext;
	}

	public class AddRelationEditor extends AddListEntryAction {

		@Override
		protected Relation createObject() {
			return new Relation();
		}

		@Override
		public Form<Relation> createForm() {
			return new RelationPanel(echNamespaceContext);
		}

		@Override
		protected void addEntry(Relation relation) {
			// TODO ist das nötig?
			if (!relation.isCareRelation())
				relation.basedOnLaw = null;
			if (!relation.isParent())
				relation.care = null;

			getValue().add(relation);
		}
	}

	private class RemoveRelationAction extends Action {
		private final Relation relation;

		private RemoveRelationAction(Relation relation) {
			this.relation = relation;
		}

		@Override
		public void action() {
			getValue().remove(relation);
			handleChange();
		}
	}

	@Override
	public Form<List<Relation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void showEntry(Relation relation) {
		if (isEditable()) {
			add(relation, new RemoveRelationAction(relation));
		} else {
			add(relation, new PageAction(new PersonPage(echNamespaceContext, relation.partner.id), "Person anzeigen"));
		}
	}

	protected Action[] getActions() {
		return new Action[] { new AddRelationEditor() };
	}
}
