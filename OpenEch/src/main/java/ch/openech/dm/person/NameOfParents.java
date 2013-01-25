package ch.openech.dm.person;

import ch.openech.mj.model.Constants;

/**
 * 
 * Ein furchtbares Konstrukt, das leider gebraucht wird, um auf die Namen der
 * Eltern insgesamt zuzugreiffen wie beim PersonPanel, andererseits auf die Namen
 * von Vater/Mutter einzeln.
 */
public class NameOfParents {
	public static final NameOfParents KEYS = Constants.of(NameOfParents.class);

	public final NameOfParent father = new NameOfParent();
	public final NameOfParent mother = new NameOfParent();
	
}
