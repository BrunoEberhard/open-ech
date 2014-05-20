package ch.openech.sedex;

import java.io.File;
import java.io.FileInputStream;

import  ch.openech.model.Envelope;
import  ch.openech.model.person.Person;
import ch.openech.xml.read.ParserTarget93;
import ch.openech.xml.read.StaxEch0093;

public class SedexPersonLoader {

	public static Person readPerson(Envelope envelope) {
		String directoryName = null; // TODO PreferencesHelper.preferences().get("sedexInput", null);
		File directory = new File(directoryName);
		
		class OverviewParserTarget93  implements ParserTarget93 {
			public Person result;
			
			@Override
			public void death(Person person) {
				result = person;
			}

			@Override
			public void moveIn(Person person) {
				result = person;
			}

			@Override
			public void moveOut(Person person) {
				result = person;
			}
		}
		
		String fileNameMessage = "data_" + envelope.messageId + ".xml";
		OverviewParserTarget93 parserTarget = new OverviewParserTarget93();
		StaxEch0093 staxEch0093 = new StaxEch0093(parserTarget);
		try {
			staxEch0093.process(new FileInputStream(new File(directory, fileNameMessage)), null);
			return parserTarget.result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
