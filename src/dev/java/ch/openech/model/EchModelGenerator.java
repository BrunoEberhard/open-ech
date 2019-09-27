package ch.openech.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.metamodel.generator.ClassGenerator;

import ch.openech.xml.EchSchemas;
import ch.openech.xml.EnumPropertyGenerator;
import ch.openech.xml.XsdMjEntity;
import ch.openech.xml.XsdModel;

public class EchModelGenerator {

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
