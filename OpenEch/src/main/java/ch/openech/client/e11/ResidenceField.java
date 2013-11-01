package ch.openech.client.e11;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.client.e07.MunicipalityField;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Residence;
import ch.openech.dm.types.TypeOfResidence;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class ResidenceField extends ObjectFlowField<Residence> {
	private Action selectSecondary;

	public ResidenceField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	// 		setTitle(main ? "Hauptwohnsitz erfassen" : "Nebenwohnsitz erfassen");

	// Als "Part" wird hier das ganze Objekt editiert. Das entsteht, weil eigentlich eine
	// ComboBox im Hauptfeld angezeigt werden müsste, dies durch die Möglichkeit der
	// Nebenwohnorte anzuzueigen aber nicht möglich ist.
	// Möglich Lösung: Zweiteilung des Eingabefeldes
	public final class ResidenceMainEditor extends ObjectFieldPartEditor<Residence> {

		@Override
		public IForm<Residence> createForm() {
			Form<Residence> form = new EchForm<Residence>();
			form.line(new MunicipalityField(Residence.RESIDENCE.reportingMunicipality, true));
			return form;
		}

		@Override
		public Residence getPart(Residence residence) {
			Residence residenceEdit = new Residence();
			residenceEdit.reportingMunicipality = residence.reportingMunicipality;
			return residenceEdit;
		}

		@Override
		public void setPart(Residence residence, Residence value) {
			setObject(value);
		}
	}

	public final class ResidenceAddSecondaryEditor extends Editor<Residence> {

		@Override
		public IForm<Residence> createForm() {
			Form<Residence> form = new EchForm<Residence>();
			form.line(new MunicipalityField(Residence.RESIDENCE.reportingMunicipality, true));
			return form;
		}

		@Override
		public Residence load() {
			Residence residence = new Residence();
			residence.reportingMunicipality = ResidenceField.this.getObject().reportingMunicipality;
			return residence;
		}

		@Override
		public Object save(Residence residence) {
			ResidenceField.this.getObject().secondary.add(residence.reportingMunicipality);
			fireObjectChange();
			return SAVE_SUCCESSFUL;
		}
	}

	private final class ResidenceRemoveSecondaryAction extends ResourceAction {
		@Override
		public void action(IComponent context) {
			getObject().secondary.clear();
			fireObjectChange();
		}
	}
	
	@Override
	public void show(Residence residence) {
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
			addText(s.toString());
		} else {
			addText("Kein Wohnsitz");
		}
		
		// Bei Nebenwohnsitz darf es nur einer sein, daher auch die Aktion nur bei leerer Liste anbieten
		// if (selectSecondary != null) selectSecondary.setEnabled(TypeOfResidence.hasOtherResidence != typeOfResidence || residence.secondary == null || residence.secondary.isEmpty());
	}
	
	
	@Override
	protected void showActions() {
		if (getObject() == null) return;

//		boolean hasMainResidence = TypeOfResidence.hasMainResidence == typeOfResidence;
//		boolean hasOtherResidence = TypeOfResidence.hasOtherResidence == typeOfResidence;
//
//		if (!hasOtherResidence) {
//			addAction(new ResidenceMainEditor());
//		}
//		if (!hasMainResidence || getObject().secondary.isEmpty()) {
//			addAction(new ResidenceAddSecondaryEditor());
//		}

		addAction(new ResidenceMainEditor());
		addAction(new ResidenceAddSecondaryEditor());

		if (!getObject().secondary.isEmpty()) {
			addAction(new ResidenceRemoveSecondaryAction());
		}
	}

	public static void fillWithMockupData(Residence residence, TypeOfResidence typeOfResidence) {
		boolean hasMainResidence = TypeOfResidence.hasMainResidence == typeOfResidence;
		boolean hasSecondResidence = TypeOfResidence.hasSecondaryResidence == typeOfResidence;
		boolean hasOtherResidence = TypeOfResidence.hasOtherResidence == typeOfResidence;
		
		boolean mainResidenceNeeded = hasMainResidence || hasSecondResidence;
		boolean secondResidenceNeeded = hasSecondResidence || hasOtherResidence;
		boolean notMoreThanOneSecondResidence = hasSecondResidence || hasOtherResidence;
		
		if (mainResidenceNeeded) {
			residence.reportingMunicipality = DataGenerator.municipalityIdentification();
		} else {
			residence.reportingMunicipality = null;
		}
		
		List<MunicipalityIdentification> secondaryResidences = new ArrayList<MunicipalityIdentification>();
		if (secondResidenceNeeded) {
			secondaryResidences.add(DataGenerator.place().municipalityIdentification);
		}
		while (!notMoreThanOneSecondResidence && Math.random() < 0.3) {
			secondaryResidences.add(DataGenerator.place().municipalityIdentification);
		}
		residence.setSecondary(secondaryResidences);
	}

	@Override
	public IForm<Residence> createFormPanel() {
		// not used
		return null;
	}

}
