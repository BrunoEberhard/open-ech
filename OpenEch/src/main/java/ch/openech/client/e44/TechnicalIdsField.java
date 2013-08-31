package ch.openech.client.e44;

import java.util.List;

import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.common.NamedId;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.fields.ObjectLinkField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

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

	public final class TechnicalIdRemoveAction extends ResourceAction {
		@Override
		public void action(IComponent context) {
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
			return "Bearbeiten";
		}
	}

	@Override
	public IForm<TechnicalIds> createFormPanel() {
		EchForm<TechnicalIds> form = new EchForm<TechnicalIds>();
		form.line(TechnicalIds.TECHNICAL_IDS.localId.personIdCategory);
		form.line(TechnicalIds.TECHNICAL_IDS.localId.personId);
		form.line(new OtherIdField(TechnicalIds.TECHNICAL_IDS.otherId, isEditable()));
		if (hasSpecialEuIds) {
			form.line(new OtherIdField(TechnicalIds.TECHNICAL_IDS.euId, isEditable()));
		}
		return form;
	}
	
	private static class OtherIdField extends ObjectFlowField<List<NamedId>> {

		public OtherIdField(List<NamedId> key, boolean editable) {
			super(Keys.getProperty(key), editable);
		}

		@Override
		protected IForm<List<NamedId>> createFormPanel() {
			// not used
			return null;
		}

		@Override
		protected void show(List<NamedId> object) {
			for (NamedId id : object) {
				addObject(id.display());
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
			public IForm<NamedId> createForm() {
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
		
		private class RemoveOtherIdAction extends ResourceAction {
			private final NamedId id;
			
			public RemoveOtherIdAction(NamedId id) {
				this.id = id;
			}

			@Override
			public void action(IComponent context) {
				OtherIdField.this.getObject().remove(id);
				setObject(getObject());
			}
		}
				
	}

}
