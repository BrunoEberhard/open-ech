package ch.openech.business;

import java.io.Serializable;

import ch.openech.mj.backend.Backend;
import ch.openech.mj.transaction.Transaction;
import ch.openech.mj.util.SerializationContainer;
import ch.openech.xml.read.StaxEch0148;

public class OrganisationTransaction implements Transaction<Serializable> {
	private static final long serialVersionUID = 1L;

	private final String[] xmls;
	
	public OrganisationTransaction(String... xmls) {
		this.xmls = xmls;
	}
	
	@Override
	public Serializable execute(Backend backend) {
		StaxEch0148 stax = new StaxEch0148(backend);
		for (String xml : xmls) {
			try {
				stax.process(xml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (Serializable) SerializationContainer.wrap(stax.getLastChanged());
	}
	
}
