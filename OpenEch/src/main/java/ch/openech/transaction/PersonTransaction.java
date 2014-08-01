package ch.openech.transaction;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.Transaction;

import ch.openech.xml.read.StaxEch0020;

public class PersonTransaction implements Transaction<Long> {
	private static final long serialVersionUID = 1L;

	private final List<String> xmls;
	
	public PersonTransaction(List<String> xmls) {
		this.xmls = xmls;
	}

	public PersonTransaction(String xml) {
		this.xmls = Collections.singletonList(xml);
	}
	
	@Override
	public Long execute(Backend backend) {
		long insertId = -1;
		StaxEch0020 stax = new StaxEch0020(backend);
		for (String xml : xmls) {
			try {
				stax.process(xml);
				if (insertId == -1) {
					insertId = stax.getInsertId();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return insertId;
	}
	
}
