package ch.openech.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.metamodel.generator.ClassValidator;
import org.minimalj.metamodel.model.MjProperty;

import ch.openech.xml.write.EchNamespaceUtil;

public class EchValidateClasses {

	@Test
	public void validateEchClasses() {
		ClassValidator validator = new ClassValidator();
		boolean valid = true;
		for (XsdModel model : EchSchemas.getXsdModels()) {
			if (hasNewerVersion(model)) {
				continue;
			}

			Collection<XsdMjEntity> entities = model.getRootEntities();
			entities = entities.stream().flatMap(e -> collectEntitiesRecusrive(e).stream()).filter(EchSchemas::filter).collect(Collectors.toList());
			try {
				validator.validate(entities);
			} catch (RuntimeException x) {
				valid = false;
				System.out.println(model.getNamespace() + ": " + x.getMessage());
			}
		}
		Assert.assertTrue(valid);
	}

	private Collection<XsdMjEntity> collectEntitiesRecusrive(XsdMjEntity entity) {
		Set<XsdMjEntity> entities = new HashSet<>();
		collectEntitiesRecusrive(entities, entity);
		return entities;
	}

	private void collectEntitiesRecusrive(Set<XsdMjEntity> entities, XsdMjEntity entity) {
		if (!entities.contains(entity)) {
			entities.add(entity);
			for (MjProperty property : entity.properties) {
				// PREDEFINED_TYPES sind nur MjEntities
				if (property.type instanceof XsdMjEntity) {
					collectEntitiesRecusrive(entities, (XsdMjEntity) property.type);
				}
			}
		}
	}

	private boolean hasNewerVersion(XsdModel model) {
		int schemaNumber = EchNamespaceUtil.extractSchemaNumber(model.getNamespace());
		int majorVersion = EchNamespaceUtil.extractSchemaMajorVersion(model.getNamespace());
		for (XsdModel m : EchSchemas.getXsdModels()) {
			int schemaNumber2 = EchNamespaceUtil.extractSchemaNumber(m.getNamespace());
			if (schemaNumber != schemaNumber2)
				continue;
			int majorVersion2 = EchNamespaceUtil.extractSchemaMajorVersion(m.getNamespace());
			if (majorVersion2 <= majorVersion)
				continue;
			return true;
		}
		return false;
	}
}
