package ch.openech;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.minimalj.application.Application;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.impl.swing.Swing;
import org.minimalj.util.resources.MultiResourceBundle;

import ch.openech.frontend.estate.BuildingEditor;
import ch.openech.model.estate.Building;
import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;

public class EstateApplication extends Application {

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MultiResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch", locale),
				ResourceBundle.getBundle("ch.openech.model.estate.0129", locale));
	}

	@Override
	public List<Action> getNavigation() {
		ActionGroup navigation = new ActionGroup(null);
		navigation.add(new BuildingEditor());
		return navigation.getItems();
	}
	
	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[] { Building.class, Person.class, Organisation.class, PlanningPermissionApplication.class };
	}

	public static void main(String[] args) {
		Swing.start(new EstateApplication());
	}

}
