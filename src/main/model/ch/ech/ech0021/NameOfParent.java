package ch.ech.ech0021;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

// handmade , wird auch als NameOfParentAtBirth benutzt, dort existiert kein officialProofOfNameOfParentsYesNo
public class NameOfParent implements Rendering {
	public static final NameOfParent $ = Keys.of(NameOfParent.class);

	@Size(100)
	public String firstNameValue;
	@Size(100)
	public String officialNameValue;
	public Boolean officialProofOfNameOfParentsYesNo;
	
	@Override
	public String render() {
		StringBuilder s = new StringBuilder(200);
		if (!StringUtils.isEmpty(firstNameValue)) {
			s.append(firstNameValue);
		}
		if (!StringUtils.isEmpty(officialNameValue)) {
			if (s.length() > 0)
				s.append(' ');
			s.append(officialNameValue);
		}
		if (Boolean.TRUE.equals(officialProofOfNameOfParentsYesNo)) {
			if (s.length() > 0)
				s.append(' ');
			s.append(" *");
		}
		return s.toString();
	}

	// Das ech Schema ist hier etwas über engineert.
	// die only element sollen jeweils nur vorhanden sein, wenn
	// das andere es nicht ist. Die 'normalen' element sollen nur
	// vorhanden sein, wenn beide da sind.
	// die Felder heissen XyValue, damit sie den gettern
	// nicht in die Quere kommen.
	
	public String getFirstName() {
		if (!StringUtils.isEmpty(officialNameValue)) {
			return firstNameValue;
		} else {
			return null;
		}
	}

	public String getFirstNameOnly() {
		if (StringUtils.isEmpty(officialNameValue)) {
			return firstNameValue;
		} else {
			return null;
		}
	}
	
	public String getOfficialName() {
		if (!StringUtils.isEmpty(firstNameValue)) {
			return officialNameValue;
		} else {
			return null;
		}
	}

	public String getOfficialNameOnly() {
		if (StringUtils.isEmpty(firstNameValue)) {
			return officialNameValue;
		} else {
			return null;
		}
	}

	public void setFirstName(String firstName) {
		this.firstNameValue = firstName;
	}

	public void setFirstNameOnly(String firstNameOnly) {
		this.firstNameValue = firstNameOnly;
	}

	public void setOfficialName(String officialName) {
		this.officialNameValue = officialName;
	}

	public void setOfficialNameOnly(String officialNameOnly) {
		this.officialNameValue = officialNameOnly;
	}
	
}