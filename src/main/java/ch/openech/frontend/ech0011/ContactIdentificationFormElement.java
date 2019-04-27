package ch.openech.frontend.ech0011;

import java.util.Collections;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.SwitchComponent;
import org.minimalj.frontend.Frontend.TableActionListener;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.editor.SearchDialog;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.form.element.FormElementConstraint;
import org.minimalj.model.Rendering;
import org.minimalj.repository.query.By;

import ch.ech.ech0011.Person;
import ch.ech.ech0098.Organisation;

public class ContactIdentificationFormElement extends AbstractFormElement<Identification> {

	private final SwitchComponent component;
	private Identification value;

	private SearchDialog<Person> personSearchDialog;
	private SearchDialog<Organisation> organisationSearchDialog;

	public ContactIdentificationFormElement(Identification key) {
		super(key);
		component = Frontend.getInstance().createSwitchComponent();
		height(3, FormElementConstraint.MAX);
	}

	@Override
	public void setValue(Identification value) {
		this.value = value != null ? value : new Identification();
		refresh();
	}

	protected void setValueInternal(Object object) {
		value.set(object);
		refresh();
		listener().changed(component);
	}

	private void refresh() {
		Input<String> input = Frontend.getInstance().createReadOnlyTextField();
		ActionGroup actions = new ActionGroup(null);
		actions.add(new PersonLookupAction());
		actions.add(new OrganisationLookupAction());
		if (value.person != null) {
			input.setValue(Rendering.toString(value));
			actions.add(new IdentificationClearAction());
		} else if (value.organisation != null) {
			input.setValue(Rendering.toString(value.organisation));
			actions.add(new IdentificationClearAction());
		} else {
			input.setValue(null);
		}
		component.show(Frontend.getInstance().createLookup(input, actions));
	}

	@Override
	public Identification getValue() {
		return value;
	}

	@Override
	public IComponent getComponent() {
		return component;
	}

	public class PersonLookupAction extends Action {
		@Override
		public void action() {
			personSearchDialog = new SearchDialog<Person>(query -> Backend.find(Person.class, By.search(query)),
					new Object[] { Person.$.nameData.officialName }, false, new PersonSearchDialogActionListener(),
					Collections.emptyList());
			personSearchDialog.show();
		}
	}

	private class PersonSearchDialogActionListener implements TableActionListener<Person> {
		@Override
		public void action(Person selectedObject) {
			ContactIdentificationFormElement.this.setValueInternal(selectedObject.personIdentification);
			personSearchDialog.closeDialog();
		}
	}

	public class IdentificationClearAction extends Action {
		@Override
		public void action() {
			ContactIdentificationFormElement.this.setValue(null);
		}
	}

	public class OrganisationLookupAction extends Action {
		@Override
		public void action() {
			organisationSearchDialog = new SearchDialog<Organisation>(
					query -> Backend.find(Organisation.class, By.search(query)),
					new Object[] { Organisation.$.organisationIdentification.organisationName }, false,
					new OrganisationSearchDialogActionListener(), Collections.emptyList());
			organisationSearchDialog.show();
		}
	}

	private class OrganisationSearchDialogActionListener implements TableActionListener<Organisation> {
		@Override
		public void action(Organisation selectedObject) {
			ContactIdentificationFormElement.this.setValueInternal(selectedObject.organisationIdentification);
			organisationSearchDialog.closeDialog();
		}
	}
}
