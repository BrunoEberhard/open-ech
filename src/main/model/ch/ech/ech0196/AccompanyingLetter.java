package ch.ech.ech0196;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class AccompanyingLetter {
	public static final AccompanyingLetter $ = Keys.of(AccompanyingLetter.class);

	@NotEmpty
	@Size(200)
	public String fileName;
	@NotEmpty
	public Integer fileSize;
}