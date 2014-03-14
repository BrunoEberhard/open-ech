package ch.openech.business;

import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;
import ch.openech.mj.db.Transaction;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.mj.util.LoggingRuntimeException;
import ch.openech.xml.read.StaxEch0020;
import ch.openech.xml.read.StaxEch0148;
import ch.openech.xml.read.StaxEchParser;

public class EchServiceImpl implements EchService {
	public static final Logger logger = Logger.getLogger(EchServiceImpl.class.getName());
	private final DbService dbService;
	private final StaxEch0020 ech0020;
	private final StaxEch0148 ech0148;
	
	public EchServiceImpl() {
		try {
			dbService = Services.get(DbService.class);
			ech0020 = new StaxEch0020(dbService);
			ech0148 = new StaxEch0148(dbService);
		} catch (Exception x) {
			throw new LoggingRuntimeException(x, logger, "Couldnt initialize " + this.getClass().getSimpleName());
		}
	}

	//
	
	@Override
	public Person process(final String... xmlStrings) {
		return process(ech0020, xmlStrings);
	}

	@Override
	public Organisation processOrg(String... xmlStrings) {
		return process(ech0148, xmlStrings);
	}
	
	private <T> T process(final StaxEchParser<T> parser, final String... xmlStrings) {
		if (xmlStrings.length > 1) {
			logger.fine("process " + xmlStrings.length + " xmls");
		}

		T result = null;
		try {
			result = dbService.transaction(new Transaction<T>() {
				@Override
				public T execute() {
					for (String xmlString : xmlStrings) {
						logger.fine(xmlString);
						try {
							parser.process(xmlString);
						} catch (XMLStreamException e) {
							// TODO process should not throw that (but runtime)
							e.printStackTrace();
						}
					}
					return parser.getLastChanged();
				}
			}, "Process xmls");
		} catch (Exception x) {
			throw new LoggingRuntimeException(x, logger, "Person parsing process failed");
		}
		return result;
	}
	
	/*
	public boolean generatePerson(Integer number) {
		if (number == null) return true;
		
		count = number;
		if (count > 0) {
			final int parallel = 10;
			final AtomicInteger success = new AtomicInteger();
			final AtomicInteger fail = new AtomicInteger();
			for (int i = 0; i<parallel; i++) {
				new Thread(new Runnable() {
					public void run() {
						final WriterEch0020 writerEch0020 = new WriterEch0020(ewkNamespaceContext);
						for (int saveProgress = 0; saveProgress<count / parallel; saveProgress++) {
							try {
								DataGenerator.generatePerson(writerEch0020);
								int value = success.addAndGet(1);
								if (value % 10 == 0) {
									System.out.println(success);
								}
							} catch (Exception x) {
								fail.addAndGet(1);
								System.out.println(fail);
							}
							// progress(saveProgress, count);
						}
					}
				}).start();
			}
		} else {
			showError("" + number);
			return false;
		}
		return true;
	}	
	*/
	
	
}
