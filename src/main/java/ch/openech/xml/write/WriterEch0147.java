package ch.openech.xml.write;

/*
 * eCH 0147 durchbricht leider die Namensschema. Daher wird momentan nur die Major Version 1
 * unterstützt in der Hoffnung dass sich die späteren Versionen wieder an die Konventionen halten.
 */
public class WriterEch0147 {

	public static final String URI0 = "http://www.ech.ch/xmlns/eCH-0147/T0/1";
	public static final String URI1 = "http://www.ech.ch/xmlns/eCH-0147/T1/1";
	public static final String URI2 = "http://www.ech.ch/xmlns/eCH-0147/T2/1";
	
	public WriterEch0147() {
	}

	/* 
	public void organisationIdentification(WriterElement parent, Organisation values) throws Exception {
		OrganisationIdentification organisationIdentification = ViewUtil.view(values, new OrganisationIdentification());
		organisationIdentification(parent, organisationIdentification);
	}
	*/

	
}
