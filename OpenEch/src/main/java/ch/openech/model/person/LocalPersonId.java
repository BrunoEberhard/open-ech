package  ch.openech.model.person;

import java.util.UUID;

import org.minimalj.model.annotation.Size;

import  ch.openech.model.EchFormats;

public class LocalPersonId {

	// Wird von der Persistence verwaltet
	public Object id;

	@Size(EchFormats.baseName)
	public String laber = UUID.randomUUID().toString().substring(0, 8);
}
