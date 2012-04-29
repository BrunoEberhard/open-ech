package ch.openech.client.org;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;

public class HeadquarterField extends ObjectFlowField<Organisation> {

	public HeadquarterField(Object key, boolean editable) {
		super(key, editable);
	}

	@Override
	protected IForm<Organisation> createFormPanel() {
		return null;
	}

	@Override
	protected void show(Organisation object) {
		
	}

}
