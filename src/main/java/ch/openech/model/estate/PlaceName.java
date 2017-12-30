package ch.openech.model.estate;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.model.types.EchCode;

public class PlaceName {

	@NotEmpty
	public PlaceNameType placeNameType;
	@Size(255) // in eCH nicht definiert
	public String localGeographicalName;
	
	public static enum PlaceNameType implements EchCode {
		_0, _1, _2, _3;
		
		@Override
		public String getValue() {
			return String.valueOf(ordinal() + 1);
		}
	}
}
