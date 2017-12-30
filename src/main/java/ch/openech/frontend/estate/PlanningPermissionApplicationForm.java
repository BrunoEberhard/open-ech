package ch.openech.frontend.estate;

import static ch.openech.model.estate.PlanningPermissionApplication.$;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.e10.ChIso2FormElement;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.common.Address;
import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

public class PlanningPermissionApplicationForm extends EchForm<PlanningPermissionApplication> {

	public PlanningPermissionApplicationForm(boolean editable) {
		super(null, editable, 4);
		addTitle("Identifikation");
		line(new NamedIdFormElement($.localID, editable), new NamedIdFormElement($.otherID, editable));
		addTitle("Beschreibung");
		line(new SuggestionFormElement($.applicationType, applicationTypeList(), editable), new SuggestionFormElement($.proceedingType, proceedingTypeList(), editable));
		
		line($.description);
		line(new PublicationFormElement($.publication, editable));
		line(new RemarkFormElement($.remark, editable));
		line($.municipality);
		
		addTitle("Adresse");
		line($.locationAddress.addressLine1);
		line($.locationAddress.addressLine2);
		line($.locationAddress.street, $.locationAddress.houseNumber);
		line(new ChIso2FormElement($.locationAddress.country), $.locationAddress.zip, $.locationAddress.town);
		// line($.locationAddress.locality); // glaube nicht, dass das Gebiet hier wirklich erwünscht ist
		addDependecy($.locationAddress.zip, new TownUpdater(), $.locationAddress.town);
		// line(new AddressFormElement($.locationAddress, true, false, false));

		addTitle("Grundstückinformation");
		line(new RealestateInformationFormElement($.realestateInformation, editable));
	}

	private List<String> applicationTypeList() {
		// sollte wohl noch lokalisiert werden
		List<String> list = new ArrayList<>();
		list.add("Baugesuch");
		list.add("Bauanfrage");
		list.add("Bauanzeige");
		list.add("Bauermittlung nach PBG");
		list.add("Reklamegesuch");
		return list;
	}
	
	private List<String> proceedingTypeList() {
		List<String> list = new ArrayList<>();
		list.add("Ordentliches Verfahren");
		list.add("Vereinfachtes Verfahren");
		return list;
	}
	
	// Schade dass hier nicht der TownUpdater vom AddressPanel verwendet werden kann
	public static class TownUpdater implements Form.PropertyUpdater<String, String, PlanningPermissionApplication> {

		@Override
		public String update(String input, PlanningPermissionApplication application) {
			Address address = application.locationAddress;
			// Adresse enthält noch den alten Wert. Updater werden aufgerufen, bevor
			// der neue Wert in das Objekt geschrieben wurde. Die folgenden getter
			// würden ohne diese Zuweisung somit die alten Werte lieferen.
			address.zip = input;
			
			String swissZipCode = address.getSwissZipCode();
			String swissZipCodeAddOn = address.getSwissZipCodeAddOn();
			Plz plz = PlzImport.getInstance().getPlz(swissZipCode, swissZipCodeAddOn);
			if (plz != null) {
				return plz.ortsbezeichnung;
			}
			// Keine Änderung, einfach gleichen Wert zurückgeben
			return address.town;
		}
	}


}
