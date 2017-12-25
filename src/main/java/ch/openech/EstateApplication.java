package ch.openech;

import java.util.Locale;
import java.util.ResourceBundle;

import org.minimalj.application.Application;
import org.minimalj.frontend.impl.swing.Swing;
import org.minimalj.util.resources.MultiResourceBundle;

import ch.openech.model.estate.Building;
import ch.openech.model.estate.PlanningPermissionApplication;

public class EstateApplication extends Application {

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MultiResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch", locale),
				ResourceBundle.getBundle("ch.openech.model.estate.0129", locale));
	}

	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[] { Building.class, PlanningPermissionApplication.class };
	}

	public static void main(String[] args) {
		Swing.start(new EstateApplication());
	}

}
