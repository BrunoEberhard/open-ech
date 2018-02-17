package ch.openech.xml;

import java.util.function.Function;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.util.StringUtils;

public class EchClassNameGenerator implements Function<MjEntity, String> {

	@Override
	public String apply(MjEntity t) {
		String name = t.getClassName();
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		if (name.endsWith("Type")) {
			name = name.substring(0, name.length() - 4);
		}
		return StringUtils.upperFirstChar(name);
	}

}
