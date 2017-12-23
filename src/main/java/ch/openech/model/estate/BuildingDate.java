package ch.openech.model.estate;

import ch.openech.model.common.DatePartiallyKnown;

public class BuildingDate /* implements Validatable */ {

	public DatePartiallyKnown date;
	public PeriodOfConstruction periodOfConstruction;
	
	/*
	 <xs:choice>
			<xs:element name="yearMonthDay">
				<xs:simpleType>
					<xs:restriction base="xs:date">
						<xs:minInclusive value="1000-01-01"/>
						<xs:maxInclusive value="2099-12-31"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
			<xs:element name="yearMonth">
				<xs:simpleType>
					<xs:restriction base="xs:gYearMonth">
						<xs:minInclusive value="1000-01"/>
						<xs:maxInclusive value="2099-12"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
			<xs:element name="year">
				<xs:simpleType>
					<xs:restriction base="xs:gYear">
						<xs:minInclusive value="1000"/>
						<xs:maxInclusive value="2099"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
			<xs:element name="periodOfConstruction" type="eCH-0129:periodOfConstructionType"/>
		</xs:choice>
		
	 */
}
