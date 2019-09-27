package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0010.AddressInformation;

// @Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-07-08T07:20:57.158")
// handmade: comesFrom, goesTo final
public class ResidenceData {
	public static final ResidenceData $ = Keys.of(ResidenceData.class);

	public SwissMunicipality reportingMunicipality;
	@NotEmpty
	public LocalDate arrivalDate;
	public Destination comesFrom;
	public DwellingAddress dwellingAddress;
	public LocalDate departureDate;
	public Destination goesTo;

	public GeneralPlace getComesFromPlace() {
		if (Keys.isKeyObject(this))
			return Keys.methodOf(this, "comesFromPlace");

		return comesFrom != null ? comesFrom.generalPlace : null;
	}

	public void setComesFromPlace(GeneralPlace place) {
		if (place != null) {
			if (comesFrom == null) {
				comesFrom = new Destination();
			}
			CloneHelper.deepCopy(place, comesFrom.generalPlace);
		} else {
			comesFrom = null;
		}
	}

	public AddressInformation getComesFromAddress() {
		if (Keys.isKeyObject(this))
			return Keys.methodOf(this, "comesFromAddress", $.getComesFromPlace());

		return comesFrom != null ? comesFrom.mailAddress : null;
	}

	public void setComesFromAddress(AddressInformation address) {
		if (comesFrom != null) {
			comesFrom.mailAddress = address;
		}
	}

}