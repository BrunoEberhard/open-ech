package ch.openech;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.minimalj.application.Application;
import org.minimalj.application.Configuration;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.impl.nanoserver.NanoWebServer;
import org.minimalj.util.resources.MultiResourceBundle;

import ch.openech.frontend.e07.CantonTablePage;
import ch.openech.frontend.e07.MunicipalityTablePage;
import ch.openech.frontend.e08.CountryTablePage;
import ch.openech.frontend.estate.BuildingTablePage;
import ch.openech.frontend.estate.DwellingTablePage;
import ch.openech.frontend.estate.LocalityTablePage;
import ch.openech.frontend.estate.PlanningPermissionApplicationTablePage;
import ch.openech.frontend.estate.RealestateTablePage;
import ch.openech.frontend.estate.StreetTablePage;
import ch.openech.frontend.estate.ZoneTablePage;
import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;
import ch.openech.transaction.ImportSwissDataTransaction;

public class EstateApplication extends Application {

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MultiResourceBundle(locale, "ch.openech.resources.OpenEch", "ch.openech.model.estate.0129",
				"ch.openech.model.estate.0211", "ch.openech.resources.Estate");
	}

	@Override
	public List<Action> getNavigation() {
		ActionGroup navigation = new ActionGroup(null);
		ActionGroup estate = navigation.addGroup("Objekte");
		estate.add(new RealestateTablePage());
		estate.add(new BuildingTablePage());
		estate.add(new DwellingTablePage());

		ActionGroup application = navigation.addGroup("Gesuche");
		application.add(new PlanningPermissionApplicationTablePage());

		ActionGroup ortsdaten = navigation.addGroup("Ortsdaten");
		ortsdaten.add(new CountryTablePage());
		ortsdaten.add(new CantonTablePage());
		ortsdaten.add(new MunicipalityTablePage());
		ortsdaten.add(new LocalityTablePage());
		ortsdaten.add(new StreetTablePage());
		ortsdaten.add(new ZoneTablePage());

		return navigation.getItems();
	}

	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[] { PlanningPermissionApplication.class, Person.class, Organisation.class };
	}

	public static void main(String[] args) {
		Configuration.set("MjInit", ImportSwissDataTransaction.class.getName());
		NanoWebServer.start(new EstateApplication());
		// Swing.start(new EstateApplication());
	}

}
