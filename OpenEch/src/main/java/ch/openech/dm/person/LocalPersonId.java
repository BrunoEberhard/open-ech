package ch.openech.dm.person;

import java.util.UUID;

import ch.openech.dm.EchFormats;
import ch.openech.mj.model.annotation.Size;

public class LocalPersonId {

	// Wird von der Persistence verwaltet
	public int id;

	@Size(EchFormats.baseName)
	public String laber = UUID.randomUUID().toString().substring(0, 8);
}
