package ch.openech;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.minimalj.application.Application;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.util.resources.MultiResourceBundle;

import ch.ech.ech0011.Person;
import ch.ech.ech0071.Canton;
import ch.ech.ech0071.Municipality;
import ch.ech.ech0072.CountryInformation;
import ch.ech.ech0098.OrganisationRoot;
import ch.ech.ech0129.Building;
import ch.ech.ech0129.Dwelling;
import ch.ech.ech0211.v1.PlanningPermissionApplication;
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
		return new Class<?>[] { Person.class, CountryInformation.class, Canton.class, Municipality.class, OrganisationRoot.class, Building.class, Dwelling.class, PlanningPermissionApplication.class };
	}
	
	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MultiResourceBundle(locale, /* this.getClass().getName(), */ "ch.openech.Model");
	}
	
//	public static void main(String[] args) {
//		Swing.start(new OpenEch());
//	}

}
