package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtil;
import org.minimalj.repository.query.By;

import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.estate.Realestate;
import ch.openech.model.estate.Realestate.RealestateView;
import ch.openech.model.estate.RealestateInformation;

public class RealestateInformationFormElement extends ListFormElement<RealestateInformation> {

	public RealestateInformationFormElement(List<RealestateInformation> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected void showEntry(RealestateInformation entry) {
		if (isEditable()) {
			add(entry, new ListEntryEditor(entry), new RemoveEntryAction(entry));
		} else {
			add(entry);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddListEntryEditor() };
	}

	@Override
	protected Form<RealestateInformation> createForm(boolean edit) {
		return new RealestateInformationForm();
	}
	
	public static class RealestateInformationForm extends EchForm<RealestateInformation> {
		
		public RealestateInformationForm() {
			super(null, 2);
			addTitle("Grundstück");
			line(new RealestateReferenceFormElement(RealestateInformation.$.realestate, Form.EDITABLE));
			line(RealestateInformation.$.municipality);
			addTitle("Lagebezeichnung");
			line(RealestateInformation.$.placeName.placeNameType);
			line(RealestateInformation.$.placeName.localGeographicalName);
			// TODO owner
		}
	}

	public static class RealestateReferenceFormElement extends ObjectFormElement<Realestate> {
		
		public RealestateReferenceFormElement(Object key, boolean editable) {
			super(Keys.getProperty(key), editable);
		}

		@Override
		protected Action[] getActions() {
			return new Action[] {new RealestateSearchAction(), new NewObjectFormElementEditor()};
		}
		
		@Override
		protected void show(Realestate object) {
			add(object, getEditorAction());
		}	
		
		private class RealestateSearchAction extends SearchDialogAction<RealestateView> {

			public RealestateSearchAction() {
				super(new Object[] { Realestate.$.EGRID });
			}
			
			@Override
			public List<RealestateView> search(String searchText) {
				return Backend.find(RealestateView.class, By.search(searchText));
			}

			@Override
			protected void save(RealestateView object) {
				Realestate realestate = ViewUtil.viewed(object);
				RealestateReferenceFormElement.this.setValue(realestate);
			}
		}
		
		@Override
		protected Form<Realestate> createForm() {
			return new RealestateTablePage.RealestateForm(Form.EDITABLE);
		}
		
	}
	
}