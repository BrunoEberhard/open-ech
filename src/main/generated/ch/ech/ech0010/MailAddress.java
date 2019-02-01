package ch.ech.ech0010;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.StringUtils;

public class MailAddress implements Rendering {
	public static final MailAddress $ = Keys.of(MailAddress.class);

	public final AddressNames names = new AddressNames();
	public final AddressInformation addressInformation = new AddressInformation();

	public AddressNames getPerson() {
		if (names.organisationName == null) {
			return names;
		} else {
			return null;
		}
	}

	public void setPerson(AddressNames names) {
		if (names == null) {
			names = EmptyObjects.getEmptyObject(AddressNames.class);
		}
		CloneHelper.deepCopy(names, this.names);
	}

	public AddressNames getOrganisation() {
		if (names.organisationName != null) {
			return names;
		} else {
			return null;
		}
	}

	public void setOrganisation(AddressNames names) {
		if (names == null) {
			names = EmptyObjects.getEmptyObject(AddressNames.class);
		}
		CloneHelper.deepCopy(names, this.names);
	}

	@Override
	public CharSequence render() {
		StringBuilder s = new StringBuilder();
		append(s);
		return s;
	}

	public void append(StringBuilder s) {
		names.render(s);
		addressInformation.render(s);
	}

	// Das ist die Zusammenfassung von PersonMailAddressInfo und
	// OrganisationMailAddressInfo
	public static class AddressNames {
		public static final AddressNames $ = Keys.of(AddressNames.class);

		// organisationName muss für eine Organisation gesetzt sein. Wird daher für
		// Unterscheidung zwischen Person und Firma verwendet
		@Size(60)
		public String organisationName, organisationNameAddOn1, organisationNameAddOn2;

		public MrMrs mrMrs;
		@Size(50)
		public String title;
		@Size(30)
		public String firstName, lastName;

		public void render(StringBuilder s) {
			StringUtils.appendLine(s, organisationName);
			StringUtils.appendLine(s, organisationNameAddOn1);
			StringUtils.appendLine(s, organisationNameAddOn2);

			StringUtils.appendLine(s, EnumUtils.getText(mrMrs), title, firstName, lastName);
		}
	}


}