package ch.ech.ech0011.v8;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0007.v4.SwissMunicipality;

//handmade
public class ReportedPerson {
	public static final ReportedPerson $ = Keys.of(ReportedPerson.class);

	@NotEmpty
	public Person person;

	public final ResidenceData residenceData = new ResidenceData();

	public final SwissMunicipality mainResidence = new SwissMunicipality();
	public List<SwissMunicipality> secondaryResidence;
	
	// public MainResidence hasMainResidence;
	// public SecondaryResidence hasSecondaryResidence;
	// public OtherResidence hasOtherResidence;

	//
	
	private final MainResidenceDelegate mainResidenceDelegate = new MainResidenceDelegate();
	private final SecondaryResidenceDelegate secondaryResidenceDelegate = new SecondaryResidenceDelegate();
	private final OtherResidenceDelegate otherResidenceDelegate = new OtherResidenceDelegate();

	//
	
	public class MainResidenceDelegate {

		public List<SwissMunicipality> getSecondaryResidence() {
			return secondaryResidence;
		}

		public void setSecondaryResidence(List<SwissMunicipality> s) {
			secondaryResidence = s;
		}

		public ResidenceData getMainResidence() {
			return residenceData;
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
			return mainResidence;
		}

		public ResidenceData getSecondaryResidence() {
			return residenceData;
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
			return residenceData;
		}
	}


}