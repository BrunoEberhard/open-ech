package ch.ech.ech0213commons;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.213")
public class Warning {
	public static final Warning $ = Keys.of(Warning.class);

	@NotEmpty
	public Integer code;
	@Size(2)
	public String descriptionLanguage;
	@Size(300)
	public String codeDescription;
	@Size(5000)
	public String comment;
}