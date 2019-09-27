package ch.openech.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.metamodel.generator.ClassGenerator;
import org.minimalj.metamodel.generator.GeneratorEntity;
import org.minimalj.util.StringUtils;

import ch.openech.xml.EchSchemas;
import ch.openech.xml.EnumPropertyGenerator;
import ch.openech.xml.XsdMjEntity;
import ch.openech.xml.XsdModel;

public class EchModelGenerator {

	private static class EchClassNameGenerator {

		public static String apply(GeneratorEntity t) {
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

	public static void main(String[] args) throws Exception {
		ClassGenerator generator = new ClassGenerator("./src/main/model");
		EnumPropertyGenerator enumPropertyGenerator = new EnumPropertyGenerator("./src/main/model/enum.properties");

		List<XsdModel> sortedModels = new ArrayList<>(EchSchemas.getXsdModels());
		sortedModels.sort((m1, m2) -> m1.getNamespace().compareTo(m2.getNamespace()));

		for (XsdModel model : sortedModels) {
			Collection<XsdMjEntity> entities = model.getEntities();
			entities = entities.stream().filter(EchSchemas::filter).collect(Collectors.toList());
			generator.generate(entities);
			enumPropertyGenerator.generate(entities);
		}
	}

}
