package ch.openech;

import java.util.List;

import org.minimalj.application.Application;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.page.PageAction;

import ch.ech.ech0011.v8.Person;
import ch.ech.ech0071.v1.Canton;
import ch.ech.ech0071.v1.Municipality;
import ch.ech.ech0072.v1.Country;
import ch.openech.frontend.ech0007.CantonTablePage;
import ch.openech.frontend.ech0007.MunicipalityTablePage;
import ch.openech.frontend.ech0020.EventMoveInEditor;
import ch.openech.frontend.org.ImportSwissDataAction;

public class OpenEch extends Application {

	@Override
	public List<Action> getNavigation() {
		ActionGroup actions = new ActionGroup("");
		actions.add(new EventMoveInEditor());
		actions.add(new PageAction(new MunicipalityTablePage()));
		actions.add(new PageAction(new CantonTablePage()));
		actions.add(new ImportSwissDataAction());
		return actions.getItems();
	}
	
	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[] { Person.class, Country.class, Canton.class, Municipality.class };
	}
	
//	public static void main(String[] args) {
//		Swing.start(new OpenEch());
//	}

}
