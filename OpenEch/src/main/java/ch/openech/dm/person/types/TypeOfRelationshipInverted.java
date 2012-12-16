package ch.openech.dm.person.types;


/**
 * Ist kein Code von eCH, dort wird nur von Kind zu Eltern verwiesen. Beim Zuzug wird
 * jedoch ein Code benötigt, der angibt, wie die nächste Person zu den letzen Personen
 * verbunden ist.
 * 
 * @author Bruno
 *
 */
public enum TypeOfRelationshipInverted {
	
	KeineBeziehung, 
	Kind, 
	Partner, 
	AllianzPartner;
	
}
