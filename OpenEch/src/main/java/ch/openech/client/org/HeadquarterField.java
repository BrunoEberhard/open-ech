package ch.openech.client.org;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Constants;
import ch.openech.mj.model.PropertyInterface;

public class HeadquarterField extends ObjectFlowField<Organisation> {

	public HeadquarterField(Organisation key, boolean editable) {
		this(Constants.getProperty(key), editable);
	}
	
	public HeadquarterField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	protected IForm<Organisation> createFormPanel() {
		return null;
	}

	@Override
	protected void show(Organisation object) {
		
	}

}
