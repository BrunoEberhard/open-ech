package ch.openech.xml;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.metamodel.generator.ClassValidator;
import org.minimalj.metamodel.model.MjEntity;

public class EchValidateClasses {

	@Test
	public void validateEchClasses() {
		ClassValidator validator = new ClassValidator();
		boolean valid = true;
		for (XsdModel model : EchSchemas.getXsdModels()) {
			Collection<MjEntity> entities = model.getEntities();
			entities = entities.stream().filter(EchSchemas::filter).collect(Collectors.toList());
			try {
				validator.validate(entities);
			} catch (RuntimeException x) {
				valid = false;
				System.out.println(model.getNamespace() + ": " + x.getMessage());
			}
		}
		Assert.assertTrue(valid);
	}
}
