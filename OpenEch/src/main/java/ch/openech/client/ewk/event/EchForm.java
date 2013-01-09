package ch.openech.client.ewk.event;

import java.util.List;

import ch.openech.client.e07.MunicipalityField;
import ch.openech.client.e07.MunicipalityReadOnlyField;
import ch.openech.client.e08.CountryField;
import ch.openech.client.e08.CountryReadOnlyField;
import ch.openech.client.e10.AddressField;
import ch.openech.client.e10.HouseNumberField;
import ch.openech.client.e101.PersonExtendedInformationField;
import ch.openech.client.e11.ContactPersonField;
import ch.openech.client.e11.DwellingAddressField;
import ch.openech.client.e11.ForeignField;
import ch.openech.client.e11.NationalityField;
import ch.openech.client.e11.NationalityReadOnlyField;
import ch.openech.client.e11.PlaceField;
import ch.openech.client.e11.PlaceOfOriginField;
import ch.openech.client.e11.PlaceReadOnlyField;
import ch.openech.client.e11.ResidenceField;
import ch.openech.client.e21.OccupationField;
import ch.openech.client.e44.PersonIdentificationField;
import ch.openech.client.e46.ContactField;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.HouseNumber;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.contact.Contact;
import ch.openech.dm.person.ContactPerson;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Residence;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.util.GenericUtils;
import ch.openech.xml.write.EchSchema;

public class EchForm<T> extends Form<T> {
	protected final EchSchema echSchema;
	
	public EchForm() {
		this(1);
	}
	
	public EchForm(int columns) {
		this((EchSchema) null, columns);
	}

	protected EchForm(EchSchema echSchema) {
		this(echSchema, 1);
	}
	
	public EchForm(EchSchema echSchema, int columns) {
		super(Resources.getResourceBundle(), true, columns);
		this.echSchema = echSchema;
	}

	protected EchForm(EchSchema echSchema, boolean editable, int columns) {
		super(Resources.getResourceBundle(), editable, columns);
		this.echSchema = echSchema;
	}
	
	//

	@Override
	public FormField<?> createField(PropertyInterface property) {
		Class<?> type = property.getFieldClazz();
		if (type == PersonIdentification.class) {
			return new PersonIdentificationField(property);
		} else if (type == Person.class) {
			return new ch.openech.client.e44.PersonField(property);
		} else if (type == Nationality.class) {
			return editable ? new NationalityField(property) : new NationalityReadOnlyField(property);
		} else if (type == ContactPerson.class) {
			return new ContactPersonField(property, echSchema, editable);
		} else if (type == Foreign.class) {
			return new ForeignField(property, echSchema, editable);
		} else if (type == Residence.class) {
			return new ResidenceField(property, editable);
		} else if (type == Place.class) {
			return editable ? new PlaceField(property) : new PlaceReadOnlyField(property);
		} else if (type == Address.class) {
			return new AddressField(property, editable);
		} else if (type == DwellingAddress.class) {
			return new DwellingAddressField(property, echSchema, editable);
		} else if (type == PersonExtendedInformation.class) {
			return new PersonExtendedInformationField(property, editable);
		} else if (type == Contact.class) {
			return new ContactField(property, editable);
		} else if (type == HouseNumber.class) {
			return new HouseNumberField(property);
		} else if (type == CountryIdentification.class) {
			return editable ? new CountryField(property) : new CountryReadOnlyField(property);
		} else if (type == MunicipalityIdentification.class) {
			return editable ? new MunicipalityField(property, false) : new MunicipalityReadOnlyField(property);
		} else if (type == List.class) {
			Class<?> listClass = GenericUtils.getGenericClass(property.getType());
			if (listClass == Occupation.class) {
				return new OccupationField(property, echSchema, editable);
//				} else if (listClass == Relation.class) {
//					return new RelationField(key, editable);
			} else if (listClass == PlaceOfOrigin.class) {
				return new PlaceOfOriginField(property, editable);
			}
		}
		
		return super.createField(property);
	}

}
