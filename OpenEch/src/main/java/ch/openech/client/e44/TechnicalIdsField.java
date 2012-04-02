package ch.openech.client.e44;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.NamedId;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;

public class TechnicalIdsField extends ObjectFlowField<TechnicalIds> {
	public static final boolean WITH_EU_IDS = true;
	public static final boolean WITHOUT_EU_IDS = false;
	
	private final boolean hasSpecialEuIds;

	public TechnicalIdsField(Object key, boolean hasSpecialEuIds, boolean editable) {
		super(key, editable, false);
		this.hasSpecialEuIds = hasSpecialEuIds;
	}


	public final class TechnicalIdRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			getObject().clear();
			fireObjectChange();
		}
	}
	
	@Override
	protected void show(TechnicalIds technicalIds) {
		int maxLength = isEditable() ? 10 : 25;
		if (technicalIds.localId.personId != null) {
			if (technicalIds.localId.personId.length() <= maxLength) {
				addObject(technicalIds.localId.personId);
			} else {
				addObject(technicalIds.localId.personId.substring(0, maxLength-1) + "...");
			}
		}
	}

	@Override
	protected void showActions() {
		addGap();
		addAction(new ObjectFieldEditor());
	}

	@Override
	public FormVisual<TechnicalIds> createFormPanel() {
		EchFormPanel<TechnicalIds> form = new EchFormPanel<TechnicalIds>(TechnicalIds.class);
		form.line(TechnicalIds.TECHNICAL_IDS.localId.personIdCategory);
		form.line(TechnicalIds.TECHNICAL_IDS.localId.personId);
		form.area(new OtherIdField(TechnicalIds.TECHNICAL_IDS.otherId, isEditable()));
		if (hasSpecialEuIds) {
			form.area(new OtherIdField(TechnicalIds.TECHNICAL_IDS.euId, isEditable()));
		}
		return form;
	}
	
	private static class OtherIdField extends ObjectFlowField<List<NamedId>> {

		public OtherIdField(Object key, boolean editable) {
			super(key, editable);
		}

		@Override
		protected FormVisual<List<NamedId>> createFormPanel() {
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
			public FormVisual<NamedId> createForm() {
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
			public void actionPerformed(ActionEvent e) {
				OtherIdField.this.getObject().remove(id);
				setObject(getObject());
			}
		}
				
	}

}
