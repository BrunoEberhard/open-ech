package ch.openech.transaction;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.Transaction;

import ch.openech.xml.read.StaxEch0148;

public class OrganisationTransaction implements Transaction<Object> {
	private static final long serialVersionUID = 1L;

	private final List<String> xmls;
	
	public OrganisationTransaction(List<String> xmls) {
		this.xmls = xmls;
	}

	public OrganisationTransaction(String xml) {
		this.xmls = Collections.singletonList(xml);
	}

	@Override
	public Object execute(Backend backend) {
		Object insertId = null;
		StaxEch0148 stax = new StaxEch0148(backend);
		for (String xml : xmls) {
			try {
				stax.process(xml);
				if (insertId == null) {
					insertId = stax.getInsertId();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return insertId;
	}
	
}
