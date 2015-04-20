package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.fields.ObjectLinkField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.TechnicalIds;

public class TechnicalIdsField extends ObjectLinkField<TechnicalIds> {
	public static final boolean WITH_EU_IDS = true;
	public static final boolean WITHOUT_EU_IDS = false;
	
	private final boolean hasSpecialEuIds;

	public TechnicalIdsField(TechnicalIds key, boolean hasSpecialEuIds, boolean editable) {
		this(Keys.getProperty(key), hasSpecialEuIds, editable);
	}
	
	public TechnicalIdsField(PropertyInterface property, boolean hasSpecialEuIds, boolean editable) {
		super(property, editable);
		this.hasSpecialEuIds = hasSpecialEuIds;
	}

	public final class TechnicalIdRemoveAction extends Action {
		@Override
		public void action() {
			getObject().clear();
			fireObjectChange();
		}
	}
	
	@Override
	protected String display(TechnicalIds technicalIds) {
		int maxLength = 25;
		if (technicalIds.localId.personId != null) {
			if (technicalIds.localId.personId.length() <= maxLength) {
				return technicalIds.localId.personId;
			} else {
				return technicalIds.localId.personId.substring(0, maxLength-1) + "...";
			}
		} else {
			return "-";
		}
	}

	@Override
	public Form<TechnicalIds> createFormPanel() {
		EchForm<TechnicalIds> form = new EchForm<TechnicalIds>();
		form.line(TechnicalIds.$.localId.personIdCategory);
		form.line(TechnicalIds.$.localId.personId);
		form.line(new OtherIdField(TechnicalIds.$.otherId, isEditable()));
		if (hasSpecialEuIds) {
			form.line(new OtherIdField(TechnicalIds.$.euId, isEditable()));
		}
		return form;
	}
	
	private static class OtherIdField extends ObjectFlowField<List<NamedId>> {

		public OtherIdField(List<NamedId> key, boolean editable) {
			super(Keys.getProperty(key), editable);
		}

		@Override
		protected Form<List<NamedId>> createFormPanel() {
			// not used
			return null;
		}

		@Override
		protected void show(List<NamedId> object) {
			for (NamedId id : object) {
				addText(id.display());
				addAction(new RemoveOtherIdAction(id));
				addGap();
			}
		}
		
		@Override
		protected void showActions() {
			addAction(new AddOtherIdAction());
		}

		private class AddOtherIdAction extends ObjectFieldPartEditor<NamedId> {
			@Override
			public Form<NamedId> createForm() {
				return new NamedIdPanel();
			}

			@Override
			protected NamedId getPart(List<NamedId> ids) {
				return new NamedId();
			}

			@Override
			protected void setPart(List<NamedId> ids, NamedId id) {
				OtherIdField.this.getObject().add(id);
			}
	    };
		
		private class RemoveOtherIdAction extends Action {
			private final NamedId id;
			
			public RemoveOtherIdAction(NamedId id) {
				this.id = id;
			}

			@Override
			public void action() {
				OtherIdField.this.getObject().remove(id);
				setObject(getObject());
			}
		}
				
	}

}