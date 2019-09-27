package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.925")
public class LocalisationDescription {
	public static final LocalisationDescription $ = Keys.of(LocalisationDescription.class);

	@NotEmpty
	public LocalisationLanguage language;
	@NotEmpty
	@Size(60)
	public String descriptionLong;
	@Size(24)
	public String descriptionShort;
	@Size(3)
	public String descriptionIndex;
}