package ch.openech.dm.contact;

import java.util.List;
import java.util.regex.Pattern;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.db.model.annotation.Varchar;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class ContactEntry implements Validatable {

	public static ContactEntry CONTACT_ENTRY = Constants.of(ContactEntry.class);
	
	public String typeOfContact; // A, E, P, I
	
	@Varchar(2)
	public String categoryCode;
	@FormatName("freeKategoryText") // sic!
	public String categoryOther;
	
	// Address only for Address Entries, value for the other types
	public Address address;
	@FormatName("emailAddress")
	public String value;
	
	// Validity
	@Date
	public String dateFrom, dateTo;

	public boolean isAddressEntry() {
		// darf nicht isAddress heissen, weil sonst mit dem Attribut address kollidiert!
		return "A".equals(typeOfContact);
	}
	
	public boolean isEmail() {
		return "E".equals(typeOfContact);
	}

	public boolean isPhone() {
		return "P".equals(typeOfContact);
	}

	public boolean isInternet() {
		return "I".equals(typeOfContact);
	}
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		if (typeOfContact != null) {
			if (categoryCode != null) {
				if (isAddressEntry()) s.append(EchCodes.addressCategory.getText(categoryCode));
				if (isPhone()) s.append(EchCodes.phoneCategory.getText(categoryCode));
				if (isEmail()) s.append(EchCodes.emailCategory.getText(categoryCode));
				if (isInternet()) s.append(EchCodes.internetCategory.getText(categoryCode));
			} else if (!StringUtils.isBlank(categoryOther)) {
				s.append(categoryOther);
			} else {
				if (isAddressEntry()) s.append("Adresse");
				if (isPhone()) s.append("Telefon");
				if (isEmail()) s.append("Email");
				if (isInternet()) s.append("Internet");
			}
			s.append("<BR>");
		}
		s.append("<SMALL>");
		if (!StringUtils.isBlank(dateFrom) && !StringUtils.isBlank(dateTo)) {
			s.append("Gültig ");
			s.append(DateUtils.formatCH(dateFrom)); 
			s.append(" - ");
			s.append(DateUtils.formatCH(dateTo)); 
			s.append("<BR>");
		} else if (!StringUtils.isBlank(dateFrom)) {
			s.append("Gültig ab ");
			s.append(DateUtils.formatCH(dateFrom)); 
			s.append("<BR>");
		} else if (!StringUtils.isBlank(dateTo)) {
			s.append("Gültig bis "); s.append(DateUtils.formatCH(dateTo));
			s.append("<BR>");
		}
		s.append("</SMALL>");

		if (isAddressEntry()) {
			if (address != null) {
				address.toHtml(s);
			}
		} else {
			if (!StringUtils.isEmpty(value)) {
				s.append(value);
				s.append("<BR>");
			}
		}
		
		s.append("</HTML>");
		return s.toString();
	}

	@Override
	public void validate(List<ValidationMessage> validationMessages) {
		// Die Regex stammen aus dem eCH-0046-2-0.xsd
		if (isAddressEntry()) {
			if (address == null || address.isEmpty()) {
				validationMessages.add(new ValidationMessage(CONTACT_ENTRY.address, "Adresse erforderlich"));
			}
		} else {
			if (StringUtils.isEmpty(value)) {
				validationMessages.add(new ValidationMessage(CONTACT_ENTRY.value, "Eingabe erforderlich"));
			} else if (isEmail()) {
				String regex = "[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+(\\.[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+)*@[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+(\\.[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+)*";
				if (!Pattern.matches(regex, value)) {
					validationMessages.add(new ValidationMessage(CONTACT_ENTRY.value, "Ungültige EMail - Adresse"));
				}
			} else if (isPhone()) {
				String regex = "\\d{10,20}";
				if (!Pattern.matches(regex, value)) {
					validationMessages.add(new ValidationMessage(CONTACT_ENTRY.value, "Ungültige Telefonnummer (10-20 Zahlen)"));
				}
			} else if (isInternet()) {
				String regex = "http://.*";
				if (!Pattern.matches(regex, value)) {
					validationMessages.add(new ValidationMessage(CONTACT_ENTRY.value, "Ungültige Internetadresse (muss mit http:// beginnen)"));
				}
			}
		}
	}
	
}
