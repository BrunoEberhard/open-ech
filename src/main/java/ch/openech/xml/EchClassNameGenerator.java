package ch.openech.xml;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.util.StringUtils;

public class EchClassNameGenerator {

	public static String apply(MjEntity t) {
		String name = t.getClassName();
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		if (name.endsWith("Type")) {
			name = name.substring(0, name.length() - 4);
			if (name.endsWith("_")) {
				name = name.substring(0, name.length() - 1);
			}
		}
		name = StringUtils.upperFirstChar(name);
		if (StringUtils.equals(name, "Object", "Class", "Package", "Import", "Public")) {
			name += "_";
		} else if (StringUtils.equals(name, "PartnerShipAbolition")) {
			name = "PartnershipAbolition";
		} else if (StringUtils.equals(name, "Address") && t.packageName.contains("0147")) {
			name = "Address147";
		} else if (StringUtils.equals(name, "Country") && t.packageName.contains("0072")) {
			name = "CountryInformation";
		} else if (StringUtils.equals(name, "PersonIdentification") && t.packageName.contains("0129")) {
			// avoid duplicate name between ech 44 and ech 129
			name = "PersonOrOrganisation";
		} else if (StringUtils.equals(name, "NameOfParentAtBirth")) {
			// NameOfParent existiert schon handmade, damit wird nix erzeugt
			name = "NameOfParent";
		} else if (StringUtils.equals(name, "List")) {
			name = "Liste";
		}
		
		return name;
	}

}
