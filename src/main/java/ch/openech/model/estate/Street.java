package ch.openech.model.estate;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.NamedId;
import ch.openech.model.types.EchCode;

@Sizes(EchFormats.class)
public class Street {

	public static final Street $ = Keys.of(Street.class);
	
	public Object id;
	
	public String ESID;
	public Boolean isOfficialDescriptionType;
	public String officialStreetNumber;
	public NamedId localID;
	public LocalisationKind localisationKind;
	public List<LocalisationDescription> description;
	public LocalisationStatus status;
	public NumberingPrinciple numberingPrinciple;
	public String ESTRID;
	@Size(255) // undefined in eCH
	public String streetGeometry;
	
	public String getDescriptions() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "descriptions");
		
		String[] descriptions = description.stream().map(d -> d.getTableText()).toArray(String[]::new);
		return String.join(", ", descriptions);
	}
	
	/*
		<xs:sequence>
			<xs:element name="ESID" type="eCH-0129:ESIDType" minOccurs="0"/>
			<xs:element name="isOfficialDescription" type="eCH-0129:isOfficialDescriptionType" minOccurs="0"/>
			<xs:element name="officialStreetNumber" type="eCH-0129:officialStreetNumberType" minOccurs="0"/>
			<xs:element name="localID" type="eCH-0129:namedIdType" minOccurs="0"/>
			<xs:element name="localisationKind" type="eCH-0129:localisationKindType" minOccurs="0"/>
			<xs:element name="description" type="eCH-0129:localisationDescriptionType"/>
			<xs:element name="status" type="eCH-0129:localisationStatusType" minOccurs="0"/>
			<xs:element name="numberingPrinciple" type="eCH-0129:numberingPrincipleType" minOccurs="0"/>
			<xs:element name="ESTRID" type="eCH-0129:ESTRIDType" minOccurs="0"/>
			<xs:element name="streetGeometry" type="xs:anyType" minOccurs="0"/>
		</xs:sequence>
	 */
	
	public static enum LocalisationKind implements EchCode {
		_9801, _9802, _9803, _9809;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	public static final class LocalisationDescription {
		@Size(EchFormats.language) @NotEmpty
		public String language;
		@Size(EchFormats.localisationDescriptionLong) @NotEmpty
		public String descriptionLong;
		@Size(EchFormats.localisationDescriptionShort)
		public String descriptionShort;
		@Size(EchFormats.localisationIndexName)
		public String descriptionIndex;
		
		public String getTableText() {
			if (!StringUtils.isEmpty(descriptionShort)) {
				return descriptionShort;
			} else {
				return descriptionLong;
			}
		}
	}
	
	public static enum LocalisationStatus implements EchCode {
		_9811, _9812, _9813, _9814;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static enum NumberingPrinciple implements EchCode {
		_9830, _9832, _9835, _9836, _9837, _9839;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
}
