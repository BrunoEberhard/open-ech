package ch.openech.transaction;

import java.io.InputStream;

import org.minimalj.backend.Persistence;
import org.minimalj.transaction.InputStreamTransaction;
import org.minimalj.transaction.PersistenceTransaction;

import ch.openech.xml.read.StaxEch0020;

public class PersonImportTransaction extends InputStreamTransaction<Integer> implements PersistenceTransaction<Integer> {
	private static final long serialVersionUID = 1L;
	
	public PersonImportTransaction(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public Integer execute(Persistence persistence) {
		try {
			StaxEch0020 stax = new StaxEch0020(persistence);
			stax.process(getStream(), null);
			return 0; // count
		} catch (Exception x) {
			x.printStackTrace();
			return 0; // count
		}
	}
	
}
