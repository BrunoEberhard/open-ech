package ch.openech.frontend.e21;

import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.frontend.page.OrganisationPage;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Relation;
import ch.openech.xml.write.EchSchema;

public class RelationFormElement extends ListFormElement<Relation> {
	private final EchSchema echNamespaceContext;

	public RelationFormElement(List<Relation> key, EchSchema echNamespaceContext, boolean editable) {
		super(Keys.getProperty(key), editable);
		this.echNamespaceContext = echNamespaceContext;
	}

	public class AddRelationEditor extends AddListEntryEditor {

		@Override
		protected void addEntry(Relation relation) {
			// TODO ist das nötig?
			if (!relation.isCareRelation())
				relation.basedOnLaw = null;
			if (!relation.isParent())
				relation.care = null;

			super.addEntry(relation);
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
	public Form<Relation> createForm(boolean edit) {
		return new RelationPanel(echNamespaceContext);
	}

	@Override
	protected void showEntry(Relation relation) {
		if (isEditable()) {
			add(relation, new RemoveRelationAction(relation));
		} else {
			add(relation);
			if (relation.partner.person != null) {
				add("Person anzeigen", new PersonPage(echNamespaceContext, relation.partner.person.id));
			} else if (relation.partner.organisation != null) {
				add("Person anzeigen", new OrganisationPage(echNamespaceContext, relation.partner.organisation.id));
			}
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddRelationEditor() };
	}
}
