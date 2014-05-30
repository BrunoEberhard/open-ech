package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.frontend.edit.fields.FormField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.GenericUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.e07.MunicipalityField;
import ch.openech.frontend.e07.MunicipalityReadOnlyField;
import ch.openech.frontend.e08.CountryField;
import ch.openech.frontend.e08.CountryReadOnlyField;
import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.e10.HouseNumberField;
import ch.openech.frontend.e101.PersonExtendedInformationField;
import ch.openech.frontend.e11.ContactPersonField;
import ch.openech.frontend.e11.DwellingAddressField;
import ch.openech.frontend.e11.ForeignField;
import ch.openech.frontend.e11.NationalityField;
import ch.openech.frontend.e11.NationalityReadOnlyField;
import ch.openech.frontend.e11.PlaceField;
import ch.openech.frontend.e11.PlaceOfOriginField;
import ch.openech.frontend.e11.PlaceReadOnlyField;
import ch.openech.frontend.e11.ResidenceField;
import ch.openech.frontend.e21.OccupationField;
import ch.openech.frontend.e44.PersonIdentificationField;
import ch.openech.frontend.e44.VnField;
import ch.openech.frontend.e46.ContactField;
import ch.openech.frontend.e97.UidStructureField;
import ch.openech.model.common.Address;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.HouseNumber;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.organisation.UidStructure;
import ch.openech.model.person.ContactPerson;
import ch.openech.model.person.Foreign;
import ch.openech.model.person.Nationality;
import ch.openech.model.person.Occupation;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonExtendedInformation;
import ch.openech.model.person.PersonIdentification;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.Residence;
import ch.openech.model.person.types.Vn;
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
		if (type == DatePartiallyKnown.class) {
			return new DatePartiallyKnownField(property, editable);
		} else if (type == PersonIdentification.class) {
			return new PersonIdentificationField(property);
		} else if (type == Person.class) {
			return new ch.openech.frontend.e44.PersonField(property);
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
		} else if (type == HouseNumber.class) {
			return new HouseNumberField(property);
		} else if (type == CountryIdentification.class) {
			return editable ? new CountryField(property) : new CountryReadOnlyField(property);
		} else if (type == MunicipalityIdentification.class) {
			return editable ? new MunicipalityField(property, false) : new MunicipalityReadOnlyField(property);
		} else if (type == Vn.class) {
			return new VnField(property, editable);			
		} else if (type == UidStructure.class) {
			return new UidStructureField(property, editable);				
		} else if (type == List.class) {
			Class<?> listClass = GenericUtils.getGenericClass(property.getType());
			if (listClass == Occupation.class) {
				return new OccupationField(property, echSchema, editable);
			} else if (listClass == PlaceOfOrigin.class) {
				return new PlaceOfOriginField(property, editable);
			} else if (listClass == ContactEntry.class) {
				return new ContactField(property, editable);
			}
		}
		
		return super.createField(property);
	}

}
