package  ch.openech.model.contact;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.Address;
import ch.openech.model.types.ContactCategory;
import ch.openech.model.types.PhoneCategory;

public class ContactEntry implements Validation, Rendering {

	public static ContactEntry $ = Keys.of(ContactEntry.class);
	
	public ContactEntryType typeOfContact;
	
	public ContactCategory categoryCode;
	public PhoneCategory phoneCategory;
	
	@Size(EchFormats.freeKategoryText) @Enabled("isCategoryCodeEmpty")
	public String categoryOther;

	@Size(EchFormats.freeKategoryText) @Enabled("isPhoneCategoryEmpty")
	public String phoneCategoryOther;

	// Address only for Address Entries, value for the other types
	public Address address;
	@Size(100)
	public String value;
	
	// Validity
	public LocalDate dateFrom, dateTo;

	public boolean isCategoryCodeEmpty() {
		return categoryCode == null;
	}

	public boolean isPhoneCategoryEmpty() {
		return phoneCategory == null;
	}

	public boolean isAddressEntry() {
		// darf nicht isAddress heissen, weil sonst mit dem Attribut address kollidiert!
		return typeOfContact == ContactEntryType.Address;
	}
	
	public boolean isEmail() {
		return typeOfContact == ContactEntryType.Email;
	}

	public boolean isPhone() {
		return typeOfContact == ContactEntryType.Phone;
	}

	public boolean isInternet() {
		return typeOfContact == ContactEntryType.Internet;
	}
	
	@Override
	public RenderType getPreferredRenderType(RenderType firstType, RenderType... otherTypes) {
		return RenderType.HMTL;
	}

	@Override
	public String render(RenderType renderType) {
		return toHtml();
	}
	
	@Deprecated
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		if (typeOfContact != null) {
			if (categoryCode != null) {
				if (isAddressEntry() | isEmail() || isInternet()) s.append(EnumUtils.getText(categoryCode));
				if (isPhone()) s.append(EnumUtils.getText(phoneCategory));
			} else if (!StringUtils.isBlank(categoryOther)) {
				s.append(categoryOther);
			} else if (!StringUtils.isBlank(phoneCategoryOther)) {
				s.append(phoneCategoryOther);
			} else {
				if (isAddressEntry()) s.append("Adresse");
				if (isPhone()) s.append("Telefon");
				if (isEmail()) s.append("Email");
				if (isInternet()) s.append("Internet");
			}
			s.append("<BR>");
		}
		s.append("<SMALL>");
		if (dateFrom != null && dateTo != null) {
			s.append("Gültig ");
			s.append(DateUtils.format(dateFrom)); 
			s.append(" - ");
			s.append(DateUtils.format(dateTo)); 
			s.append("<BR>");
		} else if (dateFrom != null) {
			s.append("Gültig ab ");
			s.append(DateUtils.format(dateFrom)); 
			s.append("<BR>");
		} else if (dateTo != null) {
			s.append("Gültig bis ").append(DateUtils.format(dateTo));
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
		
		return s.toString();
	}

	@Override
	public List<ValidationMessage> validate() {
		List<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();
		if (dateFrom != null && dateTo != null) {
			if (dateFrom.isAfter(dateTo)) {
				validationMessages.add(new ValidationMessage($.dateTo, "Zeitraum ungültig"));
			}
		}
		
		// Die Regex stammen aus dem eCH-0046-2-0.xsd
		if (isAddressEntry()) {
			if (address == null || address.isEmpty()) {
				validationMessages.add(new ValidationMessage($.address, "Adresse erforderlich"));
			}
		} else {
			if (StringUtils.isEmpty(value)) {
				validationMessages.add(new ValidationMessage($.value, "Eingabe erforderlich"));
			} else if (isEmail()) {
				String regex = "[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+(\\.[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+)*@[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+(\\.[A-Za-z0-9!#-'\\*\\+\\-/=\\?\\^_`\\{-~]+)*";
				if (!Pattern.matches(regex, value)) {
					validationMessages.add(new ValidationMessage($.value, "Ungültige EMail - Adresse"));
				}
			} else if (isPhone()) {
				String regex = "\\d{10,20}";
				if (!Pattern.matches(regex, value)) {
					validationMessages.add(new ValidationMessage($.value, "Ungültige Telefonnummer (10-20 Zahlen)"));
				}
			} else if (isInternet()) {
				String regex = "http://.*";
				if (!Pattern.matches(regex, value)) {
					validationMessages.add(new ValidationMessage($.value, "Ungültige Internetadresse (muss mit http:// beginnen)"));
				}
			}
		}
		return validationMessages;
	}
	
}
