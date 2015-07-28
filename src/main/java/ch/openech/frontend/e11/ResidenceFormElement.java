package ch.openech.frontend.e11;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.frontend.e07.MunicipalityFormElement;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.person.Residence;
import ch.openech.model.person.SecondaryResidence;
import ch.openech.model.types.TypeOfResidence;

public class ResidenceFormElement extends ObjectFormElement<Residence> {

	public ResidenceFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	// Als "Part" wird hier das ganze Objekt editiert. Das entsteht, weil eigentlich eine
	// ComboBox im Hauptfeld angezeigt werden müsste, dies durch die Möglichkeit der
	// Nebenwohnorte anzuzueigen aber nicht möglich ist.
	// Möglich Lösung: Zweiteilung des Eingabefeldes
	public final class ResidenceMainEditor extends Editor<Residence, Void> {

		@Override
		public Form<Residence> createForm() {
			Form<Residence> form = new EchForm<Residence>();
			form.line(new MunicipalityFormElement(Residence.$.reportingMunicipality, true));
			return form;
		}

		@Override
		public Residence createObject() {
			Residence residenceEdit = new Residence();
			residenceEdit.reportingMunicipality = getValue().reportingMunicipality;
			return residenceEdit;
		}

		@Override
		protected Void save(Residence entry) {
			setValue(entry);
			return null;
		}
		
		@Override
		protected void finished(Void result) {
			handleChange();
		}
	}

	public final class ResidenceAddSecondaryEditor extends Editor<Residence, Void> {

		@Override
		public Form<Residence> createForm() {
			Form<Residence> form = new EchForm<Residence>();
			form.line(new MunicipalityFormElement(Residence.$.reportingMunicipality, true));
			return form;
		}

		@Override
		public Residence createObject() {
			Residence residence = new Residence();
			residence.reportingMunicipality = ResidenceFormElement.this.getValue().reportingMunicipality;
			return residence;
		}

		@Override
		public Void save(Residence residence) {
			ResidenceFormElement.this.getValue().secondary.add(new SecondaryResidence(residence.reportingMunicipality));
			return null;
		}
		
		@Override
		protected void finished(Void result) {
			handleChange();
		}
	}

	private final class ResidenceRemoveSecondaryAction extends Action {
		@Override
		public void action() {
			getValue().secondary.clear();
			handleChange();
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
			for (SecondaryResidence secondaryResidence : residence.secondary) {
				s.append(secondaryResidence.municipalityIdentification);
				s.append(", ");
			}
			s.delete(s.length() - 2, s.length()); // cut last ", "
			hasResidence = true;
		} 
		
		if (hasResidence) {
			add(s.toString());
		} else {
			add("Kein Wohnsitz");
		}
		
		// Bei Nebenwohnsitz darf es nur einer sein, daher auch die Aktion nur bei leerer Liste anbieten
		// if (selectSecondary != null) selectSecondary.setEnabled(TypeOfResidence.hasOtherResidence != typeOfResidence || residence.secondary == null || residence.secondary.isEmpty());
	}
	
	
	
	@Override
	protected Action[] getActions() {
//		boolean hasMainResidence = TypeOfResidence.hasMainResidence == typeOfResidence;
//		boolean hasOtherResidence = TypeOfResidence.hasOtherResidence == typeOfResidence;
//
//		if (!hasOtherResidence) {
//			addAction(new ResidenceMainEditor());
//		}
//		if (!hasMainResidence || getObject().secondary.isEmpty()) {
//			addAction(new ResidenceAddSecondaryEditor());
//		}

		return new Action[] {
				new ResidenceMainEditor(),
				new ResidenceAddSecondaryEditor(),
				new ResidenceRemoveSecondaryAction()
		};
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
		
		List<SecondaryResidence> secondaryResidences = new ArrayList<>();
		if (secondResidenceNeeded) {
			secondaryResidences.add(new SecondaryResidence(DataGenerator.place().municipalityIdentification));
		}
		while (!notMoreThanOneSecondResidence && Math.random() < 0.3) {
			secondaryResidences.add(new SecondaryResidence(DataGenerator.place().municipalityIdentification));
		}
		residence.setSecondary(secondaryResidences);
	}

	@Override
	public Form<Residence> createFormPanel() {
		// not used
		return null;
	}

}
