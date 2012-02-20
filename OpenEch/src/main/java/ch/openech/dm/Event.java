package ch.openech.dm;

import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.Varchar;

public class Event {

	@Date
	public String time;
	@Varchar
	public String type;
	// @Blob
	@Varchar
	public String message;
}
