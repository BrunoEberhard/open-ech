package ch.openech.xml;

import org.junit.Test;
import org.minimalj.metamodel.generator.ClassValidator;

public class EchValidateClasses {

	@Test
	public void validateEchClasses() {
		ClassValidator validator = new ClassValidator();
		for (XsdModel model : EchSchemas.getXsdModels()) {
			validator.validate(model.getEntities());
		}
	}
}
