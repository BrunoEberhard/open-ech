package  ch.openech.model;

import org.minimalj.model.annotation.Size;
import org.threeten.bp.LocalDateTime;

public class Event {

	public Object id;
	
	public LocalDateTime time;
	@Size(255)
	public String type;
	// @Blob
	@Size(255)
	public String message;
}
