package ch.openech.dm.contact;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Int9;

import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;

// e46
// Für Personen, aber auch für Vermieter
public class Contact {

	public static final Contact CONTACT = Constants.of(Contact.class);
	
	@Is(Int9)
	// TODO in Contact stringId eventuell durch NamedId ersetzen
	public String stringId;
	public final List<ContactEntry> entries = new ArrayList<ContactEntry>();
	
	public List<ContactEntry> getAddressList() {
		List<ContactEntry> result = new ArrayList<ContactEntry>();
		for (ContactEntry entry : entries) {
			if (entry.isAddressEntry()) {
				result.add(entry);
			}
		}
		return result;
	}

	public List<ContactEntry> getEmailList() {
		List<ContactEntry> result = new ArrayList<ContactEntry>();
		for (ContactEntry entry : entries) {
			if (entry.isEmail()) {
				result.add(entry);
			}
		}
		return result;
	}

	public List<ContactEntry> getPhoneList() {
		List<ContactEntry> result = new ArrayList<ContactEntry>();
		for (ContactEntry entry : entries) {
			if (entry.isPhone()) {
				result.add(entry);
			}
		}
		return result;
	}

	public List<ContactEntry> getInternetList() {
		List<ContactEntry> result = new ArrayList<ContactEntry>();
		for (ContactEntry entry : entries) {
			if (entry.isInternet()) {
				result.add(entry);
			}
		}
		return result;
	}

}
