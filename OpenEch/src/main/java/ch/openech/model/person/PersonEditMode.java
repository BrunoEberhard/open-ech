package  ch.openech.model.person;

// TODO Display könnte auch durch "null" ersetzt werden
public enum PersonEditMode {
	DISPLAY, BASE_DELIVERY, MOVE_IN, BIRTH, CHANGE_RESIDENCE_TYPE, CORRECT_PERSON, CORRECT_IDENTIFICATION;
	
	public boolean isIdentificationVisible() {
		return this != CHANGE_RESIDENCE_TYPE;
	}
	
	public boolean isMoveIn() {
		return this == MOVE_IN || this == CHANGE_RESIDENCE_TYPE;
	}

	public boolean isEditable() {
		return this != DISPLAY;
	}


}
