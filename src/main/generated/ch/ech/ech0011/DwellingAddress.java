package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class DwellingAddress {
	public static final DwellingAddress $ = Keys.of(DwellingAddress.class);

	@Size(9)
	public Integer EGID;
	@Size(3)
	public Integer EWID;
	@Size(255) // unknown
	public String householdID;
	public final ch.ech.ech0010.SwissAddressInformation address = new ch.ech.ech0010.SwissAddressInformation();
	@NotEmpty
	public TypeOfHousehold typeOfHousehold;
	public LocalDate movingDate;
	
	//
	
	private final WithoutEGID withoutEGID = new WithoutEGID();
	
	public WithoutEGID getWithoutEGID() {
		return withoutEGID;
	}
	
	public class WithoutEGID {

		public String getHouseholdID() {
			return householdID;
		}

		public void setHouseholdID(String id) {
			householdID = id;
		}
	}
}