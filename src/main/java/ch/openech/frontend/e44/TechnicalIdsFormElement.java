package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.TechnicalIds;

public class TechnicalIdsFormElement extends ObjectFormElement<TechnicalIds> {
	public static final boolean WITH_EU_IDS = true;
	public static final boolean WITHOUT_EU_IDS = false;
	
	private final boolean hasSpecialEuIds;

	public TechnicalIdsFormElement(TechnicalIds key, boolean hasSpecialEuIds, boolean editable) {
		this(Keys.getProperty(key), hasSpecialEuIds, editable);
	}
	
	public TechnicalIdsFormElement(PropertyInterface property, boolean hasSpecialEuIds, boolean editable) {
		super(property, editable);
		this.hasSpecialEuIds = hasSpecialEuIds;
	}

	public final class TechnicalIdRemoveAction extends Action {
		@Override
		public void action() {
			getValue().clear();
			handleChange();
		}
	}
	
	@Override
	protected void show(TechnicalIds technicalIds) {
		int maxLength = 25;
		String text;
		if (technicalIds.localId.personId != null) {
			if (technicalIds.localId.personId.length() <= maxLength) {
				text = technicalIds.localId.personId;
			} else {
				text = technicalIds.localId.personId.substring(0, maxLength-1) + "...";
			}
		} else {
			text = isEditable() ? "Id bearbeiten" : "-";
		}
		Action action = new ObjectFormElementEditor(text);
		add(action);
	}

	@Override
	public Form<TechnicalIds> createFormPanel() {
		EchForm<TechnicalIds> form = new EchForm<TechnicalIds>();
		form.line(TechnicalIds.$.localId.personIdCategory);
		form.line(TechnicalIds.$.localId.personId);
		form.line(new OtherIdFormElement(TechnicalIds.$.otherId, isEditable()));
		if (hasSpecialEuIds) {
			form.line(new OtherIdFormElement(TechnicalIds.$.euId, isEditable()));
		}
		return form;
	}
	
	private static class OtherIdFormElement extends ListFormElement<NamedId> {

		public OtherIdFormElement(List<NamedId> key, boolean editable) {
			super(Keys.getProperty(key), editable);
		}

		@Override
		protected Form<List<NamedId>> createFormPanel() {
			// not used
			return null;
		}
		
		@Override
		protected void showEntry(NamedId entry) {
			add(entry, new RemoveOtherIdAction(entry));
		}
		
		@Override
		protected Action[] getActions() {
			return new Action[] { new AddOtherIdEditor() };
		}

		private class AddOtherIdEditor extends AddListEntryEditor {
			@Override
			public Form<NamedId> createForm() {
				return new NamedIdPanel();
			}

			@Override
			protected void addEntry(NamedId id) {
				OtherIdFormElement.this.getValue().add(id);
			}
	    };
		
		private class RemoveOtherIdAction extends Action {
			private final NamedId id;
			
			public RemoveOtherIdAction(NamedId id) {
				this.id = id;
			}

			@Override
			public void action() {
				OtherIdFormElement.this.getValue().remove(id);
				setValue(getValue());
			}
		}
				
	}

}
