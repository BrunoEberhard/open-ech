package ch.openech.client.ewk;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.ewk.event.ExportAllAction;
import ch.openech.client.ewk.event.KeyDeliveryAction;
import ch.openech.client.ewk.event.moveIn.MoveInWizard;
import ch.openech.datagenerator.GeneratePersonAction;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.application.WindowConfig;
import ch.openech.mj.edit.EditorPage;
import ch.openech.mj.edit.EditorPageAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageAction;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class WindowConfigEwk implements WindowConfig {

	private final EchNamespaceContext echNamespaceContext;
	
	public WindowConfigEwk (EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
	}
	
	@Override
	public String getTitle() {
		return "Open-eCH Einwohnerkontrolle";
	}
	
	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		ActionGroup create = actionGroup.getOrCreateActionGroup(ActionGroup.NEW);
		create.add(new EditorPageAction(MoveInWizard.class, echNamespaceContext.getVersion()));
		create.add(new PageAction(BirthPage.class, echNamespaceContext.getVersion()));
		
		ActionGroup admin = actionGroup.getOrCreateActionGroup("admin");
		ActionGroup export = admin.getOrCreateActionGroup("export");
		export.add(new ExportAllAction(echNamespaceContext));
		if (echNamespaceContext.keyDeliveryPossible()) {
			export.add(new KeyDeliveryAction(echNamespaceContext));
		}
		ActionGroup imprt = admin.getOrCreateActionGroup("imprt");
		imprt.add(new EditorPageAction(BaseDeliveryEditor.class, echNamespaceContext.getVersion()));
		// TODO updateBaseDeliveryPageMenuVisibility();
		imprt.add(new GeneratePersonAction(echNamespaceContext));
		// TODO updateGenerateDataVisibility();
	}
	
//	private void updateBaseDeliveryPageMenuVisibility() {
//		boolean visible = preferences.getBoolean("menuBaseDelivery", false);
//		menuItemBaseDeliveryPage.setVisible(visible);
//	}
//
//	private void updateGenerateDataVisibility() {
//		boolean visible = preferences.getBoolean("generateData", true);
//		menuItemGenerateData.setVisible(visible);
//	}
	
	@Override
	public Class<?>[] getSearchClasses() {
		return new Class<?>[]{SearchPersonPage.class};
	}
	
	public static class BirthPage extends EditorPage {

		public BirthPage(PageContext context, String version) {
			super(context, new BirthCreator(context, EchNamespaceContext.getNamespaceContext(20, version)));
		}

		@Override
		public void setPreviousPage(Page page) {
			if (page instanceof PersonViewPage) {
				PersonViewPage personPage = (PersonViewPage) page;
				getFormVisual().setObject(calculatePresets(personPage.getObject()));
			} 
		}
		
		private static Person calculatePresets(Person parentPerson) {
			Person person = new Person();

			Person mother = null;
			Person father = null;

			if (parentPerson.isMale())
				father = parentPerson;
			else
				mother = parentPerson;
			
			Relation partnerRelation = parentPerson.getPartner();
			if (partnerRelation != null && partnerRelation.partner != null && partnerRelation.partner.vn != null) { 
				Person partnerOfVisiblePerson = EchServer.getInstance().getPersistence().person().getByVn(partnerRelation.partner.vn);
				if (partnerOfVisiblePerson != null) {
					if (partnerOfVisiblePerson.isMale())
						father = partnerOfVisiblePerson;
					else
						mother = partnerOfVisiblePerson;
				}
			}

			if (mother != null) {
				addRelation(person, mother);
			}

			if (father != null) {
				addRelation(person, father);
			}

			presetOfficialName(person, father, mother);
			presetReligion(person, father, mother);
			presetNationality(person, father, mother);
			presetPlaceOfOrigin(person, father, mother);
			// presetContact(person, father, mother);

			PreferenceData preferenceData = PreferenceData.load();
			person.placeOfBirth.setMunicipalityIdentification(preferenceData.preferencesDefaultsData.residence);
			return person;
		}

		private static void addRelation(Person person, Person parent) {
			Relation relation = new Relation();
			relation.partner = parent.personIdentification;
			if (parent.isMale()) {
				relation.typeOfRelationship = "4";
			} else if (parent.isFemale()) {
				relation.typeOfRelationship = "3";
			}
			relation.care = "1";
			person.relation.add(relation);
		}

		@BusinessRule("Bei Geburt wird offizieller Name von Vater übernommen, wenn nicht vorhanden von Mutter")
		private static void presetOfficialName(Person person, Person father, Person mother) {
			if (father != null) {
				person.personIdentification.officialName = father.personIdentification.officialName;
			} else if (mother != null) {
				person.personIdentification.officialName = mother.personIdentification.officialName;
			}
		}

		// @BusinessRule("Bei Geburt wird Kontakt von Mutter übernommen, wenn Mutter unbekannt vom Vater")
		// private void presetContact(Person person, Person father, Person mother) {
		// if (mother != null) {
		// person.dwellingAddress = mother.dwellingAddress;
		// person.contactPerson = mother.contactPerson;
		// person.contactPerson.address = mother.contactAddress;
		// } else if (father != null) {
		// person.dwellingAddress = father.dwellingAddress;
		// person.contactPerson = father.contactPerson;
		// person.contactPerson.address = father.contactAddress;
		// }
		// }

		@BusinessRule("Bei Geburt wird Religion von Vater übernommen, wenn nicht vorhanden von Mutter")
		private static void presetReligion(Person person, Person father, Person mother) {
			if (father != null && !StringUtils.isEmpty(father.religion)) {
				person.religion = father.religion;
			} else if (mother != null && !StringUtils.isEmpty(mother.religion)) {
				person.religion = mother.religion;
			}
		}

		@BusinessRule("Bei Geburt wird Staatsangehörigkeit von Vater übernommen, wenn nicht vorhanden von Mutter")
		private static void presetNationality(Person person, Person father, Person mother) {
			if (father != null && "2".equals(father.nationality.nationalityStatus)) {
				father.nationality.copyTo(person.nationality);
			} else if (mother != null && "2".equals(mother.nationality.nationalityStatus)) {
				mother.nationality.copyTo(person.nationality);
			}
		}

		@BusinessRule("Bei Geburt wird Heimatort von Vater übernommen, wenn nicht vorhanden oder ausländisch von Mutter")
		private static void presetPlaceOfOrigin(Person person, Person father, Person mother) {
			if (father != null && father.isSwiss()) {
				person.placeOfOrigin.addAll(convertToAbstammung(father.placeOfOrigin));
			} else if (mother != null && mother.isSwiss()) {
				person.placeOfOrigin.addAll(convertToAbstammung(mother.placeOfOrigin));
			}
		}

		private static List<PlaceOfOrigin> convertToAbstammung(List<PlaceOfOrigin> placeOfOrigins) {
			List<PlaceOfOrigin> result = new ArrayList<PlaceOfOrigin>();
			for (PlaceOfOrigin placeOfOrigin : placeOfOrigins) {
				// Abstammung und naturalizationDate = dateOfBirth wird später auf
				// dem Server gesetzt. Das XML lässt eine übertragung das
				// placeOfOriginAddOn
				// auch gar nicht zu
				PlaceOfOrigin newPlaceOfOrigin = new PlaceOfOrigin();
				newPlaceOfOrigin.originName = placeOfOrigin.originName;
				newPlaceOfOrigin.canton = placeOfOrigin.canton;
				result.add(newPlaceOfOrigin);
			}
			return result;
		}
		
	}
		
}
