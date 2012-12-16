package ch.openech.dm.person;

// TODO Display könnte auch durch "null" ersetzt werden
public enum PersonEditMode {
	DISPLAY, BASE_DELIVERY, MOVE_IN, BIRTH, CHANGE_RESIDENCE_TYPE, CORRECT_PERSON, CORRECT_IDENTIFICATION, CORRECT_NAME;
	
	public boolean isIdentificationVisible() {
		return this != CHANGE_RESIDENCE_TYPE && this != CORRECT_NAME;
	}
	
	public boolean isMoveIn() {
		return this == MOVE_IN || this == CHANGE_RESIDENCE_TYPE;
	}
	public boolean isCorrectName() {
		return this == CORRECT_NAME;
	}
	
	public boolean isEditable() {
		return this != DISPLAY;
	}


}
