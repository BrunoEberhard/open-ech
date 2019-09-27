package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.784")
public class LocalityName {
	public static final LocalityName $ = Keys.of(LocalityName.class);

	@NotEmpty
	public LocalisationLanguage language;
	@NotEmpty
	@Size(40)
	public String nameLong;
	@Size(18)
	public String nameShort;
}