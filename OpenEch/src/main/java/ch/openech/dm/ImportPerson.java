package ch.openech.dm;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Blob;

public class ImportPerson {
	public static final ImportPerson IMPORT_PERSON = Constants.of(ImportPerson.class);
	
	@Blob
	public String data;
	
}
