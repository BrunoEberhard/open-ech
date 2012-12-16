package ch.openech.sedex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import ch.openech.dm.Envelope;
import ch.openech.dm.SedexMessageInformation;
import ch.openech.dm.person.Person;
import ch.openech.mj.db.model.ColumnProperties;
import ch.openech.xml.read.ParserTarget93;
import ch.openech.xml.read.StaxEch0090;
import ch.openech.xml.read.StaxEch0093;

public class SedexDirectoryScanner {

	public static List<Envelope> scan(File directory) {
		if (!directory.isDirectory()) throw new IllegalArgumentException(directory + " not a directory");
		if (!directory.canRead()) throw new IllegalArgumentException(directory + " not readable");

		List<Envelope> result = new ArrayList<Envelope>();
		
		for (File file : directory.listFiles()) {
			String fileName = file.getName();
			if (!fileName.startsWith("envl_")) continue;
			if (!fileName.endsWith(".xml")) continue;
			
			try {
				Envelope envelope = StaxEch0090.process(file);
				if (envelope != null && "93".equals(envelope.messageType)) {
					result.add(readMessageInformation(envelope, directory, fileName));
				} else {
					// result.add(envelope);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	private static SedexMessageInformation readMessageInformation(Envelope envelope, File directory, String fileNameEnvelope) {
		final SedexMessageInformation message = new SedexMessageInformation();
		ColumnProperties.copy(envelope, message);

		class OverviewParserTarget93 implements ParserTarget93 {
			@Override
			public void death(Person person) {
				message.type = "death";
				ColumnProperties.copy(person, message);
			}

			@Override
			public void moveIn(Person person) {
				message.type = "moveIn";
				ColumnProperties.copy(person, message);
			}

			@Override
			public void moveOut(Person person) {
				ColumnProperties.copy(person, message);
			}
		}
		
		String fileNameMessage = "data_" + fileNameEnvelope.substring(5);
		StaxEch0093 staxEch0093 = new StaxEch0093(new OverviewParserTarget93());
		try {
			staxEch0093.process(new FileInputStream(new File(directory, fileNameMessage)), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}
	
}
