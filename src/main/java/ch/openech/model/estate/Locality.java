package ch.openech.model.estate;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;

public class Locality {

	@Size(4)
	public Integer swissZipCode;
	@Size(2)
	public Integer swissZipCodeAddOn;

	public final LocalityName name = new LocalityName();
	
	public static final class LocalityName {
		@Size(EchFormats.language) @NotEmpty
		public String language;
		@Size(40) @NotEmpty
		public String nameLong;
		@Size(18) @NotEmpty
		public String nameShort;
	}
	
	/*
	 	<xs:sequence>
			<xs:element name="swissZipCode" type="eCH-0010:swissZipCodeType" minOccurs="0"/>
			<xs:element name="swissZipCodeAddOn" type="eCH-0010:swissZipCodeAddOnType" minOccurs="0"/>
			<xs:element name="name" type="eCH-0129:localityNameType"/>
		</xs:sequence>
		
	 */
}
