package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade
public class MaritalData {
	public static final MaritalData $ = Keys.of(MaritalData.class);

	@NotEmpty
	public MaritalStatus maritalStatus;
	public LocalDate dateOfMaritalStatus;
	public PartnershipAbolition cancelationReason;
	public Boolean officialProofOfMaritalStatusYesNo;

	// separationData wird nicht final generiert
	
	public final SeparationData separationData = new SeparationData();

	public Separation getSeparation() {
		return separationData.separation;
	}
	
	public void setSeparation(Separation separation) {
		separationData.separation = separation;
	}

	public LocalDate getDateOfSeparation() {
		return separationData.separationValidFrom;
	}

	public void setDateOfSeparation(LocalDate date) {
		separationData.separationValidFrom = date;
	}
	
	public LocalDate getseparationTill() {
		return separationData.separationValidTill;
	}

	public void setSeparationTill(LocalDate date) {
		separationData.separationValidTill = date;
	}

}