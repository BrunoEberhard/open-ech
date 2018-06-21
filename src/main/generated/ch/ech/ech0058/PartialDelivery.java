package ch.ech.ech0058;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class PartialDelivery {
	public static final PartialDelivery $ = Keys.of(PartialDelivery.class);

	@NotEmpty
	@Size(50)
	public String uniqueIdDelivery;
	@NotEmpty
	@Size(4)
	public Integer totalNumberOfPackages;
	@NotEmpty
	@Size(4)
	public Integer numberOfActualPackage;

	//
	
	@Size(50)
	public String uniqueIdBusinessCase;

	public String getUniqueIDBusinessCase() {
		return uniqueIdBusinessCase;
	}

	public void setUniqueIDBusinessCase(String uniqueIdBusinessCase) {
		this.uniqueIdBusinessCase = uniqueIdBusinessCase;
	}

}