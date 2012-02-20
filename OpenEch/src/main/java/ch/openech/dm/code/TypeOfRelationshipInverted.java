package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;
import ch.openech.mj.util.StringUtils;

/**
 * Ist kein Code von eCH, dort wird nur von Kind zu Eltern verwiesen. Beim Zuzug wird
 * jedoch ein Code benötigt, der angibt, wie die nächste Person zu den letzen Personen
 * verbunden ist.
 * 
 * @author Bruno
 *
 */
public enum TypeOfRelationshipInverted implements CodeValue {

	KeineBeziehung(0), Kind(1), Partner(2), AllianzPartner(3);
	
	private final String value;
	
	private TypeOfRelationshipInverted(int value) {
		this.value = Integer.toString(value);
	}
	
	@Override
	public String getKey() {
		return value;
	}

	public static TypeOfRelationshipInverted lookup(String key) {
		for (TypeOfRelationshipInverted t : values()) {
			if (StringUtils.equals(t.getKey(), key)) {
				return t;
			}
		}
		return null;
	}
}
