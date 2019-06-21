package ch.ech.ech0021;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0010.MailAddress;

// insurance muss handgemacht sein
public class HealthInsuranceData {
	public static final HealthInsuranceData $ = Keys.of(HealthInsuranceData.class);

	@NotEmpty
	public ch.openech.xml.YesNo healthInsured;
	public Insurance insurance = new Insurance();
	public LocalDate healthInsuranceValidFrom;

	public static class Insurance {
		public Object id;

		private String insuranceName;
		private MailAddress address;

		public String getInsuranceName() {
			if (address == null) {
				return insuranceName;
			} else {
				return null;
			}
		}

		public void setInsuranceName(String insuranceName) {
			this.insuranceName = insuranceName;
			if (insuranceName != null) {
				address = null;
			}
		}

		public MailAddress getInsuranceAddress() {
			return address;
		}

		public void setInsuranceAddress(MailAddress address) {
			this.address = address;
			if (address != null) {
				this.insuranceName = null;
			}
		}
	}

}