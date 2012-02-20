package ch.openech.client.e44;

import java.awt.event.ActionEvent;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class TechnicalIdsField extends ObjectField<TechnicalIds> {
	public static final boolean WITH_EU_IDS = true;
	public static final boolean WITHOUT_EU_IDS = false;
	
	private final boolean hasSpecialEuIds;
	
	private final TextField textField;

	public TechnicalIdsField(Object key, boolean hasSpecialEuIds, boolean editable) {
		super(key);
		this.hasSpecialEuIds = hasSpecialEuIds;

		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		if (editable) {
			createMenu();
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return textField;
	}

	private void createMenu() {
        addAction(new TechnicalIdOtherAddEditor());
        if (hasSpecialEuIds) {
    		addAction(new TechnicalIdAddEuEditor());
        }
        addAction(new TechnicalIdRemoveAction());
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
			display(getObject());
			fireChange();
		}
	}
	
	@Override
	protected void display(TechnicalIds technicalIds) {
		StringBuilder s = new StringBuilder();
		if (technicalIds.localId != null) {
			s.append(technicalIds.localId.personId);
		}
		for (NamedId namedPersonId : technicalIds.otherId) {
			if (s.length() > 0) s.append(", "); 
			namedPersonId.display(s);
		}
		for (NamedId namedPersonId : technicalIds.euId) {
			if (s.length() > 0) s.append(", ");
			s.append("EU/");
			namedPersonId.display(s);
		}
		textField.setText(s.toString());
	}

	@Override
	public FormVisual<TechnicalIds> createFormPanel() {
		// unused
		return null;
	}

}
