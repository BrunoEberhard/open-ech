package ch.openech.business;

import java.io.InputStream;

import ch.openech.mj.backend.Backend;
import ch.openech.mj.transaction.StreamConsumer;
import ch.openech.xml.read.StaxEch0020;

public class PersonImportStreamConsumer implements StreamConsumer<Integer> {
	private static final long serialVersionUID = 1L;

	@Override
	public Integer comsume(Backend backend, InputStream stream) {
		try {
			StaxEch0020 stax = new StaxEch0020(backend);
			stax.process(stream, null);
			return 0; // count
		} catch (Exception x) {
			x.printStackTrace();
			return 0; // count
		}
	}
	
}
