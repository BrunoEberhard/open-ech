package ch.ech.ech0011;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

// Handmade (rendering)
public class ForeignerName implements Rendering {
	public static final ForeignerName $ = Keys.of(ForeignerName.class);

	@Size(100)
	public String name;
	@Size(100)
	public String firstName;

	@Override
	public String render() {
		if (!StringUtils.isBlank(firstName)) {
			return !StringUtils.isBlank(name) ? firstName + " " + name : firstName;
		} else {
			return name;
		}
	}

}