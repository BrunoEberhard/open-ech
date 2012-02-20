package ch.openech.client.e11;

import java.util.List;

import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;

public class ForeignField extends ObjectField<Foreign> implements Validatable, DependingOnFieldAbove<Nationality> {
	private final EchNamespaceContext namespaceContext;
	private final MultiLineTextField text;
	private boolean swiss = true;
	
	public ForeignField(Object key, EchNamespaceContext namespaceContext, boolean editable) {
		super(key);
		this.namespaceContext = namespaceContext;
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		// add(new SizedScrollPane(text, 7, 12));
		if (editable) {
			addAction(new ObjectFieldEditor());
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	protected void display(Foreign foreign) {
		text.setText(foreign.toHtml());
	}

	@Override
	public void setObject(Foreign object) {
		if (object == null) throw new IllegalArgumentException();
		super.setObject(object);
	}

	@Override
	public FormVisual<Foreign> createFormPanel() {
		return new ForeignPanel(namespaceContext);
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (!swiss) {
			Foreign foreign = getObject();
			if (StringUtils.isBlank(foreign.residencePermit)) {
				resultList.add(new ValidationMessage(getName(), "Ausländerkategorie fehlt"));
			}
		}
	}

	@Override
	public String getNameOfDependedField() {
		return "nationality";
	}

	@Override
	public void setDependedField(EditField<Nationality> field) {
		Nationality nationality = field.getObject();
		swiss = nationality.isSwiss();
		text.setEnabled(!swiss);
	}

}
