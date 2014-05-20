package ch.openech.transaction;

import java.io.Serializable;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.Transaction;
import org.minimalj.util.SerializationContainer;

import ch.openech.xml.read.StaxEch0020;

public class PersonTransaction implements Transaction<Serializable> {
	private static final long serialVersionUID = 1L;

	private final String[] xmls;
	
	public PersonTransaction(String... xmls) {
		this.xmls = xmls;
	}
	
	@Override
	public Serializable execute(Backend backend) {
		StaxEch0020 stax = new StaxEch0020(backend);
		for (String xml : xmls) {
			try {
				stax.process(xml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SerializationContainer.wrap(stax.getLastChanged());
	}
	
}
