package ch.openech.client.e44;

import java.awt.event.ActionEvent;

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
	
	// "Andere Id hinzufügen"
	public final class TechnicalIdOtherAddEditor extends ObjectFieldPartEditor<NamedId> {
		
		@Override
		public FormVisual<NamedId> createForm() {
			return new NamedIdPanel();
		}

		@Override
		protected NamedId getPart(TechnicalIds object) {
			return new NamedId();
		}

		@Override
		protected void setPart(TechnicalIds object, NamedId p) {
			object.otherId.add(p);
		}
	}

	// "EU Id hinzufügen"
	public final class TechnicalIdAddEuEditor extends ObjectFieldPartEditor<NamedId> {
		
		@Override
		public FormVisual<NamedId> createForm() {
			return new NamedIdPanel();
		}

		@Override
		protected NamedId getPart(TechnicalIds object) {
			return new NamedId();
		}

		@Override
		protected void setPart(TechnicalIds object, NamedId p) {
			object.euId.add(p);
		}
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
		boolean empty = true;
		
		StringBuilder s = new StringBuilder();
		if (technicalIds != null) {
			if (technicalIds.localId.personId != null) {
				s.append(technicalIds.localId.personId);
				empty = false;
			}
			for (NamedId namedPersonId : technicalIds.otherId) {
				if (s.length() > 0) s.append(", "); 
				namedPersonId.display(s);
				empty = false;
			}
			for (NamedId namedPersonId : technicalIds.euId) {
				if (s.length() > 0) s.append(", ");
				s.append("EU/");
				namedPersonId.display(s);
				empty = false;
			}
		}
		addObject(s.toString());
		
		if (isEditable()) {
	        addAction(new TechnicalIdOtherAddEditor());
	        if (hasSpecialEuIds) {
	        	addAction(new TechnicalIdAddEuEditor());
	        }
	        if (!empty) {
		        addAction(new TechnicalIdRemoveAction());
	        }
		}
	}

	@Override
	public FormVisual<TechnicalIds> createFormPanel() {
		// unused
		return null;
	}

}
