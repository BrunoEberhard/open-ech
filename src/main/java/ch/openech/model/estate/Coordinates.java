package ch.openech.model.estate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Decimal;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.resources.Resources;

import ch.openech.model.types.EchCode;

public class Coordinates implements Validation {
	// TODO something clever to fusion east/north, x/y
	
	public static final Coordinates $ = Keys.of(Coordinates.class);
	
	public static final BigDecimal MIN_EAST = new BigDecimal("2480000.000");
	public static final BigDecimal MAX_EAST = new BigDecimal("2840000.999");
	public static final BigDecimal MIN_NORTH = new BigDecimal("1070000.000");
	public static final BigDecimal MAX_NORTH = new BigDecimal("1300000.999");
	
	public static final BigDecimal MIN_X = new BigDecimal("480000.000");
	public static final BigDecimal MAX_X = new BigDecimal("840000.999");
	public static final BigDecimal MIN_Y = new BigDecimal("70000.000");
	public static final BigDecimal MAX_Y = new BigDecimal("300000.999");
	
	// entweder east, north oder X, Y
	
	@Size(10) @Decimal(3)
	public BigDecimal east, north;
	
	@Size(9) @Decimal(3)
	public BigDecimal x, y;
	
	@NotEmpty
	public OriginOfCoordinates originOfCoordinates;
	
	@Override
	public List<ValidationMessage> validate() {
		List<ValidationMessage> messages = new ArrayList<>();
		
		if (east != null) {
			if (east.compareTo(MIN_EAST) < 0 || east.compareTo(MAX_EAST) > 0) {
				messages.add(new ValidationMessage($.east, Resources.getString("ObjectValidator.message")));
			}
		}	
		if (north != null) {
			if (north.compareTo(MIN_NORTH) < 0 || north.compareTo(MAX_NORTH) > 0) {
				messages.add(new ValidationMessage($.north, Resources.getString("ObjectValidator.message")));
			}
		}
		if (x != null) {
			if (x.compareTo(MIN_X) < 0 || x.compareTo(MAX_X) > 0) {
				messages.add(new ValidationMessage($.x, Resources.getString("ObjectValidator.message")));
			}
		}	
		if (y != null) {
			if (y.compareTo(MIN_Y) < 0 || y.compareTo(MAX_Y) > 0) {
				messages.add(new ValidationMessage($.y, Resources.getString("ObjectValidator.message")));
			}
		}
		
		return messages;
	}
	
	
	/*
	 	<xs:complexType name="coordinatesType">
		<xs:choice>
			<xs:element name="LV95">
				<xs:complexType>
					<xs:sequence>
						<xs:element name="east">
							<xs:simpleType>
								<xs:restriction base="xs:decimal">
									<xs:minInclusive value="2480000.000"/>
									<xs:totalDigits value="10"/>
									<xs:fractionDigits value="3"/>
									<xs:maxInclusive value="2840000.999"/>
								</xs:restriction>
							</xs:simpleType>
						</xs:element>
						<xs:element name="north">
							<xs:simpleType>
								<xs:restriction base="xs:decimal">
									<xs:totalDigits value="10"/>
									<xs:fractionDigits value="3"/>
									<xs:minInclusive value="1070000.000"/>
									<xs:maxInclusive value="1300000.999"/>
								</xs:restriction>
							</xs:simpleType>
						</xs:element>
						<xs:element name="originOfCoordinates" type="eCH-0129:originOfCoordinatesType"/>
					</xs:sequence>
				</xs:complexType>
			</xs:element>
			<xs:element name="LV03">
				<xs:complexType>
					<xs:sequence>
						<xs:element name="Y">
							<xs:simpleType>
								<xs:restriction base="xs:decimal">
									<xs:minInclusive value="480000.000"/>
									<xs:totalDigits value="9"/>
									<xs:fractionDigits value="3"/>
									<xs:maxInclusive value="840000.999"/>
								</xs:restriction>
							</xs:simpleType>
						</xs:element>
						<xs:element name="X">
							<xs:simpleType>
								<xs:restriction base="xs:decimal">
									<xs:totalDigits value="9"/>
									<xs:fractionDigits value="3"/>
									<xs:minInclusive value="070000.000"/>
									<xs:maxInclusive value="300000.999"/>
								</xs:restriction>
							</xs:simpleType>
						</xs:element>
						<xs:element name="originOfCoordinates" type="eCH-0129:originOfCoordinatesType"/>
					</xs:sequence>
				</xs:complexType>
			</xs:element>
		</xs:choice>
	</xs:complexType>	
	
	 */
	
	public static enum CoordinatesType {
		LV95, LV03;
	}
	
	public static enum OriginOfCoordinates implements EchCode {
		_901, _902, _903, _904, _905, _906, _909;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	/*
	<xs:simpleType name="originOfCoordinatesType">
	<xs:restriction base="xs:nonNegativeInteger">
		<xs:enumeration value="901"/>
		<xs:enumeration value="902"/>
		<xs:enumeration value="903"/>
		<xs:enumeration value="904"/>
		<xs:enumeration value="905"/>
		<xs:enumeration value="906"/>
		<xs:enumeration value="909"/>
	</xs:restriction>
	</xs:simpleType>
	*/
}
