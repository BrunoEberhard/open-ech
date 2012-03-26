package ch.openech.client.e11;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Residence;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;

public class ResidenceField extends ObjectField<Residence> implements Validatable, DependingOnFieldAbove<String>, DemoEnabled {
	private Action selectSecondary;
	private final TextField textField;
	private String typeOfResidence;

	public ResidenceField(Object key, boolean editable) {
		super(key, editable);
		
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();

		if (editable) {
			addContextAction(new ResidenceMainEditor());
			addContextAction(new ResidenceAddSecondaryEditor());
			addContextAction(new ResidenceRemoveSecondaryAction());
		}
	}
	
	// 		setTitle(main ? "Hauptwohnsitz erfassen" : "Nebenwohnsitz erfassen");

	@Override
	public Object getComponent() {
		return decorateWithContextActions(textField);
	}

	// Als "Part" wird hier das ganze Objekt editiert. Das entsteht, weil eigentlich eine
	// ComboBox im Hauptfeld angezeigt werden müsste, dies durch die Möglichkeit der
	// Nebenwohnorte anzuzueigen aber nicht möglich ist.
	// Möglich Lösung: Zweiteilung des Eingabefeldes
	public final class ResidenceMainEditor extends ObjectFieldPartEditor<Residence> {

		@Override
		public FormVisual<Residence> createForm() {
			AbstractFormVisual<Residence> form = new EchFormPanel<Residence>(Residence.class);
			form.line(new SwissMunicipalityField(Residence.RESIDENCE.reportingMunicipality, true));
			return form;
		}

		@Override
		public Residence getPart(Residence residence) {
			Residence residenceEdit = new Residence();
			residenceEdit.reportingMunicipality = residence.reportingMunicipality;
			return residenceEdit;
		}

		@Override
		public void validate(Residence object, List<ValidationMessage> resultList) {
		}

		@Override
		public void setPart(Residence residence, Residence value) {
			setObject(value);
		}
	}

	public final class ResidenceAddSecondaryEditor extends Editor<Residence> {

		@Override
		public FormVisual<Residence> createForm() {
			AbstractFormVisual<Residence> form = new EchFormPanel<Residence>(Residence.class);
			form.line(new SwissMunicipalityField(Residence.RESIDENCE.reportingMunicipality, true));
			return form;
		}

		@Override
		public Residence load() {
			Residence residence = new Residence();
			residence.reportingMunicipality = ResidenceField.this.getObject().reportingMunicipality;
			return residence;
		}

		@Override
		public void validate(Residence object, List<ValidationMessage> resultList) {
		}

		@Override
		public boolean save(Residence residence) {
			ResidenceField.this.getObject().secondary.add(residence.reportingMunicipality);
			fireObjectChange();
			return true;
		}
	}

	private final class ResidenceRemoveSecondaryAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			getObject().secondary.clear();
			fireObjectChange();
		}
	}
	
	@Override
	public void display(Residence residence) {
		StringBuilder s = new StringBuilder();
		boolean hasResidence = false;
		if (residence.reportingMunicipality != null && !residence.reportingMunicipality.isEmpty()) {
			s.append(residence.reportingMunicipality.toString());
			hasResidence = true;
		} else {
			s.append("Kein Hauptwohnsitz");
		}

		if (residence.secondary != null && !residence.secondary.isEmpty()) {
			s.append(", Nebenwohnsitz: ");
			for (MunicipalityIdentification m : residence.secondary) {
				s.append(m);
				s.append(", ");
			}
			s.delete(s.length() - 2, s.length()); // cut last ", "
			hasResidence = true;
		} 
		
		if (hasResidence) {
			textField.setText(s.toString());
		} else {
			textField.setText("Kein Wohnsitz");
		}
		
		// Bei Nebenwohnsitz darf es nur einer sein, daher auch die Aktion nur bei leerer Liste anbieten
		if (selectSecondary != null) selectSecondary.setEnabled(!"2".equals(typeOfResidence) || residence.secondary == null || residence.secondary.isEmpty());
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		boolean hasMainResidence = "1".equals(typeOfResidence);
		boolean hasSecondResidence = "2".equals(typeOfResidence);
		boolean hasOtherResidence = "3".equals(typeOfResidence);
		
		boolean mainResidenceNeeded = hasMainResidence || hasSecondResidence;
		boolean secondResidenceNeeded = hasSecondResidence || hasOtherResidence;
		boolean notMoreThanOneSecondResidence = hasSecondResidence || hasOtherResidence;
		
		MunicipalityIdentification reportingMunicipality = getObject().reportingMunicipality;
		
		if (mainResidenceNeeded) {
			if (reportingMunicipality == null || reportingMunicipality.isEmpty()) {
				resultList.add(new ValidationMessage(getName(), "Hauptwohnsitz fehlt"));
			}
		} 
		if (hasOtherResidence) {
			if (reportingMunicipality != null && !reportingMunicipality.isEmpty()) {
				resultList.add(new ValidationMessage(getName(), "Hauptwohnsitz darf nicht gesetzt sein"));
			}
		}
		
		
		if (secondResidenceNeeded) {
			if (getObject().secondary == null || getObject().secondary.isEmpty()) {
				resultList.add(new ValidationMessage(getName(), "Nebenwohnsitz fehlt"));
			}
		}
		
		if (notMoreThanOneSecondResidence) {
			if (getObject().secondary != null && getObject().secondary.size() > 1) {
				resultList.add(new ValidationMessage(getName(), "Nur 1 Nebenwohnsitz möglich"));
			}
		}
		
		if (reportingMunicipality != null && reportingMunicipality.historyMunicipalityId != null && reportingMunicipality.historyMunicipalityId.startsWith("-")) {
			if (!hasMainResidence) {
				resultList.add(new ValidationMessage(getName(), "Bundesregister als Hauptwohnsitz bei gewähltem Meldeverhältnis nicht möglich"));
			}
		}
		
		if (getObject().secondary != null) {
			for (MunicipalityIdentification municipalityIdentification : getObject().secondary) {
				if (municipalityIdentification.historyMunicipalityId != null && municipalityIdentification.historyMunicipalityId.startsWith("-")) {
					if (!hasSecondResidence) {
						resultList.add(new ValidationMessage(getName(), "Bundesregister als Nebenwohnsitz bei gewähltem Meldeverhältnis nicht möglich"));
						break;
					}
				}
			}
		}
	}

	@Override
	public String getNameOfDependedField() {
		return Person.PERSON.typeOfResidence;
	}

	@Override
	public void setDependedField(EditField<String> field) {
		String value = field.getObject();
		
		if (StringUtils.equals(value, typeOfResidence)) return;
		
		this.typeOfResidence = value;
		if ("3".equals(typeOfResidence)) {
			getObject().reportingMunicipality = null;
		}
		
		fireObjectChange();
	}

	@Override
	public void fillWithDemoData() {
		boolean hasMainResidence = "1".equals(typeOfResidence);
		boolean hasSecondResidence = "2".equals(typeOfResidence);
		boolean hasOtherResidence = "3".equals(typeOfResidence);
		
		boolean mainResidenceNeeded = hasMainResidence || hasSecondResidence;
		boolean secondResidenceNeeded = hasSecondResidence || hasOtherResidence;
		boolean notMoreThanOneSecondResidence = hasSecondResidence || hasOtherResidence;
		
		if (mainResidenceNeeded) {
			getObject().reportingMunicipality = DataGenerator.place().municipalityIdentification;
		} else {
			getObject().reportingMunicipality = null;
		}
		
		List<MunicipalityIdentification> secondaryResidences = new ArrayList<MunicipalityIdentification>();
		if (secondResidenceNeeded) {
			secondaryResidences.add(DataGenerator.place().municipalityIdentification);
		}
		while (!notMoreThanOneSecondResidence && Math.random() < 0.3) {
			secondaryResidences.add(DataGenerator.place().municipalityIdentification);
		}
		getObject().setSecondary(secondaryResidences);
	}

	@Override
	public FormVisual<Residence> createFormPanel() {
		// not used
		return null;
	}

}
