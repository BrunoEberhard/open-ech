package ch.openech.client.ewk.event;

import java.util.List;

import ch.openech.client.e07.CantonField;
import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.client.e07.SwissMunicipalityReadOnlyField;
import ch.openech.client.e08.CountryField;
import ch.openech.client.e08.CountryReadOnlyField;
import ch.openech.client.e10.AddressField;
import ch.openech.client.e10.CountryIso2Field;
import ch.openech.client.e10.HouseNumberField;
import ch.openech.client.e10.TownField;
import ch.openech.client.e10.ZipField;
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
import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.HouseNumber;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.common.Zip;
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
import ch.openech.mj.db.model.AccessorInterface;
import ch.openech.mj.db.model.Format;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.util.GenericUtils;
import ch.openech.xml.write.EchNamespaceContext;

public class EchFormPanel<T> extends Form<T> {
	private final EchNamespaceContext namespaceContext;
	
	protected EchFormPanel() {
		this(1);
	}
	
	public EchFormPanel(int columns) {
		this((EchNamespaceContext) null, columns);
	}

	protected EchFormPanel(EchNamespaceContext namespaceContext) {
		this(namespaceContext, 1);
	}
	
	public EchFormPanel(EchNamespaceContext namespaceContext, int columns) {
		super(null, Resources.getResourceBundle(), true, columns);
		this.namespaceContext = namespaceContext;
	}

	protected EchFormPanel(EchNamespaceContext namespaceContext, boolean editable, int columns) {
		super(null, Resources.getResourceBundle(), editable, columns);
		this.namespaceContext = namespaceContext;
	}
	
	//

	public EchFormPanel(Class<T> objectClass) {
		super(objectClass, Resources.getResourceBundle(), true);
		this.namespaceContext = null;
	}
	
	public EchFormPanel(Class<T> objectClass, int columns) {
		this(objectClass, null, columns);
	}

	public EchFormPanel(Class<T> objectClass, EchNamespaceContext namespaceContext) {
		super(objectClass, Resources.getResourceBundle(), true);
		this.namespaceContext = namespaceContext;
	}
	
	public EchFormPanel(Class<T> objectClass, EchNamespaceContext namespaceContext, int columns) {
		super(objectClass, Resources.getResourceBundle(), true, columns);
		this.namespaceContext = namespaceContext;
	}

	@Override
	public FormField<?> createStringField(String name, Format format) {
		if (format == EchFormats.cantonAbbreviationFormat) {
			return new CantonField(name);
		}
		
		if (namespaceContext != null) {
			if (format == EchCodes.basedOnLaw) {
				if (namespaceContext.reducedBasedOnLawCode()) {
					format = EchCodes.basedOnLaw3;
				}
			} 
			
			if (format == EchCodes.residencePermit) {
				if (namespaceContext.residencePermitDetailed()) {
					format = EchCodes.residencePermitDetailed;
				}
			}
		}
		
		if (Address.ADDRESS.country.equals(name)) {
			return new CountryIso2Field(name);
		} else if (Address.ADDRESS.town.equals(name)) {
			return new TownField(name);
		}
		
		return super.createStringField(name, format);
	}

	@Override
	public FormField<?> createField(String name, AccessorInterface accessor) {
		Class<?> type = accessor.getClazz();
		if (type == PersonIdentification.class) {
			return new PersonIdentificationField(name);
		} else if (type == Person.class) {
			return new ch.openech.client.e44.PersonField(name);
		} else if (type == Nationality.class) {
			return editable ? new NationalityField(name) : new NationalityReadOnlyField(name);
		} else if (type == ContactPerson.class) {
			return new ContactPersonField(name, namespaceContext, editable);
		} else if (type == Foreign.class) {
			return new ForeignField(name, namespaceContext, editable);
		} else if (type == Residence.class) {
			return new ResidenceField(name, editable);
		} else if (type == Place.class) {
			return editable ? new PlaceField(name) : new PlaceReadOnlyField(name);
		} else if (type == Address.class) {
			return new AddressField(name, editable);
		} else if (type == DwellingAddress.class) {
			return new DwellingAddressField(name, namespaceContext, editable);
		} else if (type == PersonExtendedInformation.class) {
			return new PersonExtendedInformationField(name, editable);
		} else if (type == Contact.class) {
			return new ContactField(name, editable);
		} else if (type == HouseNumber.class) {
			return new HouseNumberField(name);
		} else if (type == Zip.class) {
			return new ZipField(name);
		} else if (type == CountryIdentification.class) {
			return editable ? new CountryField(name) : new CountryReadOnlyField(name);
		} else if (type == MunicipalityIdentification.class) {
			return editable ? new SwissMunicipalityField(name, false) : new SwissMunicipalityReadOnlyField(name);
		} else if (type == List.class) {
			Class<?> listClass = GenericUtils.getGenericClass(accessor.getType());
			if (listClass == Occupation.class) {
				return new OccupationField(name, namespaceContext, editable);
//				} else if (listClass == Relation.class) {
//					return new RelationField(name, editable);
			} else if (listClass == PlaceOfOrigin.class) {
				return new PlaceOfOriginField(name, editable);
			}
		}
		
		return super.createField(accessor);
	}

	public EchNamespaceContext getNamespaceContext() {
		return namespaceContext;
	}
}
