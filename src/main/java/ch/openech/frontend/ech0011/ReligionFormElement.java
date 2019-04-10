package ch.openech.frontend.ech0011;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormatFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

public class ReligionFormElement extends FormatFormElement<String> implements LookupParser {

	private static final int[] RELIGION_VALUES = { 111, 121, 122, 211, 711, 811 };

	public ReligionFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public ReligionFormElement(String key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected Search<String> getSearch(PropertyInterface property) {
		return new ReligionSearch();
	}

	public static class ReligionSearch implements Search<String> {

		@Override
		public List<String> search(String query) {
			List<String> result = new ArrayList<>();
			for (int i : RELIGION_VALUES) {
				String text = Resources.getString("Religion._" + i);
				if (StringUtils.isEmpty(query) || text.startsWith(query)) {
					result.add(text);
				}
			}
			return result;
		}
	}

	@Override
	protected String getAllowedCharacters(PropertyInterface property) {
		return null;
	}

	@Override
	protected int getAllowedSize(PropertyInterface property) {
		int max = 10;
		for (int i : RELIGION_VALUES) {
			String text = Resources.getString("Religion._" + i);
			max = Math.max(max, text.length());
		}
		return max;
	}

	@Override
	public void mock() {
		setValue(String.valueOf(RELIGION_VALUES[new Random().nextInt(RELIGION_VALUES.length)]));
	}

	@Override
	protected String render(String value) {
		return renderReligion(value);
	}

	// reused in ReligionDataFormElement
	static String renderReligion(String value) {
		if (value != null) {
			String resourceName = "Religion._" + value;
			if (Resources.isAvailable(resourceName)) {
				return Resources.getString(resourceName);
			} else {
				return "code [" + value + "]";
			}
		} else {
			return null;
		}
	}

	@Override
	public String parse(String s) {
		return parseReligion(s);
	}

	// reused in ReligionDataFormElement
	static String parseReligion(String s) {
		int index = s.indexOf("[");
		int endIndex = s.indexOf(']', index);
		if (index > 0 && endIndex > index) {
			return s.substring(index + 1, endIndex);
		} else {
			for (int i : RELIGION_VALUES) {
				String text = Resources.getString("Religion._" + i);
				if (s.equals(text)) {
					return String.valueOf(i);
				}
			}
			return InvalidValues.createInvalidString(s);
		}
	}
}
