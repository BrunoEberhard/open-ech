package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ConstructionProjectInformation {
	public static final ConstructionProjectInformation $ = Keys.of(ConstructionProjectInformation.class);

	@NotEmpty
	public ch.ech.ech0129.ConstructionProject constructionProject;
	public ch.ech.ech0007.SwissMunicipality municipality;
	public ch.ech.ech0071.CantonAbbreviation canton;

	public enum Confederation { CH;	}
	public Confederation confederation;
}