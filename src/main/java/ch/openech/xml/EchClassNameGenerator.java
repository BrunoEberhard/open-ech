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
		}
		return StringUtils.upperFirstChar(name);
	}

}
