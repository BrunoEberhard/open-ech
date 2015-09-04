package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormElement;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.GenericUtils;

import ch.openech.frontend.e07.CantonFormElement;
import ch.openech.frontend.e07.MunicipalityFormElement;
import ch.openech.frontend.e08.CountryFormElement;
import ch.openech.frontend.e10.AddressFormElement;
import ch.openech.frontend.e10.HouseNumberFormElement;
import ch.openech.frontend.e101.PersonExtendedInformationFormElement;
import ch.openech.frontend.e11.ContactPersonFormElement;
import ch.openech.frontend.e11.DwellingAddressFormElement;
import ch.openech.frontend.e11.ForeignFormElement;
import ch.openech.frontend.e11.NationalityFormElement;
import ch.openech.frontend.e11.PlaceFormElement;
import ch.openech.frontend.e11.PlaceOfOriginFormElement;
import ch.openech.frontend.e11.ResidenceFormElement;
import ch.openech.frontend.e21.OccupationFormElement;
import ch.openech.frontend.e44.PartnerIdentificationFormElement;
import ch.openech.frontend.e44.PersonFormElement;
import ch.openech.frontend.e44.PersonIdentificationFormElement;
import ch.openech.frontend.e44.VnFormElement;
import ch.openech.frontend.e46.ContactFormElement;
import ch.openech.frontend.e97.UidStructureFormElement;
import ch.openech.model.common.Address;
import ch.openech.model.common.Canton;
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
import ch.openech.model.person.PartnerIdentification;
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
		super(true, columns);
		this.echSchema = echSchema;
	}

	protected EchForm(EchSchema echSchema, boolean editable, int columns) {
		super(editable, columns);
		this.echSchema = echSchema;
	}
	
	//

	@Override
	public FormElement<?> createElement(PropertyInterface property) {
		Class<?> type = property.getClazz();
		if (type == DatePartiallyKnown.class) {
			return new DatePartiallyKnownFormElement(property, editable);
		} else if (type == PersonIdentification.class) {
			return new PersonIdentificationFormElement(property);
		} else if (type == PartnerIdentification.class) {
			return new PartnerIdentificationFormElement(property);
		} else if (type == Person.class) {
			return new PersonFormElement(property);
		} else if (type == Nationality.class) {
			return editable ? new NationalityFormElement(property) : new TextFormElement(property);
		} else if (type == Canton.class) {
			return editable ?  new CantonFormElement(property) : new TextFormElement(property);
		} else if (type == ContactPerson.class) {
			return new ContactPersonFormElement(property, echSchema, editable);
		} else if (type == Foreign.class) {
			return new ForeignFormElement(property, echSchema, editable);
		} else if (type == Residence.class) {
			return new ResidenceFormElement(property, editable);
		} else if (type == Place.class) {
			return editable ? new PlaceFormElement(property) : new TextFormElement(property);
		} else if (type == Address.class) {
			return new AddressFormElement(property, editable);
		} else if (type == DwellingAddress.class) {
			return new DwellingAddressFormElement(property, echSchema, editable);
		} else if (type == PersonExtendedInformation.class) {
			return new PersonExtendedInformationFormElement(property, editable);
		} else if (type == HouseNumber.class) {
			return new HouseNumberFormElement(property);
		} else if (type == CountryIdentification.class) {
			return editable ? new CountryFormElement(property) : new TextFormElement(property);
		} else if (type == MunicipalityIdentification.class) {
			return editable ? new MunicipalityFormElement(property, false) : new TextFormElement(property);
		} else if (type == Vn.class) {
			return new VnFormElement(property, editable);			
		} else if (type == UidStructure.class) {
			return new UidStructureFormElement(property, editable);				
		} else if (type == List.class) {
			Class<?> listClass = GenericUtils.getGenericClass(property.getType());
			if (listClass == Occupation.class) {
				return new OccupationFormElement(property, echSchema, editable);
			} else if (listClass == PlaceOfOrigin.class) {
				return new PlaceOfOriginFormElement(property, editable);
			} else if (listClass == ContactEntry.class) {
				return new ContactFormElement(property, editable);
			}
		}
		
		return super.createElement(property);
	}

}
