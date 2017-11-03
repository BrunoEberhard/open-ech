package ch.openech.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class SendingApplication {
	public static final SendingApplication $ = Keys.of(SendingApplication.class);
	
	@Size(30) @NotEmpty
	public String manufacturer;
	@Size(30) @NotEmpty
	public String product;
	@Size(30) @NotEmpty
	public String productVersion;
	
}
