package ch.openech.dm.code;

import ch.openech.mj.db.model.annotation.Varchar;

public class CodeWithOther {

	@Varchar(1)
	public String code;
	
	@Varchar(255)
	public String other;
	
}
