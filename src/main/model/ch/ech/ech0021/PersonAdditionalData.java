package ch.ech.ech0021;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PersonAdditionalData {
	public static final PersonAdditionalData $ = Keys.of(PersonAdditionalData.class);

	public ch.ech.ech0010.MrMrs mrMrs;
	@Size(50)
	public String title;
	@Size(2)
	public String languageOfCorrespondance;
}