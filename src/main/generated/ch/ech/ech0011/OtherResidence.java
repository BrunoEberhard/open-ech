package ch.ech.ech0011;

import org.minimalj.model.Keys;

// handmade
public class OtherResidence {
	public static final OtherResidence $ = Keys.of(OtherResidence.class);

	public final ResidenceData secondaryResidence = new ResidenceData();

	// old versions of ech 11

	public ResidenceData getSecondaryResidenceInformation() {
		return secondaryResidence;
	}
}