package ch.ech.ech0011;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0007.SwissMunicipality;

// handmade. Kann ev entfernt werden. Die Residence Felder
// sind in die Person integriert. Und in den Events
// wird die Klasse auch nirgends verwendet.
public class ReportedPerson {
	public static final ReportedPerson $ = Keys.of(ReportedPerson.class);

	@NotEmpty
	public Person person;

	//

	private final MainResidenceDelegate mainResidenceDelegate = new MainResidenceDelegate();
	private final SecondaryResidenceDelegate secondaryResidenceDelegate = new SecondaryResidenceDelegate();
	private final OtherResidenceDelegate otherResidenceDelegate = new OtherResidenceDelegate();

	//

	public class MainResidenceDelegate {

		public List<SwissMunicipality> getSecondaryResidence() {
			return person.secondaryResidence;
		}

		public void setSecondaryResidence(List<SwissMunicipality> s) {
			person.secondaryResidence = s;
		}

		public ResidenceData getMainResidence() {
			return person.residenceData;
		}
	}

	public MainResidenceDelegate getHasMainResidence() {
		return mainResidenceDelegate;
	}

	public void setHasMainResidence(MainResidenceDelegate mainResidenceDelegate) {
		CloneHelper.deepCopy(mainResidenceDelegate, this.mainResidenceDelegate);
	}

	//

	public SecondaryResidenceDelegate getHasSecondaryResidence() {
		return secondaryResidenceDelegate;
	}

	public void setHasSecondaryResidence(SecondaryResidenceDelegate secondaryResidenceDelegate) {
		CloneHelper.deepCopy(secondaryResidenceDelegate, this.secondaryResidenceDelegate);
	}

	public class SecondaryResidenceDelegate {

		public SwissMunicipality getMainResidence() {
			return person.residenceData.reportingMunicipality;
		}

		public ResidenceData getSecondaryResidence() {
			return person.residenceData;
		}
	}

	//

	public OtherResidenceDelegate getHasOtherResidence() {
		return otherResidenceDelegate;
	}

	public void setHasOtherResidence(SecondaryResidenceDelegate otherResidenceDelegate) {
		CloneHelper.deepCopy(otherResidenceDelegate, this.otherResidenceDelegate);
	}

	public class OtherResidenceDelegate {

		public ResidenceData getSecondaryResidence() {
			return person.residenceData;
		}

		// hies in ech 11 früher so
		public ResidenceData getSecondaryResidenceInformation() {
			return person.residenceData;
		}
	}

	//

	private final AnyPerson anyPerson = new AnyPerson();

	public AnyPerson getAnyPerson() {
		return anyPerson;
	}

	public class AnyPerson {

		private Swiss swiss;
		private Foreigner foreigner;

		public class Swiss {
			// Nicht implementiert. Auf der Person ist ein Liste von erweiterten
			// PlaceOfOrginAddOn
			public List<PlaceOfOrigin> placeOfOrigin;
		}

		public class Foreigner {
			public ch.ech.ech0006.ResidencePermit getResidencePermit() {
				return person.residencePermit.getResidencePermit();
			}

			public void setResidencePermit(ch.ech.ech0006.ResidencePermit residencePermit) {
				person.residencePermit.setResidencePermit(residencePermit);
			}

			public LocalDate getResidencePermitTill() {
				return person.residencePermit.residencePermitValidTill;
			}

			public void setResidencePermitTill(LocalDate residencePermitTill) {
				person.residencePermit.residencePermitValidTill = residencePermitTill;
			}

			public String getNameOnPassport() {
				return person.nameData.nameOnForeignPassport.firstName;
			}

			public void setNameOnPassport(String nameOnPassport) {
				// ignored
			}
		}

		public Swiss getSwiss() {
			return swiss;
		}

		public void setSwiss(Swiss swiss) {
			this.swiss = swiss;
		}

		public Foreigner getForeigner() {
			return foreigner;
		}

		public void setForeigner(Foreigner foreigner) {
			this.foreigner = foreigner;
		}
	}
}