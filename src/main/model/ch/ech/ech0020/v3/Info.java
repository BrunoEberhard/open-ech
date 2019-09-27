package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.249")
public class Info {
	public static final Info $ = Keys.of(Info.class);

	@Size(250)
	public String code;
	@Size(255) // unknown
	public String textEnglish;
	@Size(255) // unknown
	public String textGerman;
	@Size(255) // unknown
	public String textFrench;
	@Size(255) // unknown
	public String textItalian;
}