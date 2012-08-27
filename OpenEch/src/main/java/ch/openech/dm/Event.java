package ch.openech.dm;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.String255;
import ch.openech.mj.db.model.annotation.Is;

public class Event {

	@Is(Date)
	public String time;
	@Is(String255)
	public String type;
	// @Blob
	@Is(String255)
	public String message;
}
