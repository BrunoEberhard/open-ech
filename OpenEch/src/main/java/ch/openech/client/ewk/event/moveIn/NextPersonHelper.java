package ch.openech.client.ewk.event.moveIn;

import ch.openech.client.ewk.event.moveIn.MoveInWizard.MoveInNextPerson;
import ch.openech.dm.code.TypeOfRelationship;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.util.BusinessRule;

public class NextPersonHelper {

	@BusinessRule("Beim Zuzug werden bei Eingabe einer weiteren Person Daten von den früheren Personen übernommen (Partner)")
	public static Person createNextPersonPartner(Person partner, boolean married) {
		Person nextPerson = new Person();
		Relation relation = new Relation();
		Relation relation2 = new Relation();
	
		if (married) {
			nextPerson.personIdentification.officialName = partner.personIdentification.officialName;
			boolean partnerIsMale = "1".equals(partner.personIdentification.sex);
			nextPerson.personIdentification.sex = partnerIsMale ? "2" : "1";
			relation.typeOfRelationship = relation2.typeOfRelationship = "1";
		} else {
			nextPerson.alliancePartnershipName = partner.alliancePartnershipName;
			nextPerson.personIdentification.sex = partner.personIdentification.sex;
			relation.typeOfRelationship = relation2.typeOfRelationship = "2";
		}
		nextPerson.maritalStatus.maritalStatus = partner.maritalStatus.maritalStatus;
		nextPerson.religion = partner.religion;
		nextPerson.typeOfResidence = partner.typeOfResidence;
		nextPerson.residence.reportingMunicipality = partner.residence.reportingMunicipality;
		nextPerson.residence.setSecondary(partner.residence.secondary);
		nextPerson.arrivalDate = partner.arrivalDate;
		nextPerson.comesFrom = partner.comesFrom;
		nextPerson.dwellingAddress = partner.dwellingAddress;
	
		relation.partner = nextPerson.personIdentification;
		partner.relation.add(relation);
	
		relation2.partner = partner.personIdentification;
		nextPerson.relation.add(relation2);
		return nextPerson;
	}

	@BusinessRule("Beim Zuzug werden bei Eingabe einer weiteren Person Daten von den früheren Personen übernommen (Kind)")
	public static Person createNextPersonChild(MoveInNextPerson moveInNextPerson) {
		Person child = new Person();
		Person basePerson = moveInNextPerson.basePerson;

		child.personIdentification.officialName = basePerson.personIdentification.officialName;
		child.religion = basePerson.religion;
		child.typeOfResidence = basePerson.typeOfResidence;
		child.residence.reportingMunicipality = basePerson.residence.reportingMunicipality;
		child.residence.setSecondary(basePerson.residence.secondary);
		child.arrivalDate = basePerson.arrivalDate;
		child.comesFrom = basePerson.comesFrom;
		child.dwellingAddress = basePerson.dwellingAddress;

		if (moveInNextPerson.mother != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Mutter, moveInNextPerson.mother));
		}
		if (moveInNextPerson.father != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Vater, moveInNextPerson.father));
		}
		if (moveInNextPerson.fosterMother != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Pflegemutter, moveInNextPerson.fosterMother));
		}
		if (moveInNextPerson.fosterFather != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Pflegevater, moveInNextPerson.fosterFather));
		}
		
		return child;
	}

	public static Relation createChildRelation(TypeOfRelationship typeOfRelationship, Person person) {
		Relation relation = new Relation();
		relation.typeOfRelationship = typeOfRelationship.getKey();
		relation.partner = person.personIdentification;
		return relation;
	}
	
	@BusinessRule("Beim Zuzug werden bei Eingabe einer weiteren Person Daten von den früheren Personen übernommen (Person ohne spezielle Beziehung)")
	public static Person createPersonWithoutRelation(Person person) {
		Person nextPerson = new Person();
		nextPerson.religion = person.religion;
		nextPerson.typeOfResidence = person.typeOfResidence;
		nextPerson.residence.reportingMunicipality = person.residence.reportingMunicipality;
		nextPerson.residence.setSecondary(person.residence.secondary);
		nextPerson.arrivalDate = person.arrivalDate;
		nextPerson.comesFrom = person.comesFrom;
		nextPerson.dwellingAddress = person.dwellingAddress;
		return nextPerson;
	}
	
}
