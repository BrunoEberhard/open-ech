package  ch.openech.model.person;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.types.TypeOfResidence;

public class Residence {
	public static final Residence $ = Keys.of(Residence.class);
	
	
	public MunicipalityIdentification reportingMunicipality;
	public final List<SecondaryResidence> secondary = new ArrayList<>(); // nur bei hasMainResidence

	public void setSecondary(List<SecondaryResidence> secondary2) {
		if (secondary2 != secondary) {
			secondary.clear();
			secondary.addAll(secondary2);
		}
	}

	public String validate(TypeOfResidence typeOfResidence) {
		boolean hasMainResidence = TypeOfResidence.hasMainResidence == typeOfResidence;
		boolean hasSecondResidence = TypeOfResidence.hasSecondaryResidence == typeOfResidence;
		boolean hasOtherResidence = TypeOfResidence.hasOtherResidence == typeOfResidence;
		
		boolean mainResidenceNeeded = hasMainResidence || hasSecondResidence;
		boolean secondResidenceNeeded = hasSecondResidence || hasOtherResidence;
		boolean notMoreThanOneSecondResidence = hasSecondResidence || hasOtherResidence;
		
		if (mainResidenceNeeded) {
			if (reportingMunicipality == null || reportingMunicipality.isEmpty()) {
				return "Hauptwohnsitz erforderlich";
			}
		} 
		if (hasOtherResidence) {
			if (reportingMunicipality != null && !reportingMunicipality.isEmpty()) {
				return "Hauptwohnsitz darf nicht gesetzt sein";
			}
		}
		
		
		if (secondResidenceNeeded) {
			if (secondary == null || secondary.isEmpty()) {
				return "Nebenwohnsitz fehlt";
			}
		}
		
		if (notMoreThanOneSecondResidence) {
			if (secondary != null && secondary.size() > 1) {
				return "Nur 1 Nebenwohnsitz möglich";
			}
		}
		
		if (reportingMunicipality != null && reportingMunicipality.isFederalRegister()) {
			if (!hasMainResidence) {
				return "Bundesregister als Hauptwohnsitz bei gewähltem Meldeverhältnis nicht möglich";
			}
		}
		
		for (SecondaryResidence secondaryResidence : secondary) {
			MunicipalityIdentification municipalityIdentification = secondaryResidence.municipalityIdentification;
			if (municipalityIdentification.isFederalRegister()) {
				if (!hasSecondResidence) {
					return "Bundesregister als Nebenwohnsitz bei gewähltem Meldeverhältnis nicht möglich";
				}
			}
		}
		
		return null;
	}

	// besser als 1. Version?
	public void validate(TypeOfResidence typeOfResidence, PropertyInterface myProperty, List<ValidationMessage> resultList) {
		boolean hasMainResidence = TypeOfResidence.hasMainResidence == typeOfResidence;
		boolean hasSecondResidence = TypeOfResidence.hasSecondaryResidence == typeOfResidence;
		boolean hasOtherResidence = TypeOfResidence.hasOtherResidence == typeOfResidence;
		
		boolean mainResidenceNeeded = hasMainResidence || hasSecondResidence;
		boolean secondResidenceNeeded = hasSecondResidence || hasOtherResidence;
		boolean notMoreThanOneSecondResidence = hasSecondResidence || hasOtherResidence;
		
		if (mainResidenceNeeded) {
			if (reportingMunicipality == null || reportingMunicipality.isEmpty()) {
				resultList.add(new ValidationMessage(myProperty, "Hauptwohnsitz erforderlich"));
			}
		} 
		if (hasOtherResidence) {
			if (reportingMunicipality != null && !reportingMunicipality.isEmpty()) {
				resultList.add(new ValidationMessage(myProperty, "Hauptwohnsitz darf nicht gesetzt sein"));
			}
		}
		
		
		if (secondResidenceNeeded) {
			if (secondary == null || secondary.isEmpty()) {
				resultList.add(new ValidationMessage(myProperty, "Nebenwohnsitz fehlt"));
			}
		}
		
		if (notMoreThanOneSecondResidence) {
			if (secondary != null && secondary.size() > 1) {
				resultList.add(new ValidationMessage(myProperty, "Nur 1 Nebenwohnsitz möglich"));
			}
		}
		
		if (reportingMunicipality != null && reportingMunicipality.isFederalRegister()) {
			if (!hasMainResidence) {
				resultList.add(new ValidationMessage(myProperty, "Bundesregister als Hauptwohnsitz bei gewähltem Meldeverhältnis nicht möglich"));
			}
		}
		
		for (SecondaryResidence secondaryResidence : secondary) {
			MunicipalityIdentification municipalityIdentification = secondaryResidence.municipalityIdentification;
			if (municipalityIdentification.isFederalRegister()) {
				if (!hasSecondResidence) {
					resultList.add(new ValidationMessage(myProperty, "Bundesregister als Nebenwohnsitz bei gewähltem Meldeverhältnis nicht möglich"));
					break;
				}
			}
		}
	}
}
