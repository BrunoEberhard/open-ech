package ch.openech;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.minimalj.application.Application;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.impl.web.WebServer;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.metamodel.page.EntityTablePage;
import org.minimalj.util.resources.MultiResourceBundle;

import ch.ech.ech0011.Person;
import ch.ech.ech0071.Canton;
import ch.ech.ech0071.Municipality;
import ch.ech.ech0072.CountryInformation;
import ch.ech.ech0098.Organisation;
import ch.ech.ech0129.Building;
import ch.ech.ech0129.Dwelling;
import ch.ech.ech0129.Locality;
import ch.ech.ech0211.v1.PlanningPermissionApplication;
import ch.openech.datagenerator.GeneratePersonEditor;
import ch.openech.frontend.AddPersonEditor;
import ch.openech.frontend.ech0007.CantonTablePage;
import ch.openech.frontend.ech0007.MunicipalityTablePage;
import ch.openech.frontend.ech0008.CountryTablePage;
import ch.openech.frontend.ech0229.NetProfitEditor;
import ch.openech.frontend.org.ImportSwissDataAction;
import ch.openech.xml.EchSchemas;

public class OpenEch extends Application {

	@Override
	public List<Action> getNavigation() {
		ActionGroup actions = new ActionGroup("");
		actions.add(new EntityTablePage());

		actions.add(new AddPersonEditor());
		actions.add(new PageAction(new MunicipalityTablePage()));
		actions.add(new PageAction(new CantonTablePage()));
		actions.add(new PageAction(new CountryTablePage()));
		actions.add(new ImportSwissDataAction());
		actions.add(new GeneratePersonEditor());
		actions.add(new NetProfitEditor());
		return actions.getItems();
	}
	
	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[] { Person.class, CountryInformation.class, Canton.class, Municipality.class, Locality.class, Organisation.class, Building.class,
				Dwelling.class, PlanningPermissionApplication.class };
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MultiResourceBundle(super.getResourceBundle(locale), ResourceBundle.getBundle("ch.openech.Person", locale), ResourceBundle.getBundle("MjModel"));
	}

	public static void main(String[] args) {
		new Thread(() -> new EchSchemas()).start();
		// MjVaadinSpringbootApplication.start(new OpenEch());
		// Swing.start(new OpenEch());
		WebServer.start(new OpenEch());
	}

}
