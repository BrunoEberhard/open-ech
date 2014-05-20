package  ch.openech.model;

import org.joda.time.LocalDateTime;
import org.minimalj.model.annotation.Size;

public class Event {

	public LocalDateTime time;
	@Size(255)
	public String type;
	// @Blob
	@Size(255)
	public String message;
}
