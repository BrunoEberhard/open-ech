package ch.openech.model.estate;

import java.util.List;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;

public class Locality implements Code, Validation, Rendering {
	public static final Locality $ = Keys.of(Locality.class);
	
	@Size(6)
	public Integer id;
	
	public final LocalityName name = new LocalityName();
	
	//
	
	@Size(4) @NotEmpty
	public transient Integer zip;
	@Size(2) @NotEmpty
	public transient Integer zipAddOn = 0;

	//
	
	public String getSwissZipCode() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "swissZipCode");
		return String.valueOf(id / 100);
	}

	public String getSwissZipCodeAddOn() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "swissZipCodeAddOn");
		return StringUtils.padLeft(String.valueOf(id % 100), 2, '0');
	}
	
	public Integer getNewId() {
		if (zip != null && !InvalidValues.isInvalid(zip) && zipAddOn != null && !InvalidValues.isInvalid(zipAddOn)) {
			return zip * 100 + zipAddOn;
		} else {
			return null;
		}
	}
	
	@Override
	public List<ValidationMessage> validate() {
		if (zip != null && !InvalidValues.isInvalid(zip) && (zip < 1000 || zip > 9999)) {
			return Validation.message($.zip, "Bereich 1000 bis 9999");
		}
		return null;
	}
	
	@Override
	public String render(RenderType renderType) {
		return getSwissZipCode() + " " + name.nameLong;
	}
	
	public static final class LocalityName {
		@Size(EchFormats.language) @NotEmpty
		public String language = "de";
		@Size(40) @NotEmpty
		public String nameLong;
		@Size(18)
		public String nameShort;
	}
	
	/*
	 	<xs:sequence>
			<xs:element name="swissZipCode" type="eCH-0010:swissZipCodeType" minOccurs="0"/>
			<xs:element name="swissZipCodeAddOn" type="eCH-0010:swissZipCodeAddOnType" minOccurs="0"/>
			<xs:element name="name" type="eCH-0129:localityNameType"/>
		</xs:sequence>
		
		// TODO
	<xs:simpleType name="localisationLanguageType">
		<xs:restriction base="xs:nonNegativeInteger">
			<xs:enumeration value="9901"/>
			<xs:enumeration value="9902"/>
			<xs:enumeration value="9903"/>
			<xs:enumeration value="9904"/>
		</xs:restriction>
	</xs:simpleType>
		
	 */
}
