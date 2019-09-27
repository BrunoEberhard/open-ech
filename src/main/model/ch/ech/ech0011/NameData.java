package ch.ech.ech0011;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;

// handmade: added search annotations
public class NameData {
	public static final NameData $ = Keys.of(NameData.class);

	@NotEmpty
	@Size(100)
	@Searched
	public String officialName, firstName;
	@Size(100)
	public String originalName;
	@Size(100)
	public String allianceName;
	@Size(100)
	public String aliasName;
	@Size(100)
	public String otherName;
	@Size(100)
	public String callName;
	public ForeignerName nameOnForeignPassport;
	public ForeignerName declaredForeignName;
}